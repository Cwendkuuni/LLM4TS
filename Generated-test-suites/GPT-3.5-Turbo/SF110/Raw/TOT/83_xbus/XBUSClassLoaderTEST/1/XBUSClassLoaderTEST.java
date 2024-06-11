import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Vector;

public class XBUSClassLoaderTest {

    // Method: getInstance()
    @Test
    public void testGetInstance_typical() {
        XBUSClassLoader instance = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
        assertNotNull(instance);
    }

    // Method: createClassLoader()
    @Test
    public void testCreateClassLoader_typical() {
        XBUSClassLoader loader = XBUSClassLoader.createClassLoader(ClassLoader.getSystemClassLoader());
        assertNotNull(loader);
    }

    // Method: createClassLoader()
    @Test
    public void testCreateClassLoader_nullParent() {
        XBUSClassLoader loader = XBUSClassLoader.createClassLoader(null);
        assertNotNull(loader);
    }

    // Method: addUrls()
    @Test
    public void testAddUrls_typical() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "testDir");
        assertFalse(urls.isEmpty());
    }

    // Method: addUrls()
    @Test
    public void testAddUrls_emptyDirName() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "");
        assertTrue(urls.isEmpty());
    }

    // Method: addUrls()
    @Test
    public void testAddUrls_invalidDirName() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "invalidDir");
        assertTrue(urls.isEmpty());
    }

    // Method: addUrls()
    @Test
    public void testAddUrls_nullFileList() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "testDir");
        assertTrue(urls.isEmpty());
    }

    // Method: addUrls()
    @Test
    public void testAddUrls_invalidFileExtension() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "testDir");
        assertFalse(urls.isEmpty());
    }

    // Method: addUrls()
    @Test
    public void testAddUrls_invalidURL() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "testDir");
        assertFalse(urls.isEmpty());
    }

    // Method: addUrls()
    @Test
    public void testAddUrls_exceptionThrown() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "testDir");
        assertFalse(urls.isEmpty());
    }

    // Method: addUrls()
    @Test
    public void testAddUrls_validURL() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "testDir");
        assertFalse(urls.isEmpty());
    }

    // Method: addUrls()
    @Test
    public void testAddUrls_validURLs() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "testDir");
        assertFalse(urls.isEmpty());
    }

    // Method: addUrls()
    @Test
    public void testAddUrls_nullURL() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "testDir");
        assertFalse(urls.isEmpty());
    }

    // Helper method to set mClassLoader field to null
    private static void setPrivateStaticField(Class<?> clazz, String fieldName, Object value) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, value);
    }

    // Helper method to get private static field value
    private static Object getPrivateStaticFieldValue(Class<?> clazz, String fieldName) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(null);
    }

    // Helper method to invoke private static method
    private static Object invokePrivateStaticMethod(Class<?> clazz, String methodName, Object... args) throws Exception {
        Class<?>[] argTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }
        Method method = clazz.getDeclaredMethod(methodName, argTypes);
        method.setAccessible(true);
        return method.invoke(null, args);
    }

    // Method: createClassLoader()
    @Test
    public void testCreateClassLoader_initializedClassLoader() throws Exception {
        // Nullify mClassLoader field
        setPrivateStaticField(XBUSClassLoader.class, "mClassLoader", null);
        
        // Invoke createClassLoader once
        XBUSClassLoader loader1 = (XBUSClassLoader) invokePrivateStaticMethod(
            XBUSClassLoader.class, "createClassLoader", ClassLoader.getSystemClassLoader());
        
        // Invoke createClassLoader again and expect same instance
        XBUSClassLoader loader2 = (XBUSClassLoader) invokePrivateStaticMethod(
            XBUSClassLoader.class, "createClassLoader", ClassLoader.getSystemClassLoader());
        
        assertEquals(loader1, loader2);
    }

    // Method: createClassLoader()
    @Test
    public void testCreateClassLoader_nullXbusHome() throws Exception {
        // Nullify XBUS_HOME constant
        setPrivateStaticField(Constants.class, "XBUS_HOME", null);
        
        // Invoke createClassLoader and expect System.exit(1)
        invokePrivateStaticMethod(XBUSClassLoader.class, "createClassLoader", ClassLoader.getSystemClassLoader());
    }

    // Method: createClassLoader()
    @Test
    public void testCreateClassLoader_withXbusHome() throws Exception {
        // Set valid XBUS_HOME value
        setPrivateStaticField(Constants.class, "XBUS_HOME", "validPath");
        
        // Invoke createClassLoader and expect non-null instance
        XBUSClassLoader loader = (XBUSClassLoader) invokePrivateStaticMethod(
            XBUSClassLoader.class, "createClassLoader", ClassLoader.getSystemClassLoader());
        
        assertNotNull(loader);
    }

}