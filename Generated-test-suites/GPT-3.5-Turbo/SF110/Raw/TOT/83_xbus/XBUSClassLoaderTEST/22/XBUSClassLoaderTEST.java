import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(classLoader);
    }
    
    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNotNull(classLoader);
    }
    
    @Test
    public void testAddUrls_WithValidDirName() {
        Vector<URL> urls = new Vector<>();
        String validDirName = "path/to/valid/dir";
        XBUSClassLoader.addUrls(urls, validDirName);
        assertEquals(0, urls.size());
    }
    
    @Test
    public void testAddUrls_WithInvalidDirName() {
        Vector<URL> urls = new Vector<>();
        String invalidDirName = "path/to/invalid/dir";
        XBUSClassLoader.addUrls(urls, invalidDirName);
        assertEquals(0, urls.size());
    }
    
    @Test
    public void testAddUrls_WithNullDirName() {
        Vector<URL> urls = new Vector<>();
        String nullDirName = null;
        XBUSClassLoader.addUrls(urls, nullDirName);
        assertEquals(0, urls.size());
    }
    
    @Test
    public void testAddUrls_WithFileNotEndingWithJarOrZip() {
        Vector<URL> urls = new Vector<>();
        String dirName = "path/to/dir";
        File file = new File(dirName + "/file.txt");
        file.createNewFile();
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(0, urls.size());
        file.delete();
    }
    
    @Test
    public void testAddUrls_WithValidJarFile() {
        Vector<URL> urls = new Vector<>();
        String dirName = "path/to/dir";
        File jarFile = new File(dirName + "/file.jar");
        try {
            jarFile.createNewFile();
            XBUSClassLoader.addUrls(urls, dirName);
            assertEquals(1, urls.size());
            assertTrue(urls.get(0).getPath().endsWith("file.jar"));
        } 
        catch (Exception e) {
            fail("Exception occurred: " + e);
        }
        finally {
            jarFile.delete();
        }
    }
    
    @Test
    public void testAddUrls_WithValidZipFile() {
        Vector<URL> urls = new Vector<>();
        String dirName = "path/to/dir";
        File zipFile = new File(dirName + "/file.zip");
        try {
            zipFile.createNewFile();
            XBUSClassLoader.addUrls(urls, dirName);
            assertEquals(1, urls.size());
            assertTrue(urls.get(0).getPath().endsWith("file.zip"));
        } 
        catch (Exception e) {
            fail("Exception occurred: " + e);
        }
        finally {
            zipFile.delete();
        }
    }
}