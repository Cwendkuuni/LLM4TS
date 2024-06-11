import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parent = new ClassLoader() {};
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(xbusClassLoader);
    }

    @Test
    public void testClassLoaderCreation() {
        ClassLoader parent = new ClassLoader() {};
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(xbusClassLoader);
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = "example/dir";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(0, urls.size());

        dirName = "example/lib";
        File file1 = new File(dirName + "/file1.jar");
        File file2 = new File(dirName + "/file2.jar");
        File file3 = new File(dirName + "/file3.zip");

        File[] jars = {file1, file2, file3};
        file1.mkdir(); file2.mkdir(); file3.mkdir();
        urls.clear();

        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(3, urls.size());
        assertTrue(urls.contains(file1.toURI().toURL()));
        assertTrue(urls.contains(file2.toURI().toURL()));
        assertTrue(urls.contains(file3.toURI().toURL()));
    }
}