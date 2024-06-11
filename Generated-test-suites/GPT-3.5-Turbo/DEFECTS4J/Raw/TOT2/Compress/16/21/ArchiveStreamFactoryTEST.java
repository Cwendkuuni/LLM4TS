import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ValidInput() {
        byte[] data = {0x50, 0x4B, 0x03, 0x04}; // ZIP file signature
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_InvalidInput() {
        byte[] data = {0x00, 0x01, 0x02, 0x03}; // Invalid signature
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
            fail("Exception not thrown for invalid input");
        } catch (ArchiveException e) {
            // Test passed
        }
    }

    @Test
    public void testCreateArchiveOutputStream_ValidOutput() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_InvalidOutput() {
        ByteArrayOutputStream outputStream = null;
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
            fail("Exception not thrown for invalid output");
        } catch (ArchiveException e) {
            // Test passed
        }
    }

    @Test
    public void testCreateArchiveInputStream_InvalidName() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("invalid", inputStream);
            fail("Exception not thrown for invalid archiver name");
        } catch (ArchiveException e) {
            // Test passed
        }
    }

    @Test
    public void testCreateArchiveOutputStream_InvalidName() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream("invalid", outputStream);
            fail("Exception not thrown for invalid archiver name");
        } catch (ArchiveException e) {
            // Test passed
        }
    }
}