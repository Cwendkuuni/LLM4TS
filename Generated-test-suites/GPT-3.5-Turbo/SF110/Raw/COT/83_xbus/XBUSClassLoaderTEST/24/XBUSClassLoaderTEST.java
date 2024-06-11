import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class XBUSClassLoaderTest {

    private XBUSClassLoader classLoader;
    
    @Before
    public void setUp() {
        classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
    }
    
    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = mock(ClassLoader.class);
        XBUSClassLoader result = XBUSClassLoader.createClassLoader(parent);
        assertEquals(parent, result.getParent());
    }
    
    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<URL>();
        String dirName = "test/dir";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(0, urls.size());
    }
    
}