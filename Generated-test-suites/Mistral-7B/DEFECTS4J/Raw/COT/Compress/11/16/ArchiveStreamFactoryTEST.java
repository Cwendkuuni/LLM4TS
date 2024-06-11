import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.archiveentry.ZipArchiveEntry;
import org.apache.commons.compress.archivers.stream.ArchiveInputStream;
import org.apache.commons.compress.archivers.stream.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test with valid archiver name and InputStream
        InputStream input = new ByteArrayInputStream("valid_archive_data".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
        ZipArchiveEntry zipEntry = (ZipArchiveEntry) archiveInputStream.getNextEntry();
        assertNotNull(zipEntry);
        archiveInputStream.close();
        input.close();

        // Test with unknown archiver name and InputStream
        input = new ByteArrayInputStream("unknown_archive_data".getBytes());
        try {
            new ArchiveStreamFactory().createArchiveInputStream("unknown", input);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
        input.close();

        // Test with null InputStream
        try {
            new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test with valid archiver name and OutputStream
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
        archiveOutputStream.putArchiveEntry(new ZipArchiveEntry("test.txt"));
        archiveOutputStream.write("Test content".getBytes(), 0, "Test content".length());
        archiveOutputStream.finishStream(archiveOutputStream.getEntry());
        archiveOutputStream.close();

        byte[] expectedContent = "Test content".getBytes();
        byte[] actualContent = ((ByteArrayOutputStream) output).toByteArray();
        assertArrayEquals(expectedContent, actualContent);

        // Test with unknown archiver name and OutputStream
        output = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("unknown", output);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
        output.close();

        // Test with null OutputStream
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() throws Exception {
        // Test with valid stream
        InputStream input = new ByteArrayInputStream("valid_archive_data".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(input);
        ZipArchiveEntry zipEntry = (ZipArchiveEntry) archiveInputStream.getNextEntry();
        assertNotNull(zipEntry);
        archiveInputStream.close();
        input.close();

        // Test with empty stream
        input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream emptyArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(input);
        assertTrue(emptyArchiveInputStream.available() == 0);
        emptyArchiveInputStream.close();
        input.close();

        // Test with null stream
        try {
            new ArchiveStreamFactory().createArchiveInputStream(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithInvalidSignature() throws Exception {
        InputStream input = new ByteArrayInputStream("invalid_archive_data".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiver() throws Exception {
        InputStream input = new ByteArrayInputStream("unknown_archive_data".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream("unknown", input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(null, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws Exception {
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }
}