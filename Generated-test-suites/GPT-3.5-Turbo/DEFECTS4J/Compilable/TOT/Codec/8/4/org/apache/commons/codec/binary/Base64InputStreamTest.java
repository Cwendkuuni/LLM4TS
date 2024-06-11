package org.apache.commons.codec.binary;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;
    private InputStream inputStream;

    @Before
    public void setup() {
        byte[] data = "Hello World!".getBytes();
        inputStream = new ByteArrayInputStream(data);
        base64InputStream = new Base64InputStream(inputStream);
    }

    @Test
    public void testRead() throws Exception {
        int expected = 72; // ASCII value of 'H'
        int result = base64InputStream.read();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReadWithEmptyInputStream() throws Exception {
        base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        int result = base64InputStream.read();
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testReadBytes() throws Exception {
        byte[] buffer = new byte[5];
        int expected = 5;
        int result = base64InputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
        Assert.assertArrayEquals("Hello".getBytes(), buffer);
    }

    @Test
    public void testReadBytesWithOffset() throws Exception {
        byte[] buffer = new byte[7];
        int expected = 4;
        int result = base64InputStream.read(buffer, 3, expected);
        Assert.assertEquals(expected, result);
        Assert.assertArrayEquals(new byte[]{0, 0, 0, 72, 101, 108, 0}, buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testReadBytesWithNullBuffer() throws Exception {
        base64InputStream.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBytesWithNegativeOffset() throws Exception {
        base64InputStream.read(new byte[5], -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBytesWithInvalidLength() throws Exception {
        base64InputStream.read(new byte[5], 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBytesWithInvalidBufferSize() throws Exception {
        base64InputStream.read(new byte[10], 0, 5);
    }

    @Test
    public void testMarkNotSupported() throws Exception {
        boolean result = base64InputStream.markSupported();
        Assert.assertEquals(false, result);
    }

}