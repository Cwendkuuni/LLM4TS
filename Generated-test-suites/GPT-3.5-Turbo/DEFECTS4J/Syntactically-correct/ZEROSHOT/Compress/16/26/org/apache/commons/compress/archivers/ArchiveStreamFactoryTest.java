import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        
        try {
            ArchiveInputStream result = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
            assertNotNull(result);
            assertTrue(result instanceof ArArchiveInputStream);

            result = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            assertNotNull(result);
            assertTrue(result instanceof ZipArchiveInputStream);

            result = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in);
            assertNotNull(result);
            assertTrue(result instanceof TarArchiveInputStream);

            result = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in);
            assertNotNull(result);
            assertTrue(result instanceof JarArchiveInputStream);

            result = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
            assertNotNull(result);
            assertTrue(result instanceof CpioArchiveInputStream);

            result = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
            assertNotNull(result);
            assertTrue(result instanceof DumpArchiveInputStream);
        } catch (ArchiveException e) {
            fail("Caught ArchiveException: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamNullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        } catch (ArchiveException e) {
            fail("Caught ArchiveException: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamNullStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        } catch (ArchiveException e) {
            fail("Caught ArchiveException: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamUnknownArchiver() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        
        try {
            ArchiveOutputStream result = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
            assertNotNull(result);
            assertTrue(result instanceof ArArchiveOutputStream);

            result = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            assertNotNull(result);
            assertTrue(result instanceof ZipArchiveOutputStream);

            result = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
            assertNotNull(result);
            assertTrue(result instanceof TarArchiveOutputStream);

            result = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
            assertNotNull(result);
            assertTrue(result instanceof JarArchiveOutputStream);

            result = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
            assertNotNull(result);
            assertTrue(result instanceof CpioArchiveOutputStream);
        } catch (ArchiveException e) {
            fail("Caught ArchiveException: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamNullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        } catch (ArchiveException e) {
            fail("Caught ArchiveException: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamNullStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        } catch (ArchiveException e) {
            fail("Caught ArchiveException: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamUnknownArchiver() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveOutputStream("unknown", new ByteArrayOutputStream());
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        
        try {
            ArchiveInputStream result = factory.createArchiveInputStream(in);
            assertNotNull(result);
            assertTrue(result instanceof ZipArchiveInputStream);
        } catch (ArchiveException e) {
            fail("Caught ArchiveException: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithNullStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveInputStream(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Stream must not be null.", e.getMessage());
        } catch (ArchiveException e) {
            fail("Caught ArchiveException: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithUnsupportedStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        
        try {
            factory.createArchiveInputStream(in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Mark is not supported.", e.getMessage());
        } catch (ArchiveException e) {
            fail("Caught ArchiveException: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithUnknownSignature() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        // simulate unknown signature
        InputStream in = new ByteArrayInputStream(new byte[12]);
        
        try {
            factory.createArchiveInputStream(in);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
    }
}