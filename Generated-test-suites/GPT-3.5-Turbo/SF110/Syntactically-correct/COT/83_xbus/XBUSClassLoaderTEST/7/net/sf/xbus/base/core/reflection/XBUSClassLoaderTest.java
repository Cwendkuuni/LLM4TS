
import org.junit.Assert;
import org.junit.Test;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
        
        Assert.assertNotNull(instance);
        Assert.assertEquals(parent, instance.getParent());
    }
    
    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        
        Assert.assertNotNull(classLoader);
        Assert.assertEquals(parent, classLoader.getParent());
    }
   
    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String dirName = Constants.XBUS_HOME + "/lib";
        XBUSClassLoader.addUrls(urls, dirName);
        
        // Assert the correct URLs have been added to the vector
        // based on the content of the directory
        // Add specific assertions for edge cases and exception handling
    }
}
