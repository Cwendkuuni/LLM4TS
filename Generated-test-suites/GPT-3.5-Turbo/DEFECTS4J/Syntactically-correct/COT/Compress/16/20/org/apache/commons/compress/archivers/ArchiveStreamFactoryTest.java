import org.junit.Test;
import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        // Test with valid archiver name and input stream
        ArchiveInputStream result1 = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
        assertNotNull(result1);
        assertTrue(result1 instanceof ZipArchiveInputStream);

        // Test with null archiver name
        try {
            new ArchiveStreamFactory().createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
            fail("IllegalArgumentException should be thrown when archiver name is null");
        } catch (IllegalArgumentException e) {
            // Exception is expected
        }

        // Test with null input stream
        try {
            new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            fail("IllegalArgumentException should be thrown when input stream is null");
        } catch (IllegalArgumentException e) {
            // Exception is expected
        }

        // Test with an unknown archiver name
        try {
            new ArchiveStreamFactory().createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
            fail("ArchiveException should be thrown when archiver name is unknown");
        } catch (ArchiveException e) {
            // Exception is expected
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        // Test with valid archiver name and output stream
        ArchiveOutputStream result1 = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream());
        assertNotNull(result1);
        assertTrue(result1 instanceof ZipArchiveOutputStream);

        // Test with null archiver name
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(null, new ByteArrayOutputStream());
            fail("IllegalArgumentException should be thrown when archiver name is null");
        } catch (IllegalArgumentException e) {
            // Exception is expected
        }

        // Test with null output stream
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            fail("IllegalArgumentException should be thrown when output stream is null");
        } catch (IllegalArgumentException e) {
            // Exception is expected
        }

        // Test with an unknown archiver name
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("unknown", new ByteArrayOutputStream());
            fail("ArchiveException should be thrown when archiver name is unknown");
        } catch (ArchiveException e) {
            // Exception is expected
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutodetect() throws ArchiveException {
        // Test with a valid input stream
        ArchiveInputStream result1 = new ArchiveStreamFactory().createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        assertNotNull(result1);

        // Test with a null input stream
        try {
            new ArchiveStreamFactory().createArchiveInputStream(null);
            fail("IllegalArgumentException should be thrown when input stream is null");
        } catch (IllegalArgumentException e) {
            // Exception is expected
        }

        // Test with an invalid input stream (does not support mark)
        try {
            new ArchiveStreamFactory().createArchiveInputStream(new ByteArrayInputStream(new byte[0]) {
                @Override
                public boolean markSupported() {
                    return false;
                }
            });
            fail("IllegalArgumentException should be thrown when input stream does not support mark");
        } catch (IllegalArgumentException e) {
            // Exception is expected
        }

        // Test with an empty input stream
        try {
            new ArchiveStreamFactory().createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
            fail("ArchiveException should be thrown when input stream is empty");
        } catch (ArchiveException e) {
            // Exception is expected
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }

        // Test with input stream containing archived data
        try {
            byte[] data = new byte[]{31, -117, 8, 0, 0, 0, 0, 0, 0, 0};
            new ArchiveStreamFactory().createArchiveInputStream(new ByteArrayInputStream(data));
            fail("ArchiveException should be thrown when input stream contains archived data");
        } catch (ArchiveException e) {
            // Exception is expected
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }

        // Test with input stream containing corrupted archive data
        try {
            byte[] data = new byte[]{31, -117, 8, 0, 0, 0, -1, -1, -1, -1};
            new ArchiveStreamFactory().createArchiveInputStream(new ByteArrayInputStream(data));
            fail("ArchiveException should be thrown when input stream contains corrupted archive data");
        } catch (ArchiveException e) {
            // Exception is expected
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
    }
}