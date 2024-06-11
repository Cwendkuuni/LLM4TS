package org.apache.commons.codec.binary;
import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int result = base64InputStream.read();
        Assert.assertEquals(72, result); // 'H' is encodes as 72
    }

    @Test
    public void testRead_multipleBytes() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[5];
        int bytesRead = base64InputStream.read(buffer);
        Assert.assertEquals(5, bytesRead);
        Assert.assertEquals(72, buffer[0]);
        Assert.assertEquals(101, buffer[1]);
        Assert.assertEquals(108, buffer[2]);
        Assert.assertEquals(108, buffer[3]);
        Assert.assertEquals(111, buffer[4]);
    }

    @Test
    public void testRead_offsetAndLength() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[5];
        int bytesRead = base64InputStream.read(buffer, 1, 3);
        Assert.assertEquals(3, bytesRead);
        Assert.assertEquals(0, buffer[0]);
        Assert.assertEquals(72, buffer[1]);
        Assert.assertEquals(101, buffer[2]);
        Assert.assertEquals(108, buffer[3]);
        Assert.assertEquals(0, buffer[4]);
    }

    @Test
    public void testRead_eof() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[20];
        int bytesRead = base64InputStream.read(buffer);
        Assert.assertEquals(13, bytesRead);
        Assert.assertEquals(72, buffer[0]);
        Assert.assertEquals(101, buffer[1]);
        Assert.assertEquals(108, buffer[2]);
        Assert.assertEquals(108, buffer[3]);
        Assert.assertEquals(111, buffer[4]);
        Assert.assertEquals(44, buffer[5]);
        Assert.assertEquals(32, buffer[6]);
        Assert.assertEquals(87, buffer[7]);
        Assert.assertEquals(111, buffer[8]);
        Assert.assertEquals(114, buffer[9]);
        Assert.assertEquals(108, buffer[10]);
        Assert.assertEquals(100, buffer[11]);
        Assert.assertEquals(33, buffer[12]);
    }

    @Test(expected = NullPointerException.class)
    public void testRead_nullByteArray() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_invalidOffset() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[5];
        base64InputStream.read(buffer, -1, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_invalidLength() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[5];
        base64InputStream.read(buffer, 1, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_invalidBufferSize() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[3];
        base64InputStream.read(buffer);
    }

    @Test
    public void testMarkSupported() {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        Assert.assertFalse(base64InputStream.markSupported());
    }
}