import static org.junit.Assert.*;
import org.junit.Test;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        // Test with parent class loader
        ClassLoader parent = new ClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);
        assertEquals(parent, classLoader.getParent());

        // Test without parent class loader
        classLoader = XBUSClassLoader.getInstance(null);
        assertNotNull(classLoader);
        assertNull(classLoader.getParent());
    }

    @Test
    public void testCreateClassLoader() {
        // Test with parent class loader
        ClassLoader parent = new ClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
        assertEquals(parent, classLoader.getParent());

        // Test without parent class loader
        classLoader = XBUSClassLoader.createClassLoader(null);
        assertNotNull(classLoader);
        assertNull(classLoader.getParent());
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String dirName = "test_dir";
        
        // Test when directory is empty
        XBUSClassLoader.addUrls(urls, dirName);
        assertTrue(urls.isEmpty());

        // Test when directory contains files
        File file1 = new File(dirName + "/file1.jar");
        File file2 = new File(dirName + "/file2.zip");
        File file3 = new File(dirName + "/file3.txt");
        File[] files = { file1, file2, file3 };
        File libPath = mock(File.class);
        when(libPath.listFiles()).thenReturn(files);
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(2, urls.size());
        assertTrue(urls.contains(file1.toURI().toURL()));
        assertTrue(urls.contains(file2.toURI().toURL()));

        // Test when directory contains only non-jar files
        urls.clear();
        File file4 = new File(dirName + "/file4.txt");
        File file5 = new File(dirName + "/file5.txt");
        File file6 = new File(dirName + "/file6.txt");
        File[] nonJarFiles = { file4, file5, file6 };
        when(libPath.listFiles()).thenReturn(nonJarFiles);
        XBUSClassLoader.addUrls(urls, dirName);
        assertTrue(urls.isEmpty());
    }
}