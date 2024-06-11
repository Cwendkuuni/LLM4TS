import static org.junit.Assert.*;
import org.junit.Test;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        // Test typical usage
        XBUSClassLoader instance = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(instance);
        
        // Test edge case of passing null as parent
        instance = XBUSClassLoader.getInstance(null);
        assertNotNull(instance);
    }

    @Test
    public void testCreateClassLoader() {
        // Test creation of class loader when XBUS_HOME is set
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNotNull(classLoader);
        
        // Test creation of class loader when XBUS_HOME is null
        // Error scenario to be tested (Trace.error and System.exit should be called)
    }

    @Test
    public void testAddUrls() {
        // Test adding URLs when directory contains only jar files
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "test/resources/jars");
        assertEquals(2, urls.size());
        
        // Test adding URLs when directory contains zip files
        urls.clear();
        XBUSClassLoader.addUrls(urls, "test/resources/zips");
        assertEquals(1, urls.size());
        
        // Test adding URLs when directory contains non-jar or non-zip files
        urls.clear();
        XBUSClassLoader.addUrls(urls, "test/resources/other");
        assertEquals(0, urls.size());
        
        // Test edge case of MalformedURLException during adding URL
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));
        urls.clear();
        XBUSClassLoader.addUrls(urls, "test/resources/invalid");
        assertEquals("java.net.MalformedURLException", outContent.toString().trim());
    }

}