import static org.junit.Assert.*;
import org.junit.Test;
import java.net.*;
import java.util.*;
import java.io.*;

public class XBUSClassLoaderTest {

  // Test case for getInstance method
  @Test
  public void testGetInstance() {
    ClassLoader parent = Thread.currentThread().getContextClassLoader();
    XBUSClassLoader xbusClassLoader = XBUSClassLoader.getInstance(parent);
    assertNotNull("XBUSClassLoader instance should not be null", xbusClassLoader);
    assertSame("The parent class loader should be the one passed as argument", parent, xbusClassLoader.getParent());
  }

  // Test case for createClassLoader method when Constants.XBUS_HOME is not set
  @Test
  public void testCreateClassLoaderWithoutXBusHome() {
    ClassLoader parent = Thread.currentThread().getContextClassLoader();
    try {
        XBUSClassLoader.createClassLoader(parent);
        fail("System should exit with code 1 when Constants.XBUS_HOME is not set");
    } catch (SecurityException e) {
      // Expecting SecurityException to be thrown as System.exit() is called
    }
  }

  // Test case for addUrls method with empty directory
  @Test
  public void testAddUrlsWithEmptyDirectory() {
    Vector<URL> urls = new Vector<URL>();
    String dirName = "emptyDirectory";
    XBUSClassLoader.addUrls(urls, dirName);
    assertTrue("URLs vector should be empty", urls.isEmpty());
  }

  // Test case for addUrls method with directory containing non-jar/zip files
  @Test
  public void testAddUrlsWithNonJarZipFiles() {
    Vector<URL> urls = new Vector<URL>();
    String dirName = "nonJarZipFilesDirectory";
    XBUSClassLoader.addUrls(urls, dirName);
    assertTrue("URLs vector should be empty", urls.isEmpty());
  }

  // Test case for addUrls method with valid jar files
  @Test
  public void testAddUrlsWithValidJarFiles() {
    Vector<URL> urls = new Vector<URL>();
    String dirName = "validJarFilesDirectory";
    XBUSClassLoader.addUrls(urls, dirName);
    assertFalse("URLs vector should not be empty", urls.isEmpty());
    assertEquals("URLs vector should contain the number of valid jar files in the directory", countValidJarFiles(dirName), urls.size());
  }

  // Helper method to count the number of valid jar files in a directory
  private int countValidJarFiles(String dirName) {
    File dir = new File(dirName);
    File[] files = dir.listFiles();
    int count = 0;
    if (files != null) {
      for (File file : files) {
        if (file.isFile()) {
          if (file.getName().endsWith("jar") || file.getName().endsWith("zip")) {
            count++;
          }
        }
      }
    }
    return count;
  }

}