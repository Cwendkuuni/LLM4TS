import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.*;

public class ArchiveStreamFactoryTest {

    // Test createArchiveInputStream() method
    @Test
    public void testCreateArchiveInputStream() {
        // Typical use case
        ArchiveInputStream input1 = ArchiveStreamFactory.createArchiveInputStream("zip", new ByteArrayInputStream(new byte[0]));
        assertNotNull(input1);

        // Edge case - Unknown archiver name
        try {
            // Try to create archive input stream with unknown archiver name
            ArchiveInputStream input2 = ArchiveStreamFactory.createArchiveInputStream("invalid", new ByteArrayInputStream(new byte[0]));
            fail("Should throw ArchiveException when archiver name is unknown.");
        } catch (ArchiveException e) {
            // Expected
        }

        // Edge case - Null archiver name
        try {
            // Try to create archive input stream with null archiver name
            ArchiveInputStream input3 = ArchiveStreamFactory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
            fail("Should throw IllegalArgumentException when archiver name is null.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Edge case - Null input stream
        try {
            // Try to create archive input stream with null input stream
            ArchiveInputStream input4 = ArchiveStreamFactory.createArchiveInputStream("zip", null);
            fail("Should throw IllegalArgumentException when input stream is null.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test createArchiveOutputStream() method
    @Test
    public void testCreateArchiveOutputStream() {
        // Typical use case
        ArchiveOutputStream output1 = ArchiveStreamFactory.createArchiveOutputStream("zip", new ByteArrayOutputStream());
        assertNotNull(output1);

        // Edge case - Unknown archiver name
        try {
            // Try to create archive output stream with unknown archiver name
            ArchiveOutputStream output2 = ArchiveStreamFactory.createArchiveOutputStream("invalid", new ByteArrayOutputStream());
            fail("Should throw ArchiveException when archiver name is unknown.");
        } catch (ArchiveException e) {
            // Expected
        }

        // Edge case - Null archiver name
        try {
            // Try to create archive output stream with null archiver name
            ArchiveOutputStream output3 = ArchiveStreamFactory.createArchiveOutputStream(null, new ByteArrayOutputStream());
            fail("Should throw IllegalArgumentException when archiver name is null.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Edge case - Null output stream
        try {
            // Try to create archive output stream with null output stream
            ArchiveOutputStream output4 = ArchiveStreamFactory.createArchiveOutputStream("zip", null);
            fail("Should throw IllegalArgumentException when output stream is null.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test createArchiveInputStream() method with auto-detection
    @Test
    public void testCreateArchiveInputStreamWithAutodetection() {
        // Typical use case - ZipArchiveInputStream
        ArchiveInputStream input1 = ArchiveStreamFactory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{80, 75, 3, 4}));
        assertNotNull(input1);
        assertTrue(input1 instanceof ZipArchiveInputStream);

        // Typical use case - JarArchiveInputStream
        ArchiveInputStream input2 = ArchiveStreamFactory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{80, 75, 5, 6}));
        assertNotNull(input2);
        assertTrue(input2 instanceof JarArchiveInputStream);

        // Typical use case - ArArchiveInputStream
        ArchiveInputStream input3 = ArchiveStreamFactory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{33, 60, 33}));
        assertNotNull(input3);
        assertTrue(input3 instanceof ArArchiveInputStream);

        // Typical use case - CpioArchiveInputStream
        ArchiveInputStream input4 = ArchiveStreamFactory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{76, 97, 77, 66, 0, 1}));
        assertNotNull(input4);
        assertTrue(input4 instanceof CpioArchiveInputStream);

        // Typical use case - DumpArchiveInputStream
        ArchiveInputStream input5 = ArchiveStreamFactory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{30, 61, 6, 2}));
        assertNotNull(input5);
        assertTrue(input5 instanceof DumpArchiveInputStream);

        // Typical use case - TarArchiveInputStream
        ArchiveInputStream input6 = ArchiveStreamFactory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{257, 0, 0, 0, 0}));
        assertNotNull(input6);
        assertTrue(input6 instanceof TarArchiveInputStream);

        // Edge case - No Archiver found for the stream signature
        try {
            // Try to create archive input stream with unknown signature
            ArchiveInputStream input7 = ArchiveStreamFactory.createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
            fail("Should throw ArchiveException when no archiver found for the stream signature.");
        } catch (ArchiveException e) {
            // Expected
        }

        // Edge case - Null input stream
        try {
            // Try to create archive input stream with null input stream
            ArchiveInputStream input8 = ArchiveStreamFactory.createArchiveInputStream(null);
            fail("Should throw IllegalArgumentException when input stream is null.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

}