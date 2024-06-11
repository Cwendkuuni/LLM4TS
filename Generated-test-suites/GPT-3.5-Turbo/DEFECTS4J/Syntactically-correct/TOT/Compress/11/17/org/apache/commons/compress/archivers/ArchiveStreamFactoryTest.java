import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.Archive;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;
    private InputStream input;
    private OutputStream output;

    @Before
    public void setup() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStreamWithNullArchiverName() {
        try {
            input = new FileInputStream(new File("test.zip"));
            factory.createArchiveInputStream(null, input);
            fail("Expected IllegalArgumentException");
        } catch(IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        } catch(ArchiveException e) {
            fail("Unexpected ArchiveException");
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithNullInputStream() {
        try {
            factory.createArchiveInputStream("zip", null);
            fail("Expected IllegalArgumentException");
        } catch(IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        } catch(ArchiveException e) {
            fail("Unexpected ArchiveException");
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithUnsupportedArchiverName() {
        try {
            input = new FileInputStream(new File("test.zip"));
            factory.createArchiveInputStream("invalid", null);
            fail("Expected ArchiveException");
        } catch(IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        } catch(ArchiveException e) {
            assertEquals("Archiver: invalid not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithSupportedArchiverName() {
        try {
            input = new FileInputStream(new File("test.zip"));
            ArchiveInputStream ais = factory.createArchiveInputStream("zip", input);
            assertNotNull(ais);
        } catch(ArchiveException e) {
            fail("Unexpected ArchiveException");
        } catch(Exception e) {
            fail("Unexpected Exception");
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullArchiverName() {
        try {
            output = new FileOutputStream(new File("test.zip"));
            factory.createArchiveOutputStream(null, output);
            fail("Expected IllegalArgumentException");
        } catch(IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        } catch(ArchiveException e) {
            fail("Unexpected ArchiveException");
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        try {
            factory.createArchiveOutputStream("zip", null);
            fail("Expected IllegalArgumentException");
        } catch(IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        } catch(ArchiveException e) {
            fail("Unexpected ArchiveException");
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithUnsupportedArchiverName() {
        try {
            output = new FileOutputStream(new File("test.zip"));
            factory.createArchiveOutputStream("invalid", null);
            fail("Expected ArchiveException");
        } catch(IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        } catch(ArchiveException e) {
            assertEquals("Archiver: invalid not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithSupportedArchiverName() {
        try {
            output = new FileOutputStream(new File("test.zip"));
            ArchiveOutputStream aos = factory.createArchiveOutputStream("zip", output);
            assertNotNull(aos);
        } catch(ArchiveException e) {
            fail("Unexpected ArchiveException");
        } catch(Exception e) {
            fail("Unexpected Exception");
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithMarkNotSupported() {
        try {
            input = new ByteArrayInputStream(new byte[0]);
            factory.createArchiveInputStream(input);
            fail("Expected IllegalArgumentException");
        } catch(IllegalArgumentException e) {
            assertEquals("Mark is not supported.", e.getMessage());
        } catch(ArchiveException e) {
            fail("Unexpected ArchiveException");
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithUnknownInput() {
        try {
            input = new ByteArrayInputStream(new byte[12]);
            factory.createArchiveInputStream(input);
            fail("Expected ArchiveException");
        } catch(ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        } catch(Exception e) {
            fail("Unexpected Exception");
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithSupportedInput() {
        try {
            byte[] data = "Test Data".getBytes();
            input = new ByteArrayInputStream(data);
            ArchiveInputStream ais = factory.createArchiveInputStream(input);
            assertNotNull(ais);
        } catch(ArchiveException e) {
            fail("Unexpected ArchiveException");
        } catch(Exception e) {
            fail("Unexpected Exception");
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithTarInput() {
        try {
            byte[] data = ("ustar\0" + "2.0\0").getBytes();
            input = new ByteArrayInputStream(data);
            ArchiveInputStream ais = factory.createArchiveInputStream(input);
            assertNotNull(ais);
        } catch(ArchiveException e) {
            fail("Unexpected ArchiveException");
        } catch(Exception e) {
            fail("Unexpected Exception");
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithDumpInput() {
        try {
            byte[] data = "Test Data".getBytes();
            byte[] signature = new byte[32];
            System.arraycopy(data, 0, signature, 0, data.length);
            input = new ByteArrayInputStream(signature);
            ArchiveInputStream ais = factory.createArchiveInputStream(input);
            assertNotNull(ais);
        } catch(ArchiveException e) {
            fail("Unexpected ArchiveException");
        } catch(Exception e) {
            fail("Unexpected Exception");
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

}