package net.sf.xbus.base.core.reflection;

import org.junit.Test;
import static org.junit.Assert.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parent = XBUSClassLoaderTest.class.getClassLoader();
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(xbusClassLoader);
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = XBUSClassLoaderTest.class.getClassLoader();
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(xbusClassLoader);
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = "src/test/resources";
        XBUSClassLoader.addUrls(urls, dirName);

        String expectedPath = null;
        try {
            Path expected = Paths.get(dirName).toRealPath();
            expectedPath = expected.toString();
        } catch (URISyntaxException | IOException e) {
            fail("Error getting real path of directory");
        }

        for (URL url : urls) {
            String urlPath = null;
            try {
                Path path = Paths.get(url.toURI()).toRealPath();
                urlPath = path.toString();
            } catch (URISyntaxException | IOException e) {
                fail("Error getting real path of URL");
            }
            assertTrue(urlPath.startsWith(expectedPath));
        }
    }
}