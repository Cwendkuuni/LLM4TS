import static org.junit.Assert.*;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.io.File;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);
        assertEquals(parent, classLoader.getParent());
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
        assertEquals(parent, classLoader.getParent());
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = "testDir";
        File libPath = new File(dirName);
        File[] jars = libPath.listFiles();
        
        XBUSClassLoader.addUrls(urls, dirName);
        
        for (int i = 0; jars != null && i < jars.length; ++i) {
            if (jars[i].isFile()) {
                String name = jars[i].getName();
                if (name.endsWith("jar") || name.endsWith("zip")) {
                    try {
                        assertTrue(urls.contains(jars[i].toURL()));
                    } catch (MalformedURLException e) {
                        fail(e.getMessage());
                    }
                }
            }
        }
    }
}