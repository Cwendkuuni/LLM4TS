
import static org.junit.Assert.*;
import org.junit.Test;

public class XBUSClassLoaderTest {

  @Test
  public void testGetInstance() {
    // Test when parent class loader is null
    ClassLoader parent = null;
    XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
    assertNotNull(instance);
  }

  @Test
  public void testAddUrls() {
    // Test when directory name is valid
    Vector urls = new Vector();
    String dirName = "/path/to/directory";
    XBUSClassLoader.addUrls(urls, dirName);
    assertFalse(urls.isEmpty());

    // Test when directory name is invalid, empty, or non-existent
    urls.clear();
    dirName = "/path/to/nonexistent/directory";
    XBUSClassLoader.addUrls(urls, dirName);
    assertTrue(urls.isEmpty());
  }
}