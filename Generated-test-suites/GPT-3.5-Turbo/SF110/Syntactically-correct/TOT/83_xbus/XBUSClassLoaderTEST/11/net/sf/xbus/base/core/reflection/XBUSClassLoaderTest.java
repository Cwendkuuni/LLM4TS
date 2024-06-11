
import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;
import java.io.*;

public class XBUSClassLoaderTest {

    @Test
    public void getInstance_returnsNonNullInstance() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader loader = XBUSClassLoader.getInstance(parent);
        assertNotNull(loader);
    }

    @Test
    public void getInstance_returnsSameInstance() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader loader1 = XBUSClassLoader.getInstance(parent);
        XBUSClassLoader loader2 = XBUSClassLoader.getInstance(parent);
        assertEquals(loader1, loader2);
    }

    @Test
    public void createClassLoader_setXBUS_HOME_ErrorExit() {
        ClassLoader parent = getClass().getClassLoader();
        try {
            // Save current System exit
            SecurityManager securityManager = System.getSecurityManager();
            ExitException exitException = new ExitException();
            System.setSecurityManager(new SecurityManager() {
                @Override
                public void checkPermission(Permission perm) {
                    // Do nothing
                }

                @Override
                public void checkPermission(Permission perm, Object context) {
                    // Do nothing
                }

                @Override
                public void checkExit(int status) {
                    super.checkExit(status);
                    throw exitException;
                }
            });

            // Call createClassLoader with null XBUS_HOME
            try {
                XBUSClassLoader.createClassLoader(parent);
                fail("Expected ExitException to be thrown");
            } catch (ExitException e) {
                assertEquals("XBUS_HOME has not been set!", e.getMessage());
                assertEquals(1, e.getStatus());
            }

            // Restore original System exit
            System.setSecurityManager(securityManager);
        } catch (SecurityException e) {
            fail("Failed to set SecurityManager");
        }
    }

    @Test
    public void addUrls_addsURLsToVector() {
        Vector<URL> urls = new Vector<URL>();
        String dirName = "testData";
        XBUSClassLoader.addUrls(urls, dirName);
        assertFalse(urls.isEmpty());
    }

    @Test
    public void addUrls_skipsNonJarAndNonZipFiles() {
        Vector<URL> urls = new Vector<URL>();
        String dirName = "testData";
        XBUSClassLoader.addUrls(urls, dirName);
        assertTrue(urls.isEmpty());
    }

    @Test
    public void addUrls_handlesMalformedURL() {
        Vector<URL> urls = new Vector<URL>();
        String dirName = "testDataInvalid";
        XBUSClassLoader.addUrls(urls, dirName);
        assertTrue(urls.isEmpty());
    }

    private static class ExitException extends SecurityException {
        private final int status;

        public ExitException() {
            this(0);
        }

        public ExitException(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }
}
