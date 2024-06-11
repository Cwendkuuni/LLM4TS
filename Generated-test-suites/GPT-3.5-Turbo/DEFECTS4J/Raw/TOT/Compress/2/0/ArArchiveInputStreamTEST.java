import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arArchiveInputStream;

    @Before
    public void setUp() {
        byte[] data = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a,
                // Example AR Entry
                0x74, 0x65, 0x73, 0x74, 0x2e, 0x74, 0x78, 0x74, 0x00, 0x00, 0x00, 0x2a,
                // AR Entry Data
                0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x20, 0x41, 0x50, 0x49, 0x20, 0x54, 0x65, 0x73, 0x74, 0x21};
        InputStream inputStream = new ByteArrayInputStream(data);
        arArchiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = arArchiveInputStream.getNextArEntry();

        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(42, entry.getLength());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = arArchiveInputStream.getNextEntry();

        Assert.assertNotNull(entry);
        Assert.assertTrue(entry instanceof ArArchiveEntry);
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(42, ((ArArchiveEntry) entry).getLength());
    }

    @Test
    public void testClose() throws IOException {
        arArchiveInputStream.close();

        // Verify that the input stream is closed
        // (cannot test if the underlying input stream is also closed)
        try {
            arArchiveInputStream.read();
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test
    public void testRead() throws IOException {
        int value = arArchiveInputStream.read();

        Assert.assertEquals(0x74, value);
    }

    @Test
    public void testReadBytes() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = arArchiveInputStream.read(buffer);

        Assert.assertEquals(5, bytesRead);
        Assert.assertArrayEquals(new byte[]{0x74, 0x65, 0x73, 0x74, 0x2e}, buffer);
    }

    @Test
    public void testReadBytesWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = arArchiveInputStream.read(buffer, 2, 5);

        Assert.assertEquals(5, bytesRead);
        Assert.assertArrayEquals(new byte[]{0x00, 0x00, 0x74, 0x65, 0x73, 0x74, 0x2e, 0x74, 0x78, 0x74}, buffer);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};

        Assert.assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatchesWithInvalidSignature() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0b};

        Assert.assertFalse(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatchesWithInvalidLength() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e};

        Assert.assertFalse(ArArchiveInputStream.matches(signature, signature.length));
    }

}