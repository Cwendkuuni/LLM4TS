package nu.staldal.lagoon;

import java.util.*;
import org.xml.sax.*;
import nu.staldal.lagoon.core.*;
import java.io.*;
import nu.staldal.xtree.*;

public class LagoonCLI
{
    private static boolean DEBUG;
    private static final String syntaxMsg = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
    private static Properties properties;
    
    public static void main(final String[] args) {
        boolean force = false;
        boolean build = false;
        long interval = 0L;
        if (args.length < 1) {
            System.out.println("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]");
            return;
        }
        if (args.length > 1 && args[1].equals("build")) {
            build = true;
        }
        else if (args.length > 1 && args[1].equals("force")) {
            force = true;
        }
        else if (args.length > 1) {
            try {
                interval = 1000 * Integer.parseInt(args[1]);
            }
            catch (NumberFormatException e5) {
                System.out.println("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]");
                return;
            }
            if (interval < 1L) {
                System.out.println("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]");
                return;
            }
        }
        LagoonProcessor processor;
        try {
            System.out.println("Initializing Lagoon...");
            String targetURL;
            File sourceDir;
            File sitemapFile;
            String password;
            if (args[0].endsWith(".xml") || args[0].endsWith(".sitemap")) {
                targetURL = System.getProperty("user.dir");
                sourceDir = new File(targetURL);
                sitemapFile = new File(args[0]);
                password = null;
            }
            else {
                final File propertyFile = new File(args[0]);
                LagoonCLI.properties = new Properties();
                final FileInputStream fis = new FileInputStream(propertyFile);
                LagoonCLI.properties.load(fis);
                fis.close();
                targetURL = getProperty("targetURL");
                sitemapFile = new File(getProperty("sitemapFile"));
                sourceDir = new File(getProperty("sourceDir"));
                password = LagoonCLI.properties.getProperty("password");
            }
            Element sitemapTree;
            try {
                sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(sitemapFile), false);
            }
            catch (SAXException e) {
                final Exception ee = e.getException();
                if (ee == null) {
                    e.printStackTrace();
                    throw new LagoonException(e.getMessage());
                }
                if (ee instanceof IOException) {
                    throw (IOException)ee;
                }
                ee.printStackTrace();
                throw new LagoonException(ee.getMessage());
            }
            processor = new LagoonProcessor(targetURL, sitemapTree, sourceDir, password, new PrintWriter(System.out, true), new PrintWriter(System.err, true));
        }
        catch (AuthenticationMissingException e6) {
            System.err.println("Error while initializing Lagoon:");
            System.err.println("Password is required but not specified");
            return;
        }
        catch (AuthenticationException e7) {
            System.err.println("Error while initializing Lagoon:");
            System.err.println("Incorrect password");
            return;
        }
        catch (FileNotFoundException e2) {
            System.err.println("Error while initializing Lagoon:");
            System.err.println("File not found: " + e2.getMessage());
            return;
        }
        catch (IOException e3) {
            System.err.println("Error while initializing Lagoon:");
            System.err.println("I/O error: " + e3.toString());
            if (LagoonCLI.DEBUG) {
                e3.printStackTrace();
            }
            return;
        }
        catch (LagoonException e4) {
            System.err.println("Error while initializing Lagoon:");
            System.err.println(e4.getMessage());
            return;
        }
        System.out.println("Lagoon initialized successfully");
        try {
            if (build) {
                System.out.println("Building website...");
                final long timeBefore = System.currentTimeMillis();
                if (!processor.build(false)) {
                    System.out.println("...with errors...");
                }
                final long timeElapsed = System.currentTimeMillis() - timeBefore;
                showTime(timeElapsed);
            }
            else if (force) {
                System.out.println("Force building website...");
                final long timeBefore = System.currentTimeMillis();
                if (!processor.build(true)) {
                    System.out.println("...with errors...");
                }
                final long timeElapsed = System.currentTimeMillis() - timeBefore;
                showTime(timeElapsed);
            }
            else if (interval > 0L) {
                while (true) {
                    System.out.println("Building website...");
                    final long timeBefore = System.currentTimeMillis();
                    if (!processor.build(false)) {
                        System.out.println("...with errors...");
                    }
                    final long timeElapsed = System.currentTimeMillis() - timeBefore;
                    showTime(timeElapsed);
                    if (timeElapsed < interval) {
                        try {
                            Thread.sleep(interval - timeElapsed);
                        }
                        catch (InterruptedException ex) {}
                    }
                }
            }
            else {
                final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    System.out.println("Ready");
                    final String s = in.readLine();
                    if (s.length() < 1) {
                        continue;
                    }
                    final char c = Character.toLowerCase(s.charAt(0));
                    if (c == 'b') {
                        System.out.println("Building website...");
                        final long timeBefore2 = System.currentTimeMillis();
                        if (!processor.build(false)) {
                            System.out.println("...with errors...");
                        }
                        final long timeElapsed2 = System.currentTimeMillis() - timeBefore2;
                        showTime(timeElapsed2);
                    }
                    else if (c == 'f') {
                        System.out.println("Force building website...");
                        final long timeBefore2 = System.currentTimeMillis();
                        if (!processor.build(true)) {
                            System.out.println("...with errors...");
                        }
                        final long timeElapsed2 = System.currentTimeMillis() - timeBefore2;
                        showTime(timeElapsed2);
                    }
                    else {
                        if (c == 'q') {
                            break;
                        }
                        continue;
                    }
                }
            }
            if (LagoonCLI.DEBUG) {
                final Thread[] threads = new Thread[Thread.activeCount()];
                Thread.enumerate(threads);
                for (int i = 0; i < threads.length; ++i) {
                    if (threads[i] != null && threads[i] != Thread.currentThread()) {
                        System.out.println("Thread still running: " + threads[i]);
                    }
                }
            }
            System.out.println("Exiting Lagoon...");
            processor.destroy();
            System.exit(0);
        }
        catch (FileNotFoundException e2) {
            System.err.println("File not found: " + e2.getMessage());
        }
        catch (IOException e3) {
            System.err.println("I/O error: " + e3.toString());
            if (LagoonCLI.DEBUG) {
                e3.printStackTrace();
            }
        }
    }
    
    private static String getProperty(final String name) throws LagoonException {
        final String value = LagoonCLI.properties.getProperty(name);
        if (value == null) {
            throw new LagoonException("Property " + name + " not specified");
        }
        return value.trim();
    }
    
    private static void showTime(final long ms) {
        if (ms < 10000L) {
            System.out.println("in " + ms + " ms");
        }
        else {
            System.out.println("in " + ms / 1000L + " s");
        }
    }
    
    static {
        LagoonCLI.DEBUG = false;
    }
}
