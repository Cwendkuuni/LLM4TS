package net.sf.xbus.base.core.reflection;

import org.junit.Test;
import static org.junit.Assert.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        XBUSClassLoader instance = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(instance);
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        try {
            XBUSClassLoader.addUrls(urls, "invalid_directory_path");
            fail("MalformedURLException should have been thrown");
        } catch (MalformedURLException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNotNull(classLoader);
    }
}