import static org.junit.Assert.*;
import org.junit.Test;

public class XBUSClassLoaderTest {

  @Test
  public void testGetInstance() {
    ClassLoader parent = getClass().getClassLoader();
    XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
    assertNotNull(instance);
  }

  @Test
  public void testCreateClassLoader() {
    ClassLoader parent = getClass().getClassLoader();
    XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
    assertNotNull(classLoader);
  }

  @Test
  public void testAddUrls() {
    Vector urls = new Vector();
    String dirName = "testDirectory";
    XBUSClassLoader.addUrls(urls, dirName);
    assertFalse(urls.isEmpty());
  }
}