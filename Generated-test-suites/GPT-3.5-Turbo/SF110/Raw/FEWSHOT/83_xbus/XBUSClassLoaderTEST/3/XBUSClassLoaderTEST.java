package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.MalformedURLException;

public class XBUSClassLoaderTest {

    @Test
    public void testCreateClassLoader() {
        // Test scenario where XBUSClassLoader.mClassLoader is null
        XBUSClassLoader.mClassLoader = null;
        assertNull(XBUSClassLoader.createClassLoader(null));
        
        // Test scenario where XBUSClassLoader.mClassLoader is not null
        XBUSClassLoader.mClassLoader = new XBUSClassLoader(new URL[]{}, null);
        assertSame(XBUSClassLoader.mClassLoader, XBUSClassLoader.createClassLoader(null));
    }

    @Test
    public void testGetInstance() {
        // Test scenario where XBUSClassLoader.mClassLoader is null
        XBUSClassLoader.mClassLoader = null;
        XBUSClassLoader instance = XBUSClassLoader.getInstance(null);
        assertNotNull(instance);
        assertSame(XBUSClassLoader.mClassLoader, instance);

        // Test scenario where XBUSClassLoader.mClassLoader is not null
        XBUSClassLoader.mClassLoader = new XBUSClassLoader(new URL[]{}, null);
        instance = XBUSClassLoader.getInstance(null);
        assertNotNull(instance);
        assertSame(XBUSClassLoader.mClassLoader, instance);
    }

    @Test
    public void testAddUrls() {
        // Test scenario where jars is null
        XBUSClassLoader.addUrls(null, "dirName");

        // Test scenario where jars is an empty array
        File[] jars = {};
        XBUSClassLoader.addUrls(new Vector(), "dirName");

        // Test scenario where jars contains files with correct extensions
        jars = new File[]{new File("file.jar"), new File("file.zip")};
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "dirName");
        assertEquals(2, urls.size());

        // Test scenario where jars contains files with incorrect extensions
        jars = new File[]{new File("file.txt"), new File("file.exe")};
        urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "dirName");
        assertEquals(0, urls.size());
    }
}