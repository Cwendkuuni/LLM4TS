package net.sf.xbus.base.core.reflection;

import org.junit.Assert;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;

public class XBUSClassLoaderTest {
    @Test
    public void testGetInstance() {
        ClassLoader parent = null;
        XBUSClassLoader instance1 = XBUSClassLoader.getInstance(parent);
        Assert.assertNotNull(instance1);
        
        XBUSClassLoader instance2 = XBUSClassLoader.getInstance(parent);
        Assert.assertEquals(instance1, instance2);
    }
    
    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = null;
        
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        Assert.assertNotNull(classLoader);
        
        // Check if Constants.XBUS_HOME is null
        try {
            Constants.XBUS_HOME = null;
            classLoader = XBUSClassLoader.createClassLoader(parent);
            Assert.fail("Expected System.exit(1) to be called");
        } catch (SecurityException e) {
            Assert.assertEquals("XBUS_HOME has not been set!", e.getMessage());
        } catch (Exception e) {
            Assert.fail("Expected SecurityException");
        } finally {
            Constants.XBUS_HOME = "path/to/xbus";
        }
        
        // Check adding URLs
        Vector urls = new Vector();
        String dirName = "path/to/xbus/lib";
        XBUSClassLoader.addUrls(urls, dirName);
        Assert.assertEquals(0, urls.size());
        
        dirName = "path/to/xbus/lib";
        File libPath = new File(dirName);
        File file1 = new File("path/to/xbus/lib/file1.jar");
        File file2 = new File("path/to/xbus/lib/file2.zip");
        File[] jars = {file1, file2};
        Mockito.when(libPath.listFiles()).thenReturn(jars);
        XBUSClassLoader.addUrls(urls, dirName);
        Assert.assertEquals(2, urls.size());
        
        try {
            File file3 = new File("path/to/xbus/lib/file3");
            File[] invalidJars = {file3};
            Mockito.when(libPath.listFiles()).thenReturn(invalidJars);
            XBUSClassLoader.addUrls(urls, dirName);
            Assert.fail("Expected MalformedURLException to be thrown");
        } catch (MalformedURLException e) {
            // Expected
        } catch (Exception e) {
            Assert.fail("Expected MalformedURLException");
        }
        
        // Check URL array creation
        Constants.XBUS_HOME = "path/to/xbus";
        dirName = "path/to/xbus/lib";
        File[] jars2 = {file1};
        Mockito.when(libPath.listFiles()).thenReturn(jars2);
        URL[] urlArray1 = XBUSClassLoader.createURLArray(dirName);
        Assert.assertEquals(1, urlArray1.length);
        Assert.assertEquals(file1.toURI().toURL(), urlArray1[0]);
        
        URL[] urlArray2 = XBUSClassLoader.createURLArray(null);
        Assert.assertEquals(0, urlArray2.length);
        
        URL[] urlArray3 = XBUSClassLoader.createURLArray("");
        Assert.assertEquals(0, urlArray3.length);
    }
}