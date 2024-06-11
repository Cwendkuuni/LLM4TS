import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;
import java.io.*;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
        assertNotNull(instance);
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrlsWithValidDirectory() {
        Vector urls = new Vector();
        String dirName = "path/to/valid/directory";
        
        XBUSClassLoader.addUrls(urls, dirName);
        assertFalse(urls.isEmpty());
    }

    @Test
    public void testAddUrlsWithInvalidDirectory() {
        Vector urls = new Vector();
        String dirName = "path/to/invalid/directory";
        
        XBUSClassLoader.addUrls(urls, dirName);
        assertTrue(urls.isEmpty());
    }

    @Test(expected = MalformedURLException.class)
    public void testAddUrlsWithMalformedUrl() {
        Vector urls = new Vector();
        String dirName = "path/to/valid/directory";
        File file = new File(dirName);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            new File(file, "invalidFile").createNewFile();
            XBUSClassLoader.addUrls(urls, dirName);
        } finally {
            file.delete();
        }
    }

    // 
    // Existing JUnit 4 test cases for the public methods of XBUSClassLoader
    // ...
    // 
}