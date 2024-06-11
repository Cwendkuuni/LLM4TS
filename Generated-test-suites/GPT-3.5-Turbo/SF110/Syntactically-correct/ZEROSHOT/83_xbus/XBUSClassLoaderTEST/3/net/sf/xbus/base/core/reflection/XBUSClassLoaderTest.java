import org.junit.Assert;
import org.junit.Test;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        
        Assert.assertNotNull(classLoader);
        Assert.assertEquals(parent, classLoader.getParent());
    }
    
    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        
        Assert.assertNotNull(classLoader);
        Assert.assertEquals(parent, classLoader.getParent());
    }
    
    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = "example/dir";
        
        XBUSClassLoader.addUrls(urls, dirName);
        
        Assert.assertEquals(0, urls.size());
    }
}