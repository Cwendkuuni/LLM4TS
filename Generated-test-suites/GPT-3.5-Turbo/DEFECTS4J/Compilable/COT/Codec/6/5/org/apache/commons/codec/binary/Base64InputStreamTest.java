package org.apache.commons.codec.binary;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] input = "Hello, World!".getBytes();
        InputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int result = base64InputStream.read();

        Assert.assertEquals(72, result);
    }

    @Test
    public void testReadWithOffsetAndLen() throws IOException {
        byte[] input = "Hello, World!".getBytes();
        InputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);


        byte[] result = new byte[5];
        int bytesRead = base64InputStream.read(result, 0, 5);

        Assert.assertEquals(5, bytesRead);
        Assert.assertArrayEquals(new byte[]{72, 101, 108, 108, 111}, result);
    }

    @Test
    public void testReadWithOffsetAndLenEqualToBufferLength() throws IOException {
        byte[] input = "Hello, World!".getBytes();
        InputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);


        byte[] result = new byte[13];
        int bytesRead = base64InputStream.read(result, 0, 13);

        Assert.assertEquals(13, bytesRead);
        Assert.assertArrayEquals(new byte[]{72, 101, 108, 108, 111, 44, 32, 87, 111, 114, 108, 100, 33}, result);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello, World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        base64InputStream.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        byte[] buffer = new byte[5];
        InputStream inputStream = new ByteArrayInputStream("Hello, World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        base64InputStream.read(buffer, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] buffer = new byte[5];
        InputStream inputStream = new ByteArrayInputStream("Hello, World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        base64InputStream.read(buffer, 10, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffsetAndLen() throws IOException {
        byte[] buffer = new byte[5];
        InputStream inputStream = new ByteArrayInputStream("Hello, World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        base64InputStream.read(buffer, 4, 5);
    }

    @Test
    public void testReadWithZeroLength() throws IOException {
        byte[] buffer = new byte[5];
        InputStream inputStream = new ByteArrayInputStream("Hello, World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int bytesRead = base64InputStream.read(buffer, 0, 0);

        Assert.assertEquals(0, bytesRead);
    }

    @Test
    public void testReadWithEmptyInputStream() throws IOException {
        byte[] buffer = new byte[5];
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int bytesRead = base64InputStream.read(buffer, 0, 5);

        Assert.assertEquals(-1, bytesRead);
    }
}

