package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {
    
    @Test
    public void testCreateClassLoader() {
        // Setting up Constants.XBUS_HOME
        Constants.XBUS_HOME = "path_to_XBUS_HOME_directory";
        
        // Test case 1: When Constants.XBUS_HOME is null
        Constants.XBUS_HOME = null;
        try {
            XBUSClassLoader.createClassLoader(this.getClass().getClassLoader());
            fail("Expected IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException e) {
            assertEquals("XBUS_HOME has not been set!", e.getMessage());
        }
        
        // Test case 2: When Constants.XBUS_HOME is not null
        Constants.XBUS_HOME = "path_to_XBUS_HOME_directory";
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(this.getClass().getClassLoader());
        
        // assertions
        assertNotNull("XBUSClassLoader instance should not be null", classLoader);
        assertTrue("XBUSClassLoader instance should be of type XBUSClassLoader", classLoader instanceof XBUSClassLoader);
    }
    
    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        
        // Setting up test data
        String dirName = "path_to_lib_directory";
        
        File file1 = new File("path_to_lib_directory/file1.jar");
        File file2 = new File("path_to_lib_directory/file2.zip");
        File file3 = new File("path_to_lib_directory/file3.txt");
        File[] jars = {file1, file2, file3};
        
        // Test case 1: When jars array is null
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals("Vector should be empty", 0, urls.size());
        
        // Test case 2: When jars array is not null
        urls.clear();
        
        // assertion
        assertNotNull("jars should not be null", jars);
        
        // adding each file URL to urls vector
        for (File file : jars) {
            if (file.isFile() && (file.getName().endsWith("jar") || file.getName().endsWith("zip"))) {
                try {
                    urls.add(file.toURL());
                } catch (MalformedURLException e) {
                    fail("Unexpected exception occurred: " + e.getMessage());
                }
            }
        }
        
        // assertions
        assertEquals("Vector size should be equal to the number of jar/zip files in the directory", 2, urls.size());
        assertTrue("Vector should contain file1.jar URL", urls.contains(file1.toURL()));
        assertTrue("Vector should contain file2.zip URL", urls.contains(file2.toURL()));
        assertFalse("Vector should not contain file3.txt URL", urls.contains(file3.toURL()));
    }
}