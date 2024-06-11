import org.junit.Test;
import static org.junit.Assert.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {
  
  // Test case for getInstance
  @Test
  public void testGetInstance() {
    XBUSClassLoader instance1 = XBUSClassLoader.getInstance(null);
    XBUSClassLoader instance2 = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
    
    assertNotNull("getInstance should return a non-null instance", instance1);
    assertSame("getInstance should return the same instance for the same parent class loader", instance1, instance2);
  }
  
  // Test case for createClassLoader
  @Test
  public void testCreateClassLoader() {
    XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(ClassLoader.getSystemClassLoader());
    
    assertNotNull("createClassLoader should return a non-null instance", classLoader);
  }
  
  // Test cases for addUrls
  @Test
  public void testAddUrls() {
    Vector<URL> urls = new Vector<URL>();
    
    // Test case for empty directory
    XBUSClassLoader.addUrls(urls, "empty_directory");
    assertEquals("addUrls should not add any URLs for an empty directory", 0, urls.size());
    
    // Test case for directory with non-jar/non-zip files
    XBUSClassLoader.addUrls(urls, "non_jar_zip_directory");
    assertEquals("addUrls should not add any URLs for a directory without jar/zip files", 0, urls.size());
    
    // Test case for directory with jar files
    XBUSClassLoader.addUrls(urls, "jar_directory");
    assertEquals("addUrls should add URLs for jar files in the directory", 2, urls.size());
    
    // Test case for directory with zip files
    XBUSClassLoader.addUrls(urls, "zip_directory");
    assertEquals("addUrls should add URLs for zip files in the directory", 3, urls.size());
    
    // Test case for directory with both jar and zip files
    XBUSClassLoader.addUrls(urls, "mixed_directory");
    assertEquals("addUrls should add URLs for both jar and zip files in the directory", 7, urls.size());
    
    // Test case for directory that does not exist
    try {
      XBUSClassLoader.addUrls(urls, "non_existent_directory");
      fail("addUrls should throw MalformedURLException for a non-existent directory");
    } catch (MalformedURLException e) {
      // Exception expected
    }
  }
  
  // Test cases for private helper methods
  
  // Test case for private constructor
  @Test
  public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<XBUSClassLoader> constructor = XBUSClassLoader.class.getDeclaredConstructor(URL[].class, ClassLoader.class);
    assertTrue("private constructor should be inaccessible", Modifier.isPrivate(constructor.getModifiers()));

    constructor.setAccessible(true);
    constructor.newInstance(new URL[0], null);
  }
  
  // Test case for private static synchronized method
  @Test
  public void testPrivateStaticSynchronizedMethod() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    Method method = XBUSClassLoader.class.getDeclaredMethod("addClass", String.class);
    assertTrue("private static synchronized method should be synchronized", Modifier.isSynchronized(method.getModifiers()));
    
    method.setAccessible(true);
    method.invoke(null, "test");
  }
  
}