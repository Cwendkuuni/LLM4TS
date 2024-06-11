import org.junit.*;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        // Typical use case
        ClassLoader parent = ClassLoader.getSystemClassLoader();
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.getInstance(parent);
        Assert.assertNotNull(xbusClassLoader);
        
        // Edge case: null parent class loader
        parent = null;
        xbusClassLoader = XBUSClassLoader.getInstance(parent);
        Assert.assertNotNull(xbusClassLoader);
        
        // Error scenario: Constants.XBUS_HOME is null
        // This test case should terminate the program with status code 1
        // A workaround to test this scenario is by overriding System.exit() method
        parent = ClassLoader.getSystemClassLoader();
        System.setSecurityManager(new SecurityManager() {
            @Override
            public void checkExit(int status) {
                throw new SecurityException();
            }
        });
        try {
            xbusClassLoader = XBUSClassLoader.getInstance(parent);
            Assert.fail("Expected SecurityException");
        } catch (SecurityException e) {
            // Test passed
        }
        System.setSecurityManager(null); // Reset SecurityManager
    }
    
    @Test
    public void testCreateClassLoader() {
        // Typical use case
        ClassLoader parent = ClassLoader.getSystemClassLoader();
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.createClassLoader(parent);
        Assert.assertNotNull(xbusClassLoader);
        
        // Edge case: null parent class loader
        parent = null;
        xbusClassLoader = XBUSClassLoader.createClassLoader(parent);
        Assert.assertNotNull(xbusClassLoader);
    }
    
    @Test
    public void testAddUrls() {
        // Typical use case
        Vector<URL> urls = new Vector<>();
        String dirName = Constants.XBUS_HOME + "/lib";
        XBUSClassLoader.addUrls(urls, dirName);
        Assert.assertFalse(urls.isEmpty());
        Assert.assertTrue(urls.size() > 0);
        
        // Edge case: empty directory
        urls.clear();
        dirName = "";
        XBUSClassLoader.addUrls(urls, dirName);
        Assert.assertTrue(urls.isEmpty());
        
        // Error scenario: MalformedURLException
        // This test case should terminate the program with status code 1
        urls.clear();
        dirName = "invalidDirectory"; // Assuming this directory does not exist
        System.setSecurityManager(new SecurityManager() {
            @Override
            public void checkExit(int status) {
                throw new SecurityException();
            }
        });
        try {
            XBUSClassLoader.addUrls(urls, dirName);
            Assert.fail("Expected SecurityException");
        } catch (SecurityException e) {
            // Test passed
        }
        System.setSecurityManager(null); // Reset SecurityManager
    }
    
}