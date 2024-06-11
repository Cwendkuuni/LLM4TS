import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);

        assertNotNull(instance);
        assertEquals(parent, instance.getParent());
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);

        assertNotNull(classLoader);
        assertEquals(parent, classLoader.getParent());
    }

    @Test
    public void testAddUrls() {
        String dirName = "test/resources";
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, dirName);

        assertEquals(1, urls.size());
        assertTrue(urls.get(0).toString().endsWith("test/resources/test.jar"));
    }
}