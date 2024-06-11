import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Ar Archive Input Stream.
 */
public class ArArchiveInputStreamTest {

    @Test
    public void testConstructor() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        try (ArArchiveInputStream testee = new ArArchiveInputStream(input)) {
            // nothing to test here
        }
    }

    @Test
    public void testGetNextArEntry_normalCase() throws IOException {
        final byte[] signature = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        final byte[] header = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a,
                                          (byte) 0x74, 0x65, 0x73, 0x74, 0x31, 0x37, 0x37, 0x32,
                                          (byte) 0x34, 0x30, 0x32, 0x31, 0x38, 0x37, 0x34, 0x33,
                                          (byte) 0x32, 0x35, 0x33, 0x30, 0x38, 0x38, 0x34, 0x30,
                                          (byte) 0x31, 0x33, 0x37, 0x34, 0x31, 0x37, 0x31, 0x0a,
                                          (byte) 0x33, 0x31, 0x31, 0x34, 0x31, 0x30, 0x31, 0x39,
                                          (byte) 0x31, 0x34, 0x38, 0x36, 0x31, 0x38, 0x37, 0x31,
                                          (byte) 0x34, 0x31, 0x37, 0x34, 0x31, 0x37, 0x31, 0x0a,
                                          (byte) 0x33, 0x31, 0x31, 0x34, 0x31, 0x30, 0x31, 0x39,
                                          (byte) 0x31, 0x34, 0x38, 0x36, 0x31, 0x38, 0x37, 0x31,
                                          (byte) 0x34, 0x31, 0x37, 0x34, 0x31, 0x37, 0x31};
        final ByteArrayInputStream input = new ByteArrayInputStream(signature);

        try (ArArchiveInputStream testee = new ArArchiveInputStream(input)) {
            final ArArchiveEntry entry = testee.getNextArEntry();
            Assert.assertNotNull(entry);
            Assert.assertEquals("test.txt", entry.getName());
            Assert.assertEquals(17L, entry.getSize());
        }
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_invalidHeader() throws IOException {
        final byte[] signature = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a,
                                          (byte) 0x74, 0x65, 0x73, 0x74, 0x31, 0x37, 0x32, 0x31,
                                          (byte) 0x34, 0x30, 0x32, 0x31, 0x38, 0x37, 0x34, 0x33,
                                          (byte) 0x32, 0x35, 0x33, 0x30, 0x38, 0x38, 0x34, 0x30,
                                          (byte) 0x31, 0x33, 0x37, 0x34, 0x31, 0x37, 0x31, 0x0a,
                                          (byte) 0x33, 0x31, 0x31, 0x34, 0x31, 0x30, 0x31, 0x39,
                                          (byte) 0x31, 0x34, 0x38, 0x36, 0x31, 0x38, 0x37, 0x31,
                                          (byte) 0x34, 0x31, 0x37, 0x34, 0x31, 0x37, 0x31, 0x0a,
                                          (byte) 0x33, 0x31, 0x31, 0x34, 0x31, 0x30, 0x31, 0x39,
                                          (byte) 0x31, 0x34, 0x38, 0x36, 0x31, 0x38, 0x37, 0x31,
                                          (byte) 0x34, 0x31, 0x37, 0x34, 0x31, 0x37, 0x31, 0x0a,
                                          (byte) 0x33, 0x31, 0x31, 0x34, 0x31, 0x30, 0x31, 0x39,
                                          (byte) 0x31, 0x34, 0x38, 0x36, 0x31, 0x38, 0x37, 0x31,
                                          (byte) 0x34, 0x31, 0x37, 0x34, 0x31, 0x37, 0x31};
        final ByteArrayInputStream input = new ByteArrayInputStream(signature);

        try (ArArchiveInputStream testee = new ArArchiveInputStream(input)) {
            testee.getNextArEntry();
            Assert.fail("Expected IOException but got none");
        }
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_noEntry() throws IOException {
        final byte[] signature = new byte[8];
        final ByteArrayInputStream input = new ByteArrayInputStream(signature);

        try (ArArchiveInputStream testee = new ArArchiveInputStream(input)) {
            testee.getNextArEntry();
            Assert.fail("Expected IOException but got none");
        }
    }

    @Test
    public void testGetNextEntry() throws IOException {
        testGetNextArEntry_normalCase();
    }

    @Test(expected = IOException.class)
    public void testGetNextEntry_invalidHeader() throws IOException {
        testGetNextArEntry_invalidHeader();
    }

    @Test(expected = IOException.class)
    public void testGetNextEntry_noEntry() throws IOException {
        testGetNextArEntry_noEntry();
    }

    @Test
    public void testClose() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        try (ArArchiveInputStream testee = new ArArchiveInputStream(input)) {
            testee.close();
        }
        try (ArArchiveInputStream testee = new ArArchiveInputStream(input)) {
            testee.getNextArEntry();
            Assert.fail("Expected IOException but got none");
        }
    }

    @Test
    public void testRead() throws IOException {
        final byte[] data = new byte[]{'T', 'E', 'S', 'T', ' ', 'F', 'I', 'L', 'E', 'S', '\n'};
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1];
        try (ArArchiveInputStream testee = new ArArchiveInputStream(new ByteArrayInputStream(data))) {
            int read = 0;
            while ((read = testee.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            Assert.assertEquals(new String(data), new String(output.toByteArray()));
        }
    }

    @Test
    public void testRead_emptyInputStream() throws IOException {
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        final byte[] buffer = new byte[1];
        try (ArArchiveInputStream testee = new ArArchiveInputStream(input)) {
            int read = testee.read(buffer);
            Assert.assertEquals(-1, read);
        }
    }

    @Test
    public void testRead_bufferFull() throws IOException {
        final byte[] data = new byte[]{'T', 'E', 'S', 'T', ' ', 'F', 'I', 'L', 'E', 'S', '\n'};
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final byte[] buffer = new byte[data.length];
        try (ArArchiveInputStream testee = new ArArchiveInputStream(new ByteArrayInputStream(data))) {
            int read = testee.read(buffer);
            output.write(buffer, 0, read);
            Assert.assertEquals(new String(data), new String(output.toByteArray()));
        }
    }

    @Test(expected = IOException.class)
    public void testRead_nullBuffer() throws IOException {
        final byte[] data = new byte[]{'T', 'E', 'S', 'T', ' ', 'F', 'I', 'L', 'E', 'S', '\n'};
        try (ArArchiveInputStream testee = new ArArchiveInputStream(new ByteArrayInputStream(data))) {
            testee.read(null);
            Assert.fail("Expected IOException but got none");
        }
    }

    @Test
    public void testMatches() throws IOException {
        final byte[] signature = new byte[]{'T', 'E', 'S', 'T', ' ', 'F', 'I', 'L', 'E', 'S', '\n'};
        Assert.assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }
}