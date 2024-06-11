import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    @Test
    public void testReadOneByte() throws IOException {
        byte[] data = "Hello!".getBytes();
        InputStream input = new ByteArrayInputStream(Base64.encodeBase64(data));
        Base64InputStream base64Input = new Base64InputStream(input);

        int byteRead = base64Input.read();
        
        Assert.assertNotNull(byteRead);
        Assert.assertEquals('H', byteRead);
    }

    @Test
    public void testReadMultipleBytes() throws IOException {
        byte[] data = "Hello, world!".getBytes();
        InputStream input = new ByteArrayInputStream(Base64.encodeBase64(data));
        Base64InputStream base64Input = new Base64InputStream(input);

        byte[] buffer = new byte[5];
        int bytesRead = base64Input.read(buffer);
        
        Assert.assertEquals(5, bytesRead);
        Assert.assertEquals('H', buffer[0]);
        Assert.assertEquals('e', buffer[1]);
        Assert.assertEquals('l', buffer[2]);
        Assert.assertEquals('l', buffer[3]);
        Assert.assertEquals('o', buffer[4]);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] data = "Hello, world!".getBytes();
        InputStream input = new ByteArrayInputStream(Base64.encodeBase64(data));
        Base64InputStream base64Input = new Base64InputStream(input);

        byte[] buffer = new byte[10];
        int bytesRead = base64Input.read(buffer, 3, 5);
        
        Assert.assertEquals(5, bytesRead);
        Assert.assertEquals(0, buffer[0]);
        Assert.assertEquals(0, buffer[1]);
        Assert.assertEquals(0, buffer[2]);
        Assert.assertEquals('H', buffer[3]);
        Assert.assertEquals('e', buffer[4]);
    }

    @Test
    public void testReadReturnsNegativeOneOnEOF() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64Input = new Base64InputStream(input);

        int byteRead = base64Input.read();
        
        Assert.assertEquals(-1, byteRead);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBufferThrowsNullPointerException() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64Input = new Base64InputStream(input);

        base64Input.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffsetThrowsIndexOutOfBoundsException() throws IOException {
        byte[] data = "Hello, world!".getBytes();
        InputStream input = new ByteArrayInputStream(Base64.encodeBase64(data));
        Base64InputStream base64Input = new Base64InputStream(input);

        byte[] buffer = new byte[10];
        base64Input.read(buffer, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLengthThrowsIndexOutOfBoundsException() throws IOException {
        byte[] data = "Hello, world!".getBytes();
        InputStream input = new ByteArrayInputStream(Base64.encodeBase64(data));
        Base64InputStream base64Input = new Base64InputStream(input);

        byte[] buffer = new byte[10];
        base64Input.read(buffer, 0, 15);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSizeThrowsIndexOutOfBoundsException() throws IOException {
        byte[] data = "Hello, world!".getBytes();
        InputStream input = new ByteArrayInputStream(Base64.encodeBase64(data));
        Base64InputStream base64Input = new Base64InputStream(input);

        byte[] buffer = new byte[5];
        base64Input.read(buffer);
    }
}