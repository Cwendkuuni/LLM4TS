
import static org.junit.Assert.*;
import org.junit.Test;
import java.net.URL;
import java.net.URLClassLoader;

public class XBUSClassLoaderTest {

  private final ClassLoader parent = ClassLoader.getSystemClassLoader();

  @Test
  public void testGetInstance() {
    assertNotNull(XBUSClassLoader.getInstance(parent));
  }

  @Test
  public void testCreateClassLoader() {
    assertNotNull(XBUSClassLoader.createClassLoader(parent));
  }

  @Test
  public void testAddUrls() {
    XBUSClassLoader.addUrls(new Vector<>(), "dirName");
    // Check if urls have been added correctly
    // Add assertion here
  }
}
