import static org.junit.Assert.*;
import org.junit.Test;
import java.net.URLClassLoader;
import java.net.URL;
import java.io.File;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parentClassLoader = new URLClassLoader(new URL[0]);
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parentClassLoader);
        assertNotNull(instance);
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parentClassLoader = new URLClassLoader(new URL[0]);
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parentClassLoader);
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<URL>();
        String dirName = "testDir";
        File libPath = new File(dirName);
        libPath.mkdirs(); // Create test directory

        // Create test jars
        File jarFile = new File(dirName + "/test.jar");
        try {
            jarFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File zipFile = new File(dirName + "/test.zip");
        try {
            zipFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        XBUSClassLoader.addUrls(urls, dirName);

        assertTrue(urls.contains(jarFile.toURL()));
        assertTrue(urls.contains(zipFile.toURL()));

        // Clean up test files
        jarFile.delete();
        zipFile.delete();
        libPath.delete();
    }
}