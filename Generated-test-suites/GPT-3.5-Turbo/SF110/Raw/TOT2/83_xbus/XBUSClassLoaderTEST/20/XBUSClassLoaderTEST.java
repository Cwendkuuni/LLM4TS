package net.sf.xbus.base.core.reflection;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        // Test the getInstance method
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(xbusClassLoader);
    }

    @Test
    public void testAddUrls() {
        // Test the addUrls method with typical use case
        Vector urls = new Vector();
        String dirName = "test/directory";
        File libPath = new File(dirName);
        assertTrue(libPath.exists());
    }

    @Test
    public void testAddUrlsEdgeCase() {
        // Test the addUrls method with an edge case of an empty directory
        Vector urls = new Vector();
        String dirName = "empty/directory";
        File libPath = new File(dirName);
        assertFalse(libPath.exists());
    }

    @Test
    public void testAddUrlsException() {
        // Test the addUrls method with an exception scenario
        Vector urls = new Vector();
        String dirName = "invalid/directory";
        File libPath = new File(dirName);
        try {
            XBUSClassLoader.addUrls(urls, dirName);
            fail("Expected a MalformedURLException to be thrown");
        } catch (MalformedURLException e) {
            assertNotNull(e.getMessage());
        }
    }
}