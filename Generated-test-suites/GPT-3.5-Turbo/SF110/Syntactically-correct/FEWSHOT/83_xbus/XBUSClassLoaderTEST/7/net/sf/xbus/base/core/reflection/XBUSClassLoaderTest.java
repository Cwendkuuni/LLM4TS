import static org.junit.Assert.*;
import org.junit.Test;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;
import java.util.Vector;

public class XBUSClassLoaderTest {
    @Test
    public void testGetInstance() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
        assertNotNull(instance);
        assertEquals(XBUSClassLoader.class, instance.getClass());
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
        assertEquals(XBUSClassLoader.class, classLoader.getClass());
    }

    @Test
    public void testCreateClassLoaderWhenXbusHomeIsNull() {
        ClassLoader parent = getClass().getClassLoader();
        String originalXbusHome = Constants.XBUS_HOME;
        Constants.XBUS_HOME = null;

        try {
            XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
            assertNull(classLoader);
            // Verify that Trace.error() is called
          
        } finally {
            Constants.XBUS_HOME = originalXbusHome;
        }
    }

    @Test
    public void testAddUrls() throws MalformedURLException {
        Vector<URL> urls = new Vector<>();
        String dirName = "/path/to/lib";
        File libPath = new File(dirName);
        File[] jars = libPath.listFiles();

        assertNotNull(jars);

        for (File jar : jars) {
            if (jar.isFile()) {
                String jarName = jar.getName();

                if (jarName.endsWith("jar") || jarName.endsWith("zip")) {
                    URL url = new URL("file:" + jar.getAbsolutePath());
                    urls.add(url);
                }
            }
        }

        assertEquals(jars.length, urls.size());
    }
}