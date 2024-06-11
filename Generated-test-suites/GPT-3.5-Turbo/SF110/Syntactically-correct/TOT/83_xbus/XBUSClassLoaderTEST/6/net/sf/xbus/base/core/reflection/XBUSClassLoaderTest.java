import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        // Typical Use Case
        ClassLoader parent = Mockito.mock(ClassLoader.class);
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
        assertNotNull(instance);
        assertEquals(parent, instance.getParent());

        // Edge Case
        XBUSClassLoader instance2 = XBUSClassLoader.getInstance(null);
        assertNotNull(instance2);
        assertEquals(ClassLoader.getSystemClassLoader(), instance2.getParent());
    }

    @Test
    public void testCreateClassLoader() {
        // Error Scenario
        ClassLoader parent = Mockito.mock(ClassLoader.class);
        XBUSClassLoader instance = Mockito.spy(XBUSClassLoader.class);

        Mockito.when(instance.getParent()).thenReturn(parent);
        try {
            XBUSClassLoader.createClassLoader(parent);
            fail("Expected Error: XBUS_HOME has not been set!");
        } catch (Exception e) {
            assertEquals("XBUS_HOME has not been set!", e.getMessage());
        }
    }

    @Test
    public void testAddUrls() {
        // Typical Use Case
        Vector urls = new Vector();
        String dirName = "/path/to/lib";
        XBUSClassLoader.addUrls(urls, dirName);
        assertFalse(urls.isEmpty());
        assertEquals(1, urls.size());

        // Edge Case
        Vector urls2 = new Vector();
        String emptyDir = "/path/to/empty/dir";
        XBUSClassLoader.addUrls(urls2, emptyDir);
        assertTrue(urls2.isEmpty());
        assertEquals(0, urls2.size());

        // Error Scenario
        Vector urls3 = new Vector();
        String invalidDir = "/path/to/invalid/dir";
        try {
            XBUSClassLoader.addUrls(urls3, invalidDir);
            fail("Expected Error: Directory does not exist");
        } catch (Exception e) {
            assertEquals("Directory does not exist", e.getMessage());
        }
    }
}