import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;

public class XBUSClassLoaderTest {
    
    private XBUSClassLoader testClassLoader;
    private ClassLoader parentClassLoader;
    
    @Before
    public void setUp() {
        parentClassLoader = getClass().getClassLoader();
        testClassLoader = XBUSClassLoader.getInstance(parentClassLoader);
    }
    
    @Test
    public void testCreateClassLoader() {
        assertNotNull(testClassLoader);
    }
    
    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = "test";
        File libPath = new File(dirName);
        urls.add(libPath.toURI().toURL());
        testClassLoader.addUrls(urls, dirName);
        assertEquals(1, urls.size());
    }
    
    @Test
    public void testAddUrlsWithInvalidPath() {
        Vector<URL> urls = new Vector<>();
        String invalidDirName = "invalid";
        testClassLoader.addUrls(urls, invalidDirName);
        assertEquals(0, urls.size());
    }
    
    @Test
    public void testCreateClassLoaderWithoutSettingXBUS_HOME() {
        Constants.XBUS_HOME = null;
        XBUSClassLoader.mClassLoader = null;
        XBUSClassLoader.getInstance(parentClassLoader);
        assertNull(XBUSClassLoader.mClassLoader);
    }
    
    @Test(expected = MalformedURLException.class)
    public void testAddUrlsWithMalformedURL() throws MalformedURLException {
        Vector<URL> urls = new Vector<>();
        String dirName = "test";
        File libPath = new File(dirName);
        urls.add(libPath.toURI().toURL());
        // Add an invalid URL to force MalformedURLException
        urls.add(new URL("http://invalidurl"));
        testClassLoader.addUrls(urls, dirName);
    }
}