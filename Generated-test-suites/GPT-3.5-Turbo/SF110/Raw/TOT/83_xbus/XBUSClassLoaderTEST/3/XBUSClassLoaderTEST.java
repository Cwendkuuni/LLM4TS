import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import net.sf.xbus.base.core.trace.Trace;

public class XBUSClassLoaderTest {
    
    private XBUSClassLoader classLoader;
    
    @Before
    public void setUp() {
        classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
    }
    
    @Test
    public void testGetInstance() {
        assertNotNull("getInstance() should return a non-null instance", classLoader);
        assertSame("getInstance() should return the same instance", classLoader, XBUSClassLoader.getInstance(getClass().getClassLoader()));
        assertNotSame("getInstance() should return a new instance with a different parent class loader", classLoader, XBUSClassLoader.getInstance(classLoader));
    }
    
    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader newClassLoader = XBUSClassLoader.createClassLoader(classLoader);
        assertNotNull("createClassLoader() should return a non-null instance", newClassLoader);
        assertNotSame("createClassLoader() should return a new instance", classLoader, newClassLoader);
        assertSame("createClassLoader() should return the same instance when called with the same parent class loader", newClassLoader, XBUSClassLoader.createClassLoader(classLoader));
    }
    
    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        File jarFile = new File("test.jar");
        try {
            URL jarUrl = jarFile.toURI().toURL();
            XBUSClassLoader.addUrls(urls, jarFile.getParent());
            assertTrue("addUrls() should add the URL of the jar file to the vector", urls.contains(jarUrl));
            
            File txtFile = new File("test.txt");
            URL txtUrl = txtFile.toURI().toURL();
            XBUSClassLoader.addUrls(urls, txtFile.getParent());
            assertFalse("addUrls() should not add the URL of the text file to the vector", urls.contains(txtUrl));
            
            File subDir = new File("subdir");
            assertTrue(subDir.mkdir());
            XBUSClassLoader.addUrls(urls, subDir.getParent());
            assertFalse("addUrls() should not add the URLs of files in subdirectories to the vector", urls.contains(txtUrl));
            
            File zipFile = new File("test.zip");
            URL zipUrl = zipFile.toURI().toURL();
            XBUSClassLoader.addUrls(urls, zipFile.getParent());
            assertTrue("addUrls() should add the URL of the zip file to the vector", urls.contains(zipUrl));
            
            File invalidSubDir = new File("invalidsubdir");
            File invalidFile = new File(invalidSubDir, "test.txt");
            assertTrue(invalidSubDir.mkdir());
            assertTrue(invalidFile.createNewFile());
            XBUSClassLoader.addUrls(urls, invalidSubDir.getParent());
            assertFalse("addUrls() should not add the URLs of files in invalid subdirectories to the vector", urls.contains(invalidFile.toURI().toURL()));
        } catch (MalformedURLException e) {
            fail("Failed to create URL: " + e.getMessage());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getClass().getName() + " - " + e.getMessage());
        } finally {
            deleteFile(jarFile);
            deleteFile(txtFile);
            deleteFile(subDir);
            deleteFile(zipFile);
            deleteFile(invalidSubDir);
        }
    }
    
    private void deleteFile(File file) {
        if (file != null && file.exists() && !file.delete()) {
            Trace.warn("Failed to delete file: " + file.getName());
        }
    }
}