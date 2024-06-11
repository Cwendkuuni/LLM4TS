import static org.junit.Assert.*;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        // Typical Use Case: Parent class loader is provided
        ClassLoader parentClassLoader = getClass().getClassLoader();
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parentClassLoader);
        assertNotNull(instance);
        
        // Edge Case: Parent class loader is null
        XBUSClassLoader instance2 = XBUSClassLoader.getInstance(null);
        assertNull(instance2);
    }
    
    @Test
    public void testCreateClassLoader() {
        // Typical Use Case: XBUS_HOME is set
        ClassLoader parentClassLoader = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parentClassLoader);
        assertNotNull(classLoader);
        
        // Error Scenario: XBUS_HOME is not set
        // We cannot directly test `System.exit(1)`, so we will verify the Trace.error() message instead
        try {
            System.setOut(new PrintStream(new ByteArrayOutputStream()));
            System.setErr(new PrintStream(new ByteArrayOutputStream()));
            XBUSClassLoader.createClassLoader(parentClassLoader);
            fail("Expected Trace.error() but no exception was thrown");
        } catch (IllegalStateException e) {
            assertEquals("XBUS_HOME has not been set!", e.getMessage());
        }
    }
    
    @Test
    public void testAddUrls() {
        // Typical Use Case: Directory contains jar file
        Vector<URL> urls = new Vector<>();
        String dirName = "/path/to/jar/files";
        XBUSClassLoader.addUrls(urls, dirName);
        assertFalse(urls.isEmpty());
        
        // Typical Use Case: Directory contains zip file
        urls.clear();
        dirName = "/path/to/zip/files";
        XBUSClassLoader.addUrls(urls, dirName);
        assertFalse(urls.isEmpty());
        
        // Edge Case: Directory is empty
        urls.clear();
        dirName = "/path/to/empty/directory";
        XBUSClassLoader.addUrls(urls, dirName);
        assertFalse(urls.isEmpty());
        
        // Error Scenario: Directory path is invalid
        // We cannot directly test `System.exit(1)`, so we will verify the Trace.error() message instead
        try {
            String invalidDirName = "/path/to/invalid/directory";
            System.setOut(new PrintStream(new ByteArrayOutputStream()));
            System.setErr(new PrintStream(new ByteArrayOutputStream()));
            XBUSClassLoader.addUrls(urls, invalidDirName);
            fail("Expected Trace.error() but no exception was thrown");
        } catch (MalformedURLException e) {
            assertEquals("Invalid directory path", e.getMessage());
        }
    }
}