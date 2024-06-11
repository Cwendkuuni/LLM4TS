import static org.junit.Assert.*;
import org.junit.Test;
import java.net.URL;
import java.util.Vector;
import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;

public class XBUSClassLoaderTest {

    @Test
    public void getInstance_validParentClassLoader_returnsXBUSClassLoaderInstance() {
        ClassLoader parent = new ClassLoader();
        
        XBUSClassLoader loader = XBUSClassLoader.getInstance(parent);
        
        assertNotNull(loader);
        assertTrue(loader instanceof XBUSClassLoader);
    }
    
    @Test
    public void addUrls_directoryValid_returnsURLListWithJarAndZipFiles() {
        Vector<URL> urls = new Vector<>();
        String dirName = "/path/to/directory";
        File libPath = new File(dirName);
        libPath.mkdirs();
        File jarFile = new File(libPath, "test.jar");
        File zipFile = new File(libPath, "test.zip");
        
        try {
            jarFile.createNewFile();
            zipFile.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        XBUSClassLoader.addUrls(urls, dirName);
        
        assertFalse(urls.isEmpty());
        assertTrue(urls.contains(jarFile.toURL()));
        assertTrue(urls.contains(zipFile.toURL()));
    }
    
    @Test
    public void createClassLoader_xbusHomeNull_exitsWithError() {
        ClassLoader parent = new ClassLoader();
        
        System.setSecurityManager(new SecurityManager() {
            @Override
            public void checkPermission(Permission perm) {
                // Do nothing to allow System.exit() during testing
            }
        });
        
        try {
            XBUSClassLoader.createClassLoader(parent);
            fail("Expected System.exit(1) to be called");
        }
        catch (SecurityException e) {
            // Expected exception
        }
        finally {
            System.setSecurityManager(null);
        }
    }
    
    @Test
    public void createClassLoader_xbusHomeValid_returnsXBUSClassLoaderInstance() {
        ClassLoader parent = new ClassLoader();
        String xbusHome = "/path/to/xbus/home";
        Constants.XBUS_HOME = xbusHome;
        
        XBUSClassLoader loader = XBUSClassLoader.createClassLoader(parent);
        
        assertNotNull(loader);
        assertTrue(loader instanceof XBUSClassLoader);
        assertEquals(xbusHome + "/lib", urls.elementAt(0).toString());
        assertEquals(xbusHome + "/lib/runtime", urls.elementAt(1).toString());
        assertEquals(xbusHome + "/plugin/lib", urls.elementAt(2).toString());
        assertEquals(xbusHome + "/test/lib", urls.elementAt(3).toString());
    }

}