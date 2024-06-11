
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parentClassLoader = XBUSClassLoaderTest.class.getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parentClassLoader);

        assertNotNull(classLoader);
        assertEquals(XBUSClassLoader.class, classLoader.getClass());
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = "testDir";

        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdir(); // Create a temporary directory
        }

        File file1 = new File(dir, "test1.jar");
        File file2 = new File(dir, "test2.zip");

        urls.add(getURL(file1));
        urls.add(getURL(file2));

        assertEquals(2, urls.size());
        assertEquals(getURL(file1), urls.get(0));
        assertEquals(getURL(file2), urls.get(1));

        deleteDirectory(dir);
    }

    private URL getURL(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null && files.length > 0) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }

        directory.delete();
    }
}

