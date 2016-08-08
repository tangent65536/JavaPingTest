package tangent65536;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Tangent65536
 * This is used for testing my new X556UB laptop.
 * ALL RIGHTS RESERVED.
 */
public class ThreadPingURL extends Thread
{
	private static final Logger logger = LogManager.getLogger();
	private final Scanner input = new Scanner(System.in);
	private boolean log = true;
	
	private final Map<String, Object> map = new HashMap<String, Object>();
	
	public static void main(String[] args)
	{
		(new ThreadPingURL()).start();
	}
	
	private ThreadPingURL()
	{
		super("JavaPingTest");
		Security.setProperty("networkaddress.cache.ttl", "0");
		try
		{
			//Initialize JVM cache
			this.ping("localhost", false);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		this.log = true;
	}
	
	@Override
	public void run()
	{
		while(this.input.hasNext())
		{
			try
			{
				final String line = this.input.nextLine();
				this.logInfo("Input >> " + line);
				if(line.equalsIgnoreCase("stop") || line.equalsIgnoreCase("exit"))
				{
					this.interrupt();
					this.logInfo("Stopped!");
					System.exit(0);
				}
				else if(line.equals(""))
				{
					continue;
				}
				this.ping(line, true);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			this.logInfo("Please enter an URL to resolve, or type \"stop\" to exit.");
		}
	}
	
	@Override
	public void start()
	{
		super.start();
		this.logInfo("Ping test made by Tangent65536.");
		this.logInfo("Specifically built for testing my new X556UB laptop.");
		this.logInfo("All rights reserved.");
		this.logInfo("--------------------------------");
		this.logInfo("Please enter an URL to resolve, or type \"stop\" to exit.");
	}
	
	private void ping(final String line, boolean _log) throws IOException
	{
		this.log = _log;
		Socket socket = new Socket();
		long time = System.currentTimeMillis();
		this.logInfo("Resolving " + line);
		InetSocketAddress isa = new InetSocketAddress(line, 80);
		InetAddress ia = isa.getAddress();
		if(ia != null)
		{
			this.logInfo(line + " resolved: " + ia.getHostAddress());
			this.logInfo("Resolving address took about " + (System.currentTimeMillis() - time) + "ms");
			
			if(this.map.get(line.toLowerCase()) != null)
			{
				this.logWarn("You've ping-ed this URL before! The resolving time may be unaccurate.");
			}
			else
			{
				this.map.put(line.toLowerCase(), this);
			}
			
			try
			{
				time = System.currentTimeMillis();
				this.logInfo("Ping-ing " + line);
				socket.connect(isa, 10000);
				this.logInfo("Ping-replying took about " + (System.currentTimeMillis() - time) + "ms");
			}
			catch(IOException ioe)
			{
				this.logError("Connection timed out.");
			}
		}
		else
		{
			this.logError("Unable to resolve " + line);
		}
		socket.close();
	}
	
	private void logInfo(String str)
	{
		if(this.log)
		{
			logger.info(str);
		}
	}
	
	private void logWarn(String str)
	{
		if(this.log)
		{
			logger.warn(str);
		}
	}
	
	private void logError(String str)
	{
		if(this.log)
		{
			logger.error(str);
		}
	}
}
