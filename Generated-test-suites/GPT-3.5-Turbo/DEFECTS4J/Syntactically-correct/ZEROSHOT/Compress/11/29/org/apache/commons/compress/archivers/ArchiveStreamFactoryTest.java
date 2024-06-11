import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_archiverNameInNull() throws ArchiveException {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[10]));
            fail("IllegalArgumentException not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_inputStreamInNull() throws ArchiveException {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveInputStream("zip", null);
            fail("IllegalArgumentException not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_archiverNameInNull() throws ArchiveException {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
            fail("IllegalArgumentException not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_outputStreamInNull() throws ArchiveException {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveOutputStream("zip", null);
            fail("IllegalArgumentException not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_archiverNameInAR() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream result = factory.createArchiveInputStream("ar", new ByteArrayInputStream(new byte[10]));
        assertNotNull(result);
        assertTrue(result instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_archiverNameInZIP() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream result = factory.createArchiveInputStream("zip", new ByteArrayInputStream(new byte[10]));
        assertNotNull(result);
        assertTrue(result instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_archiverNameInTAR() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream result = factory.createArchiveInputStream("tar", new ByteArrayInputStream(new byte[10]));
        assertNotNull(result);
        assertTrue(result instanceof TarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_archiverNameInJAR() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream result = factory.createArchiveInputStream("jar", new ByteArrayInputStream(new byte[10]));
        assertNotNull(result);
        assertTrue(result instanceof JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_archiverNameInCPIO() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream result = factory.createArchiveInputStream("cpio", new ByteArrayInputStream(new byte[10]));
        assertNotNull(result);
        assertTrue(result instanceof CpioArchiveInputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_archiverNameInAR() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream result = factory.createArchiveOutputStream("ar", new ByteArrayOutputStream());
        assertNotNull(result);
        assertTrue(result instanceof ArArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_archiverNameInZIP() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream result = factory.createArchiveOutputStream("zip", new ByteArrayOutputStream());
        assertNotNull(result);
        assertTrue(result instanceof ZipArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_archiverNameInTAR() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream result = factory.createArchiveOutputStream("tar", new ByteArrayOutputStream());
        assertNotNull(result);
        assertTrue(result instanceof TarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_archiverNameInJAR() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream result = factory.createArchiveOutputStream("jar", new ByteArrayOutputStream());
        assertNotNull(result);
        assertTrue(result instanceof JarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_archiverNameInCPIO() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream result = factory.createArchiveOutputStream("cpio", new ByteArrayOutputStream());
        assertNotNull(result);
        assertTrue(result instanceof CpioArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveInputStream_inputStreamInNull() throws ArchiveException {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveInputStream(null);
            fail("IllegalArgumentException not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Stream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_inputStreamDoesNotSupportMark() throws ArchiveException {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new InputStream() {
                public int read() { return -1; }
            };
            factory.createArchiveInputStream(in);
            fail("IllegalArgumentException not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Mark is not supported.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_inputStreamInZip() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[]{80, 75, 3, 4});
        ArchiveInputStream result = factory.createArchiveInputStream(in);
        assertNotNull(result);
        assertTrue(result instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_inputStreamInJar() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[]{80, 75, 3, 4, 10});
        ArchiveInputStream result = factory.createArchiveInputStream(in);
        assertNotNull(result);
        assertTrue(result instanceof JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_inputStreamInAr() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[]{-1,-2,-3,-4,-5});
        ArchiveInputStream result = factory.createArchiveInputStream(in);
        assertNotNull(result);
        assertTrue(result instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_inputStreamInCpio() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[]{070, 117, 115, 116, 97, 114});
        ArchiveInputStream result = factory.createArchiveInputStream(in);
        assertNotNull(result);
        assertTrue(result instanceof CpioArchiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_inputStreamInUnknown() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[]{0, 1, 2, 3, 4});
        factory.createArchiveInputStream(in);
    }
}