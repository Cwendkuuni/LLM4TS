
import org.junit.Assert;
import org.junit.Test;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        XBUSClassLoader instance = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
        Assert.assertNotNull(instance);
    }

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(ClassLoader.getSystemClassLoader());
        Assert.assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "dirName");
        Assert.assertEquals(0, urls.size());
    }
}
