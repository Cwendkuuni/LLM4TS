
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testReadSingleByte() throws IOException {
        byte[] input = "Hello World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int byteRead = base64InputStream.read();

        Assert.assertEquals(input[0], byteRead);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] input = "Hello World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] buffer = new byte[5];
        int bytesRead = base64InputStream.read(buffer, 0, 5);

        Assert.assertEquals(5, bytesRead);
        Assert.assertArrayEquals("Hello".getBytes(), buffer);
    }

    @Test
    public void testReadEndOfFile() throws IOException {
        byte[] input = "Hello".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] buffer = new byte[10];
        int bytesRead = base64InputStream.read(buffer, 0, 10);

        Assert.assertEquals(5, bytesRead);
        Assert.assertArrayEquals("Hello".getBytes(), buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        base64InputStream.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeOffset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        base64InputStream.read(new byte[5], -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeLength() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        base64InputStream.read(new byte[5], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidBufferSize() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        base64InputStream.read(new byte[10], 0, 5);
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        boolean markSupported = base64InputStream.markSupported();

        Assert.assertFalse(markSupported);
    }
}
