import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {
    
    @Before
    public void setUp() {
        // Initialize necessary objects or variables
    }
    
    // Test case for getInstance method
    
    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        
        assertNotNull(classLoader);
        assertEquals(getClass().getClassLoader(), classLoader.getParent());
    }
    
    // Test case for createClassLoader method
    
    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        
        assertNotNull(classLoader);
        assertEquals(getClass().getClassLoader(), classLoader.getParent());
        
        // Test for XBUS_HOME null scenario
        // Will exit the system and throw an error
        Constants.XBUS_HOME = null;
        try {
            classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
            fail("Expected System.exit(1) to be called");
        } catch (SecurityException se) {
            assertEquals("XBUS_HOME has not been set!", se.getMessage());
        }
    }
    
    // Test case for addUrls method
    
    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        
        // Test with valid directory name
        String dirName = "testDirectory";
        XBUSClassLoader.addUrls(urls, dirName);
        assertTrue(urls.isEmpty());
        
        // Test with directory containing .jar and .zip files
        dirName = "testDirectory2";
        File testDir = new File(dirName);
        testDir.mkdir();
        File jarFile = new File(dirName + "/test.jar");
        try {
            jarFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File zipFile = new File(dirName + "/test.zip");
        try {
            zipFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(2, urls.size());
        
        // Test with file not ending with .jar or .zip
        File notJarOrZipFile = new File(dirName + "/test.txt");
        try {
            notJarOrZipFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(2, urls.size());
    }
}