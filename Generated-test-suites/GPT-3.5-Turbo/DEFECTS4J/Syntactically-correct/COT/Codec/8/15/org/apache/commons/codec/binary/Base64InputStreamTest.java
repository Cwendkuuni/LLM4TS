
import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;

public class Base64InputStreamTest {

    @Test
    public void testConstructor() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        Assert.assertNotNull(base64InputStream);
    }

    @Test
    public void testRead() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        try {
            int readByte = base64InputStream.read();
            Assert.assertEquals(1, readByte);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
	
    @Test
    public void testReadWithOffsetAndLength() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[3];
        try {
            int bytesRead = base64InputStream.read(buffer, 0, 3);
            Assert.assertEquals(3, bytesRead);
            Assert.assertArrayEquals(new byte[]{1, 2, 3}, buffer);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        boolean markSupported = base64InputStream.markSupported();
        Assert.assertFalse(markSupported);
    }

}
