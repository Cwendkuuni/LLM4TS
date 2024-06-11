import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory = new ArchiveStreamFactory();

    // Test the createArchiveInputStream() method with valid archiverName and InputStream
    @Test
    public void testCreateArchiveInputStream_ValidData_ReturnsArchiveInputStream() throws ArchiveException, IOException {
        // Prepare
        String archiverName = "zip";
        byte[] testData = new byte[] { 0, 1, 2, 3, 4 };
        InputStream in = new ByteArrayInputStream(testData);

        // Execute
        ArchiveInputStream ais = factory.createArchiveInputStream(archiverName, in);

        // Verify
        assertTrue(ais instanceof ArchiveInputStream);
    }

    // Test the createArchiveInputStream() method with null archiverName
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName_ThrowsIllegalArgumentException() throws ArchiveException {
        // Prepare
        String archiverName = null;
        InputStream in = new ByteArrayInputStream(new byte[0]);

        // Execute
        factory.createArchiveInputStream(archiverName, in);

        // Verify - Exception is thrown
    }

    // Test the createArchiveInputStream() method with null InputStream
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream_ThrowsIllegalArgumentException() throws ArchiveException {
        // Prepare
        String archiverName = "tar";
        InputStream in = null;

        // Execute
        factory.createArchiveInputStream(archiverName, in);

        // Verify - Exception is thrown
    }

    // Test the createArchiveOutputStream() method with valid archiverName and OutputStream
    @Test
    public void testCreateArchiveOutputStream_ValidData_ReturnsArchiveOutputStream() throws ArchiveException, IOException {
        // Prepare
        String archiverName = "tar";
        OutputStream out = new ByteArrayOutputStream();

        // Execute
        ArchiveOutputStream aos = factory.createArchiveOutputStream(archiverName, out);

        // Verify
        assertTrue(aos instanceof ArchiveOutputStream);
    }

    // Test the createArchiveOutputStream() method with null archiverName
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName_ThrowsIllegalArgumentException() throws ArchiveException {
        // Prepare
        String archiverName = null;
        OutputStream out = new ByteArrayOutputStream();

        // Execute
        factory.createArchiveOutputStream(archiverName, out);

        // Verify - Exception is thrown
    }

    // Test the createArchiveOutputStream() method with null OutputStream
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream_ThrowsIllegalArgumentException() throws ArchiveException {
        // Prepare
        String archiverName = "zip";
        OutputStream out = null;

        // Execute
        factory.createArchiveOutputStream(archiverName, out);

        // Verify - Exception is thrown
    }

    // Test the createArchiveInputStream() method with valid InputStream
    @Test
    public void testCreateArchiveInputStream_AutoDetect_ReturnsArchiveInputStream() throws ArchiveException, IOException {
        // Prepare
        byte[] testData = new byte[] { 80, 75, 3, 4 };
        InputStream in = new ByteArrayInputStream(testData);

        // Execute
        ArchiveInputStream ais = factory.createArchiveInputStream(in);

        // Verify
        assertTrue(ais instanceof ArchiveInputStream);
    }

    // Test the createArchiveInputStream() method with invalid InputStream
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AutoDetect_InvalidInputStream_ThrowsArchiveException()
            throws ArchiveException, IOException {
        // Prepare
        byte[] testData = new byte[] { 0, 1, 2, 3, 4 };
        InputStream in = new ByteArrayInputStream(testData);

        // Execute
        factory.createArchiveInputStream(in);

        // Verify - Exception is thrown
    }

    // Test the createArchiveInputStream() method with null InputStream
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_AutoDetect_NullInputStream_ThrowsIllegalArgumentException()
            throws ArchiveException {
        // Prepare
        InputStream in = null;

        // Execute
        factory.createArchiveInputStream(in);

        // Verify - Exception is thrown
    }

    // Test the createArchiveInputStream() method with InputStream that does not support mark
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_AutoDetect_StreamDoesNotSupportMark_ThrowsIllegalArgumentException()
            throws ArchiveException {
        // Prepare
        InputStream in = new InputStream() {
            public int read() throws IOException {
                return 0;
            }
        };

        // Execute
        factory.createArchiveInputStream(in);

        // Verify - Exception is thrown
    }

}