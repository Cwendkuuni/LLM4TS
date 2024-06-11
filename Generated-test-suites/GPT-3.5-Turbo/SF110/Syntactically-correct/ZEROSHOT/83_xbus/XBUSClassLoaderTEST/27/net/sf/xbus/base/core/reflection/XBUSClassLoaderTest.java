import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;
import java.util.Vector;

public class XBUSClassLoaderTest {

    XBUSClassLoader classLoader;

    @Before
    public void setUp() {
        URL[] urls = new URL[0];
        classLoader = new XBUSClassLoader(urls, this.getClass().getClassLoader());
    }

    @Test
    public void testGetInstance() {
        XBUSClassLoader instance = XBUSClassLoader.getInstance(this.getClass().getClassLoader());
        assertSame(classLoader, instance);
    }

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader createClassLoader = XBUSClassLoader.createClassLoader(this.getClass().getClassLoader());
        assertSame(classLoader, createClassLoader);
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<URL>();
        String dirName = "testDir";
        File libPath = new File(dirName);
        File[] jars = libPath.listFiles();
        for (int i = 0; jars != null && i < jars.length; ++i) {
            if (jars[i].isFile()) {
                if (jars[i].getName().endsWith("jar") || jars[i].getName().endsWith("zip")) {
                    try {
                        urls.add(jars[i].toURL());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        assertEquals(urls.size(), 0);
    }
}