package net.sf.xbus.base.core.reflection;

import org.junit.Test;
import org.junit.Assert;
import java.net.URL;
import java.net.MalformedURLException;

public class XBUSClassLoaderTest {

  @Test
  public void testGetInstance() {
    ClassLoader parent = XBUSClassLoaderTest.class.getClassLoader();
    XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
    Assert.assertNotNull("XBUSClassLoader instance should not be null", instance);
  }

  @Test
  public void testAddUrls() {
    Vector urls = new Vector();
    String dirName = "/path/to/directory";
    XBUSClassLoader.addUrls(urls, dirName);
    Assert.assertEquals("Number of URLs should be equal to the number of files in the directory", urls.size(), new File(dirName).listFiles().length);
  }

  @Test
  public void testCreateClassLoader() {
    ClassLoader parent = XBUSClassLoaderTest.class.getClassLoader();
    XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
    Assert.assertNotNull("XBUSClassLoader instance should not be null", classLoader);
    Assert.assertEquals("Parent class loader should be equal to the provided parent", parent, classLoader.getParent());
  }

  @Test
  public void testXBUSClassLoader() {
    URL[] urlArray = new URL[0];
    ClassLoader parent = XBUSClassLoaderTest.class.getClassLoader();
    XBUSClassLoader classLoader = new XBUSClassLoader(urlArray, parent);
    Assert.assertNotNull("XBUSClassLoader instance should not be null", classLoader);
    Assert.assertEquals("URL array should be equal to the provided urlArray", urlArray, classLoader.getURLs());
    Assert.assertEquals("Parent class loader should be equal to the provided parent", parent, classLoader.getParent());
  }
}