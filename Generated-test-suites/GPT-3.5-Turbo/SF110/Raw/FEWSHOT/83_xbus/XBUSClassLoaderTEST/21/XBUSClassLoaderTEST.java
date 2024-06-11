package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {

    @Test
    public void testConstructor() {
        URL[] urls = new URL[0];
        ClassLoader parent = mock(ClassLoader.class);

        XBUSClassLoader classLoader = new XBUSClassLoader(urls, parent);

        assertSame(urls, classLoader.getURLs());
        assertSame(parent, classLoader.getParent());
    }

    @Test
    public void testGetInstance() {
        ClassLoader parent = mock(ClassLoader.class);

        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);

        assertSame(XBUSClassLoader.mClassLoader, classLoader);
        verifyStatic(XBUSClassLoader.class);
        XBUSClassLoader.createClassLoader(parent);
    }

    @Test
    public void testCreateClassLoader() throws MalformedURLException {
        ClassLoader parent = mock(ClassLoader.class);
        Vector<URL> urls = new Vector<>();
        urls.add(new URL("file:/path/to/lib"));

        mockStatic(XBUSClassLoader.class);
        when(XBUSClassLoader.createClassLoader(parent)).thenCallRealMethod();
        doReturn(null).when(XBUSClassLoader.classLock);

        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);

        assertSame(XBUSClassLoader.mClassLoader, classLoader);
        verifyStatic(XBUSClassLoader.class);
        XBUSClassLoader.createClassLoader(parent);
        verifyStatic(Constants.class);
        Constants.XBUS_HOME = "/path/to/xbus_home";
        verifyStatic(Trace.class);
        Trace.error("XBUS_HOME has not been set!");
        verifyStatic(System.class);
        System.exit(1);
        verifyStatic(XBUSClassLoader.class, times(4));
        XBUSClassLoader.addUrls(urls, "/path/to/xbus_home/lib");
        XBUSClassLoader.addUrls(urls, "/path/to/xbus_home/lib/runtime");
        XBUSClassLoader.addUrls(urls, "/path/to/xbus_home/plugin/lib");
        XBUSClassLoader.addUrls(urls, "/path/to/xbus_home/test/lib");
        verifyStatic(File.class, times(4));
        File libPath = new File("/path/to/xbus_home/lib");
        File libRuntimePath = new File("/path/to/xbus_home/lib/runtime");
        File pluginLibPath = new File("/path/to/xbus_home/plugin/lib");
        File testLibPath = new File("/path/to/xbus_home/test/lib");
        verify(libPath, times(1)).listFiles();
        verify(libRuntimePath, times(1)).listFiles();
        verify(pluginLibPath, times(1)).listFiles();
        verify(testLibPath, times(1)).listFiles();
    }

    @Test
    public void testAddUrls() throws MalformedURLException {
        Vector<URL> urls = new Vector<>();
        String dirName = "/path/to/lib";
        File libPath = mock(File.class);
        File[] jars = new File[0];

        mockStatic(File.class);
        when(new File(dirName)).thenReturn(libPath);
        when(libPath.listFiles()).thenReturn(jars);

        XBUSClassLoader.addUrls(urls, dirName);

        verifyStatic(File.class);
        new File(dirName);
        verify(libPath, times(1)).listFiles();
    }
}