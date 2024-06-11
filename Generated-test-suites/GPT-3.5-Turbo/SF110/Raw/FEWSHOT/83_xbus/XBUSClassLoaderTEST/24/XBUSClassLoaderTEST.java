package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class XBUSClassLoaderTest {
    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "testDir");

        assertEquals(0, urls.size()); // Verify that no URLs were added to the vector

        File testDir = new File("testDir");

        // Add a jar file to the testDir
        File jarFile = new File(testDir, "test.jar");
        createFile(jarFile, true);
        XBUSClassLoader.addUrls(urls, "testDir");
        assertEquals(1, urls.size()); // Verify that one URL was added to the vector
        assertTrue(urls.contains(jarFile.toURI().toURL()));

        // Add a non-jar file to the testDir
        File nonJarFile = new File(testDir, "test.txt");
        createFile(nonJarFile, false);
        XBUSClassLoader.addUrls(urls, "testDir");
        assertEquals(1, urls.size()); // Verify that no additional URLs were added to the vector

        // Add a zip file to the testDir
        File zipFile = new File(testDir, "test.zip");
        createFile(zipFile, true);
        XBUSClassLoader.addUrls(urls, "testDir");
        assertEquals(2, urls.size()); // Verify that one additional URL was added to the vector
        assertTrue(urls.contains(jarFile.toURI().toURL()));
        assertTrue(urls.contains(zipFile.toURI().toURL()));
    }

    private void createFile(File file, boolean create) {
        try {
            if (create) {
                file.createNewFile();
            } else {
                file.mkdir();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}