import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_withValidArchiverName_shouldReturnArchiveInputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream("test".getBytes());
            ArchiveInputStream result = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            assertNotNull(result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testCreateArchiveInputStream_withNullArchiverName_shouldThrowNullPointerException() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream("test".getBytes());
            factory.createArchiveInputStream(null, in);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullInputStream_shouldThrowIllegalArgumentException() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_withInvalidArchiverName_shouldThrowArchiveException() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream("test".getBytes());
            factory.createArchiveInputStream("invalid", in);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_withValidArchiverName_shouldReturnArchiveOutputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream result = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            assertNotNull(result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testCreateArchiveOutputStream_withNullArchiverName_shouldThrowNullPointerException() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            OutputStream out = new ByteArrayOutputStream();
            factory.createArchiveOutputStream(null, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_withNullOutputStream_shouldThrowIllegalArgumentException() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_withInvalidArchiverName_shouldThrowArchiveException() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            OutputStream out = new ByteArrayOutputStream();
            factory.createArchiveOutputStream("invalid", out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_withValidInputStream_shouldReturnArchiveInputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream("test".getBytes());
            ArchiveInputStream result = factory.createArchiveInputStream(in);
            assertNotNull(result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testCreateArchiveInputStream_withNullInputStream_shouldThrowNullPointerException() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveInputStream(null);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withInputStreamThatDoesNotSupportMark_shouldThrowIllegalArgumentException() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new FileInputStream(new File("test.txt"));
            factory.createArchiveInputStream(in);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_withInvalidInputStream_shouldThrowArchiveException() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream("test".getBytes());
            factory.createArchiveInputStream(in);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_withZipInputStream_shouldReturnZipArchiveInputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new FileInputStream(new File("test.zip"));
            ArchiveInputStream result = factory.createArchiveInputStream(in);
            assertTrue(result instanceof ZipArchiveInputStream);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_withJarInputStream_shouldReturnJarArchiveInputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new FileInputStream(new File("test.jar"));
            ArchiveInputStream result = factory.createArchiveInputStream(in);
            assertTrue(result instanceof JarArchiveInputStream);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_withArInputStream_shouldReturnArArchiveInputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new FileInputStream(new File("test.ar"));
            ArchiveInputStream result = factory.createArchiveInputStream(in);
            assertTrue(result instanceof ArArchiveInputStream);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_withCpioInputStream_shouldReturnCpioArchiveInputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new FileInputStream(new File("test.cpio"));
            ArchiveInputStream result = factory.createArchiveInputStream(in);
            assertTrue(result instanceof CpioArchiveInputStream);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_withInvalidTarInputStream_shouldThrowArchiveException() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new FileInputStream(new File("test.tar"));
            factory.createArchiveInputStream(in);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_withTarInputStream_shouldReturnTarArchiveInputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new FileInputStream(new File("test.tar.gz"));
            ArchiveInputStream result = factory.createArchiveInputStream(in);
            assertTrue(result instanceof TarArchiveInputStream);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_withValidGzippedTarInputStream_shouldReturnTarArchiveInputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new FileInputStream(new File("test.tar.gz"));
            ArchiveInputStream result = factory.createArchiveInputStream(in);
            assertTrue(result instanceof TarArchiveInputStream);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_withInvalidGzippedTarInputStream_shouldThrowArchiveException() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new FileInputStream(new File("test.invalid.tar.gz"));
            factory.createArchiveInputStream(in);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
  
}