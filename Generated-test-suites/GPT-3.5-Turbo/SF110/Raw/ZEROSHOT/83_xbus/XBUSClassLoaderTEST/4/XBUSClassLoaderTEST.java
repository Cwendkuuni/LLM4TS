package net.sf.xbus.base.core.reflection;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class XBUSClassLoader extends URLClassLoader
{
    private static XBUSClassLoader mClassLoader;
    private static final Object classLock;
    
    private XBUSClassLoader(final URL[] urlArray, final ClassLoader parent) {
        super(urlArray, parent);
    }
    
    public static XBUSClassLoader getInstance(final ClassLoader parent) {
        return createClassLoader(parent);
    }
    
    private static XBUSClassLoader createClassLoader(final ClassLoader parent) {
        synchronized (XBUSClassLoader.classLock) {
            if (XBUSClassLoader.mClassLoader == null) {
                if (Constants.XBUS_HOME == null) {
                    Trace.error("XBUS_HOME has not been set!");
                    System.exit(1);
                }
                final Vector urls = new Vector();
                addUrls(urls, Constants.XBUS_HOME + "/lib");
                addUrls(urls, Constants.XBUS_HOME + "/lib/runtime");
                addUrls(urls, Constants.XBUS_HOME + "/plugin/lib");
                addUrls(urls, Constants.XBUS_HOME + "/test/lib");
                final URL[] urlArray = new URL[urls.size()];
                for (int i = 0; i < urls.size(); ++i) {
                    urlArray[i] = urls.elementAt(i);
                }
                XBUSClassLoader.mClassLoader = new XBUSClassLoader(urlArray, parent);
            }
        }
        return XBUSClassLoader.mClassLoader;
    }
    
    private static void addUrls(final Vector urls, final String dirName) {
        final File libPath = new File(dirName);
        final File[] jars = libPath.listFiles();
        for (int i = 0; jars != null && i < jars.length; ++i) {
            if (jars[i].isFile()) {
                if (!jars[i].getName().endsWith("jar")) {
                    if (!jars[i].getName().endsWith("zip")) {
                        continue;
                    }
                }
                try {
                    urls.add(jars[i].toURL());
                }
                catch (MalformedURLException e) {
                    Trace.error(e);
                    System.exit(1);
                }
            }
        }
    }
    
    static {
        XBUSClassLoader.mClassLoader = null;
        classLock = XBUSClassLoader.class;
    }
}