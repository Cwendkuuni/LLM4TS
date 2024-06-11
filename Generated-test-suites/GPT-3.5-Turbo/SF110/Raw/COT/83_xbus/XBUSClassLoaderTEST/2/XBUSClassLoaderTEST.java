import static org.junit.Assert.*;
import org.junit.Test;

public class XBUSClassLoaderTest {

  // Test getInstance() method
  @Test
  public void testGetInstance() {
    ClassLoader parent = getClass().getClassLoader();
    XBUSClassLoader loader = XBUSClassLoader.getInstance(parent);
    assertNotNull(loader);
  }

  // Test createClassLoader() method
  @Test
  public void testCreateClassLoader() {
    ClassLoader parent = getClass().getClassLoader();
    XBUSClassLoader loader = XBUSClassLoader.createClassLoader(parent);
    assertNotNull(loader);
  }

  // Test addUrls() method with valid inputs
  @Test
  public void testAddUrls() {
    Vector urls = new Vector();
    String dirName = "/path/to/dir";
    XBUSClassLoader.addUrls(urls, dirName);
    assertEquals(0, urls.size());
  }

  // Test addUrls() method with invalid inputs
  @Test
  public void testAddUrlsInvalidInputs() {
    Vector urls = new Vector();
    String dirName = "/path/to/nonexistent/dir";
    XBUSClassLoader.addUrls(urls, dirName);
    assertEquals(0, urls.size());
  }
}