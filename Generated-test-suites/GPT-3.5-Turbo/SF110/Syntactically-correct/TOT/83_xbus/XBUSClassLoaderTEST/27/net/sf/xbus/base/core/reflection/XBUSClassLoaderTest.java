import org.junit.*;
import static org.junit.Assert.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;

public class XBUSClassLoaderTest {
    
    private ClassLoader parentClassLoader;
    private XBUSClassLoader classLoader;
    
    @Before
    public void setUp() {
        parentClassLoader = getClass().getClassLoader();
        classLoader = XBUSClassLoader.getInstance(parentClassLoader);
    }
    
    @Test
    public void testGetInstance() {
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parentClassLoader);
        assertSame("getInstance() should return the same instance", classLoader, instance);
    }
    
    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader newClassLoader = XBUSClassLoader.createClassLoader(parentClassLoader);
        assertNotSame("createClassLoader() should return a new instance", classLoader, newClassLoader);
    }
    
    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = Constants.XBUS_HOME + "/lib";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals("addUrls() should add all jar and zip files from the specified directory to the URL list", 2, urls.size());
    }
    
    @Test
    public void testAddUrlsInvalidDirectory() {
        Vector<URL> urls = new Vector<>();
        String dirName = Constants.XBUS_HOME + "/invalidDir";
        XBUSClassLoader.addUrls(urls, dirName);
        assertTrue("addUrls() should not add any URLs if the specified directory does not exist", urls.isEmpty());
    }
    
    @Test
    public void testAddUrlsEmptyDirectory() {
        Vector<URL> urls = new Vector<>();
        String dirName = Constants.XBUS_HOME + "/emptyDir";
        XBUSClassLoader.addUrls(urls, dirName);
        assertTrue("addUrls() should not add any URLs if the specified directory is empty", urls.isEmpty());
    }
    
    @Test(expected = MalformedURLException.class)
    public void testAddUrlsMalformedURL() {
        Vector<URL> urls = new Vector<>();
        String dirName = Constants.XBUS_HOME + "/lib";
        File malformedFile = new File(dirName + "/malformedfile.txt");
        try {
            malformedFile.createNewFile();
        } catch (Exception e) {
            fail("Failed to create a malformed file for testing");
        }
        XBUSClassLoader.addUrls(urls, dirName);
    }
    
    @Test
    public void testConstructor() {
        URL[] urlArray = {new URL("http://www.example.com")};
        XBUSClassLoader newClassLoader = new XBUSClassLoader(urlArray, parentClassLoader);
        assertNotNull("Constructor should create a new instance of XBUSClassLoader", newClassLoader);
    }
}