import static org.junit.Assert.*;
import org.junit.Test;
import java.net.MalformedURLException;
import java.io.File;
import java.net.URL;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parent = new ClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);
        assertSame(parent, classLoader.getParent());
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = new ClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
        assertSame(parent, classLoader.getParent());
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String dirName = "path/to/directory";
        XBUSClassLoader.addUrls(urls, dirName);
        
        // Assert that urls contains valid URL objects
        for (Object url : urls) {
            assertTrue(url instanceof URL);
        }
    }

}