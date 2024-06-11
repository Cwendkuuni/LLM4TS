package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class XBUSClassLoaderTest {

    private static final String XBUS_HOME = "path/to/XBUS_HOME";
    private static final String LIB_DIR = XBUS_HOME + "/lib";
    private static final String RUNTIME_DIR = XBUS_HOME + "/lib/runtime";
    private static final String PLUGIN_LIB_DIR = XBUS_HOME + "/plugin/lib";
    private static final String TEST_LIB_DIR = XBUS_HOME + "/test/lib";

    private XBUSClassLoader classLoader;

    @Before
    public void setup() {
        classLoader = XBUSClassLoader.getInstance(this.getClass().getClassLoader());
    }

    @Test
    public void testCreateClassLoader_whenXBUS_HOMEIsNull_shouldExitWithErrorAndReturnNull() {
        // Arrange
        System.setProperty("XBUS_HOME", null);
        String expectedErrorMessage = "XBUS_HOME has not been set!";

        // Act
        Exception exception = null;
        try {
            classLoader = XBUSClassLoader.createClassLoader(this.getClass().getClassLoader());
        } catch (SecurityException e) {
            exception = e;
        }

        // Assert
        assertNotNull(exception);
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    public void testCreateClassLoader_whenXBUS_HOMEIsNotNull_shouldCreateClassLoaderWithUrls() {
        // Arrange
        System.setProperty("XBUS_HOME", XBUS_HOME);
        Vector<URL> expectedUrls = new Vector<>();
        addUrls(expectedUrls, LIB_DIR);
        addUrls(expectedUrls, RUNTIME_DIR);
        addUrls(expectedUrls, PLUGIN_LIB_DIR);
        addUrls(expectedUrls, TEST_LIB_DIR);
        URL[] expectedUrlArray = new URL[expectedUrls.size()];
        expectedUrls.toArray(expectedUrlArray);

        // Act
        classLoader = XBUSClassLoader.createClassLoader(this.getClass().getClassLoader());

        // Assert
        assertEquals(expectedUrlArray.length, classLoader.getURLs().length);
        for (int i = 0; i < expectedUrlArray.length; i++) {
            assertEquals(expectedUrlArray[i], classLoader.getURLs()[i]);
        }
    }

    @Test
    public void testAddUrls_whenDirectoryDoesNotExist_shouldNotAddUrls() {
        // Arrange
        Vector<URL> urls = new Vector<>();
        String invalidDirName = "invalid/dir";

        // Act
        XBUSClassLoader.addUrls(urls, invalidDirName);

        // Assert
        assertEquals(0, urls.size());
    }

    @Test
    public void testAddUrls_whenDirectoryExists_shouldAddUrlsOfJarAndZipFiles() {
        // Arrange
        Vector<URL> urls = new Vector<>();
        String validDirName = LIB_DIR;
        File libPath = new File(validDirName);
        File[] jars = libPath.listFiles();
        int expectedUrlsCount = 0;

        // Act
        if (jars != null) {
            for (File jar : jars) {
                if (jar.isFile() && (jar.getName().endsWith("jar") || jar.getName().endsWith("zip"))) {
                    try {
                        urls.add(jar.toURL());
                        expectedUrlsCount++;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Assert
        assertEquals(expectedUrlsCount, urls.size());
    }

    private void addUrls(Vector<URL> urls, String dirName) {
        File libPath = new File(dirName);
        File[] jars = libPath.listFiles();
        for (int i = 0; jars != null && i < jars.length; i++) {
            if (jars[i].isFile() && (jars[i].getName().endsWith("jar") || jars[i].getName().endsWith("zip"))) {
                try {
                    urls.add(jars[i].toURL());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}