import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry_validEntry() throws IOException {
        byte[] inputBytes = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a,  // ar header
                0x0a, 0x0a, 0x0a, 0x0a, 0x0a};  // dummy entry content
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry entry = arInputStream.getNextArEntry();

        Assert.assertNotNull(entry);
        Assert.assertEquals("", entry.getName());
        Assert.assertEquals(0, entry.getSize());
    }

    @Test
    public void testGetNextArEntry_invalidHeader() throws IOException {
        byte[] inputBytes = new byte[]{0x0a, 0x0a, 0x0a, 0x0a, 0x0a, 0x0a, 0x0a, 0x0a,  // invalid header
                0x0a, 0x0a, 0x0a, 0x0a, 0x0a};  // dummy entry content
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        try {
            arInputStream.getNextArEntry();
            Assert.fail("IOException expected");
        } catch (IOException e) {
            Assert.assertEquals("invalid header ", e.getMessage());
        }
    }

    @Test
    public void testGetNextArEntry_noEntry() throws IOException {
        byte[] inputBytes = new byte[]{};
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry entry = arInputStream.getNextArEntry();

        Assert.assertNull(entry);
    }

    @Test
    public void testGetNextArEntry_oddOffset() throws IOException {
        byte[] inputBytes = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x0a,  // ar header with odd offset
                0x0a, 0x0a, 0x0a, 0x0a, 0x0a};  // dummy entry content
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry entry = arInputStream.getNextArEntry();

        Assert.assertNotNull(entry);
        Assert.assertEquals("", entry.getName());
        Assert.assertEquals(0, entry.getSize());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] inputBytes = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a,  // ar header
                0x0a, 0x0a, 0x0a, 0x0a, 0x0a};  // dummy entry content
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry entry = arInputStream.getNextEntry();

        Assert.assertNotNull(entry);
        Assert.assertEquals("", entry.getName());
        Assert.assertEquals(0, entry.getSize());
    }

    @Test
    public void testClose() throws IOException {
        byte[] inputBytes = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a,  // ar header
                0x0a, 0x0a, 0x0a, 0x0a, 0x0a};  // dummy entry content
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        arInputStream.close();

        try {
            arInputStream.getNextArEntry();
            Assert.fail("IOException expected");
        } catch (IOException e) {
            Assert.assertEquals("Stream closed", e.getMessage());
        }
    }

    @Test
    public void testRead_singleByte() throws IOException {
        byte[] inputBytes = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68,
                0x3e, 0x0a,  // ar header
                0x0a, 0x0a, 0x0a, 0x0a, 0x0a};  // dummy entry content
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        int singleByte = arInputStream.read();

        Assert.assertEquals(33, singleByte);
    }

    @Test
    public void testRead_bytes() throws IOException {
        byte[] inputBytes = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68,
                0x3e, 0x0a,  // ar header
                0x0a, 0x0a, 0x0a, 0x0a, 0x0a};  // dummy entry content
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        byte[] outputBytes = new byte[13];

        int bytesRead = arInputStream.read(outputBytes);

        Assert.assertEquals(13, bytesRead);
        Assert.assertArrayEquals(inputBytes, outputBytes);
    }

    @Test
    public void testRead_specificBytes() throws IOException {
        byte[] inputBytes = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68,
                0x3e, 0x0a,  // ar header
                0x0a, 0x0a, 0x0a, 0x0a, 0x0a};  // dummy entry content
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        byte[] outputBytes = new byte[5];

        int bytesRead = arInputStream.read(outputBytes, 1, 4);

        Assert.assertEquals(4, bytesRead);
        Assert.assertArrayEquals(new byte[]{0, 0x21, 0x3c, 0x61, 0x72}, outputBytes);
    }

    @Test
    public void testMatches_validSignature() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};  // valid AR signature
        int length = 8;

        boolean matches = ArArchiveInputStream.matches(signature, length);

        Assert.assertTrue(matches);
    }

    @Test
    public void testMatches_invalidSignature() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0b};  // invalid AR signature
        int length = 8;

        boolean matches = ArArchiveInputStream.matches(signature, length);

        Assert.assertFalse(matches);
    }

}