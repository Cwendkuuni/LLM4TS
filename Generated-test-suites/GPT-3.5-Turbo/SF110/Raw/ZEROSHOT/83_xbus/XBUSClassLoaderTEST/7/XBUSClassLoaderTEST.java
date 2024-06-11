package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.net.*;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        // Test if the instance returned is not null
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        // Test if the class loader is created
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        // Test if URLs are added correctly to the vector
        Vector<URL> urls = new Vector<>();
        String dirName = "test/dir";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(0, urls.size());

        // Add a jar file and test if it is added to the vector
        String jarFileName = "test.jar";
        File jarFile = new File(dirName + "/" + jarFileName);
        jarFile.createNewFile();
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(1, urls.size());
        jarFile.delete();
    }
}