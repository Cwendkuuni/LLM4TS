import static org.junit.Assert.*;
import org.junit.Test;

public class XBUSClassLoaderTest {

   @Test
   public void testGetInstance() {
      // Create a parent class loader
      ClassLoader parent = new ClassLoader() {};
      
      // Get the instance of XBUSClassLoader with the parent class loader
      XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
      
      // Assert that the returned class loader is not null
      assertNotNull(classLoader);
   }
   
   @Test
   public void testCreateClassLoader() {
      // Create a parent class loader
      ClassLoader parent = new ClassLoader() {};
      
      // Create a new XBUSClassLoader
      XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
      
      // Assert that the created class loader is not null
      assertNotNull(classLoader);

      // Assert that the created class loader is an instance of XBUSClassLoader
      assertTrue(classLoader instanceof XBUSClassLoader);
   }
   
   @Test
   public void testAddUrls() {
      // Create a vector of URLs
      Vector<URL> urls = new Vector<>();
      
      // Add URLs using addUrls method
      XBUSClassLoader.addUrls(urls, "path/to/directory");
      
      // Assert that the vector is not empty
      assertFalse(urls.isEmpty());
   }
}