import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

@RunWith(PowerMockRunner.class)
public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        // Typical use case
        XBUSClassLoader instance = XBUSClassLoader.getInstance(getClass().getClassLoader());
        Assert.assertNotNull(instance);
        
        // Edge case
        instance = XBUSClassLoader.getInstance(null);
        Assert.assertNotNull(instance);
        
        // Error scenario
        // In this case, Constants.XBUS_HOME is null, so XBUSClassLoader.getInstance() should exit with status code 1
        // We cannot test the System.exit() call directly, but we can assert that the program terminates by trying to catch the SecurityException thrown in cases of exit() call
        try {
            SecurityManager sm = new SecurityManager() {
                @Override
                public void checkPermission(java.security.Permission perm) {
                    if (perm.getName().startsWith("exitVM")) {
                        throw new SecurityException();
                    }
                }
            };
            System.setSecurityManager(sm);
            instance = XBUSClassLoader.getInstance(getClass().getClassLoader());
            Assert.fail("Expected SecurityException not thrown");
        } catch (SecurityException e) {
            // Expected
        } finally {
            System.setSecurityManager(null);
        }
    }
    
    @Test
    public void testCreateClassLoader() {
        // Typical use case
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        Assert.assertNotNull(classLoader);
        
        // Edge case
        classLoader = XBUSClassLoader.createClassLoader(null);
        Assert.assertNotNull(classLoader);
        
        // Error scenario
        // In this case, Constants.XBUS_HOME is not set, so Trace.error() should be called and the program should exit with status code 1
        // We cannot test the Trace.error() call directly, but we can assert that the program terminates by trying to catch the SecurityException thrown in cases of exit() call
        try {
            SecurityManager sm = new SecurityManager() {
                @Override
                public void checkPermission(java.security.Permission perm) {
                    if (perm.getName().startsWith("exitVM")) {
                        throw new SecurityException();
                    }
                }
            };
            System.setSecurityManager(sm);
            classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
            Assert.fail("Expected SecurityException not thrown");
        } catch (SecurityException e) {
            // Expected
        } finally {
            System.setSecurityManager(null);
        }
    }
    
    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        
        // Typical use case
        XBUSClassLoader.addUrls(urls, "path/to/dir");
        Assert.assertEquals(3, urls.size());
        
        // Edge case
        urls.clear();
        XBUSClassLoader.addUrls(urls, "path/to/emptyDir");
        Assert.assertEquals(0, urls.size());
        
        // Error scenario
        // In this case, the input directory does not exist, so addUrls() should not throw an exception, but it should not add any URLs either
        urls.clear();
        XBUSClassLoader.addUrls(urls, "path/to/nonExistingDir");
        Assert.assertEquals(0, urls.size());
        
        // Error scenario
        // In this case, the input directory contains a file with an invalid extension, so addUrls() should not throw an exception, but it should not add the URL of the file
        urls.clear();
        XBUSClassLoader.addUrls(urls, "path/to/dir");
        Assert.assertEquals(2, urls.size());
    }
}