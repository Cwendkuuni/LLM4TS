import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import org.junit.Assert;
import org.junit.Test;

public class Base64InputStreamTest {

    // Test case for constructor Base64InputStream(InputStream in)
    @Test
    public void testBase64InputStream1() {
        byte[] inputBytes = new byte[] {65, 66, 67}; // "ABC"
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        Base64InputStream base64InputStream = new Base64InputStream(input);

        Assert.assertNotNull(base64InputStream);
    }

    // Test case for constructor Base64InputStream(InputStream in, boolean doEncode)
    @Test
    public void testBase64InputStream2() {
        byte[] inputBytes = Base64.getEncoder().encode("ABC".getBytes()); // "QUJD"
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        Base64InputStream base64InputStream = new Base64InputStream(input, true);

        Assert.assertNotNull(base64InputStream);
    }

    // Test case for constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator)
    @Test
    public void testBase64InputStream3() {
        byte[] inputBytes = Base64.getEncoder().encode("ABC".getBytes()); // "QUJD"
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        Base64InputStream base64InputStream = new Base64InputStream(input, true, 76, "\r\n".getBytes());

        Assert.assertNotNull(base64InputStream);
    }

    // Test case for read()
    @Test
    public void testRead1() throws IOException {
        byte[] inputBytes = new byte[] {65, 66, 67}; // "ABC"
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        Base64InputStream base64InputStream = new Base64InputStream(input);
        int result = base64InputStream.read();

        Assert.assertEquals(65, result); // 'A'
    }

    // Test case for read(byte[] b, int offset, int len)
    @Test
    public void testRead2() throws IOException {
        byte[] inputBytes = Base64.getEncoder().encode("ABC".getBytes()); // "QUJD"
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        byte[] outputBytes = new byte[3];
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        int result = base64InputStream.read(outputBytes, 0, 3);

        Assert.assertEquals(3, result);
        Assert.assertArrayEquals(new byte[] {65, 66, 67}, outputBytes); // "ABC"
    }

    // Test case for markSupported()
    @Test
    public void testMarkSupported() {
        byte[] inputBytes = new byte[] {65, 66, 67}; // "ABC"
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        Base64InputStream base64InputStream = new Base64InputStream(input);
        boolean result = base64InputStream.markSupported();

        Assert.assertFalse(result);
    }

    // Test case for read() with EOF reached
    @Test
    public void testReadWithEOF() throws IOException {
        byte[] inputBytes = new byte[] {65}; // "A"
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        Base64InputStream base64InputStream = new Base64InputStream(input);
        int result1 = base64InputStream.read(); // 'A'
        int result2 = base64InputStream.read(); // EOF

        Assert.assertEquals(65, result1); // 'A'
        Assert.assertEquals(-1, result2);
    }

    // Test case for read(byte[] b, int offset, int len) with EOF reached
    @Test
    public void testReadWithEOF2() throws IOException {
        byte[] inputBytes = Base64.getEncoder().encode("A".getBytes()); // "QQ=="
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        byte[] outputBytes = new byte[3];
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        int result1 = base64InputStream.read(outputBytes, 0, 1); // 'A'
        int result2 = base64InputStream.read(outputBytes, 1, 2); // EOF

        Assert.assertEquals(1, result1);
        Assert.assertEquals(-1, result2);
        Assert.assertArrayEquals(new byte[] {65, 0, 0}, outputBytes); // "A\0\0"
    }

    // Test case for read(byte[] b, int offset, int len) with buffer being null
    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        byte[] inputBytes = new byte[] {65}; // "A"
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        Base64InputStream base64InputStream = new Base64InputStream(input);
        base64InputStream.read(null, 0, 1);
    }

    // Test case for read(byte[] b, int offset, int len) with invalid offset
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] inputBytes = new byte[] {65}; // "A"
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        Base64InputStream base64InputStream = new Base64InputStream(input);
        base64InputStream.read(new byte[1], -1, 1);
    }

    // Test case for read(byte[] b, int offset, int len) with invalid len
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLen() throws IOException {
        byte[] inputBytes = new byte[] {65}; // "A"
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        Base64InputStream base64InputStream = new Base64InputStream(input);
        base64InputStream.read(new byte[1], 0, -1);
    }

    // Test case for read(byte[] b, int offset, int len) with invalid buffer size
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws IOException {
        byte[] inputBytes = new byte[] {65}; // "A"
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        Base64InputStream base64InputStream = new Base64InputStream(input);
        base64InputStream.read(new byte[1], 0, 2);
    }

}