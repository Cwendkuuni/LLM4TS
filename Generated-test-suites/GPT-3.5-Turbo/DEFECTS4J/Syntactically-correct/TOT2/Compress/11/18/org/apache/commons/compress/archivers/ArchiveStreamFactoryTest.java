import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        // Define test input
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream in = new ByteArrayInputStream(new byte[0]);

        // Test createArchiveInputStream with valid parameters
        try {
            ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
            assertNotNull(ais);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test createArchiveInputStream with null archiverName
        try {
            new ArchiveStreamFactory().createArchiveInputStream(null, in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test createArchiveInputStream with null InputStream
        try {
            new ArchiveStreamFactory().createArchiveInputStream(archiverName, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        // Define test output
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream out = new ByteArrayOutputStream();

        // Test createArchiveOutputStream with valid parameters
        try {
            ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, out);
            assertNotNull(aos);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test createArchiveOutputStream with null archiverName
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(null, out);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test createArchiveOutputStream with null OutputStream
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(archiverName, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_autoDetection() {
        // Define test signature
        byte[] signature = new byte[12];

        // Test createArchiveInputStream autoDetection with valid signature
        try {
            InputStream in = new ByteArrayInputStream(signature);
            ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
            assertNotNull(ais);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test createArchiveInputStream autoDetection with null InputStream
        try {
            new ArchiveStreamFactory().createArchiveInputStream(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test createArchiveInputStream autoDetection with InputStream that does not support mark
        try {
            InputStream in = new InputStream() {
                @Override
                public int read() throws IOException {
                    return 0;
                }
            };
            new ArchiveStreamFactory().createArchiveInputStream(in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // Add more test cases for edge cases and exceptions if needed

}