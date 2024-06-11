package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test cases for Base64InputStream class
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with default arguments
     * @throws IOException
     */
    @Test
    public void testDefaultConstructor() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        Assert.assertFalse(base64InputStream.doEncode);
        Assert.assertNull(base64InputStream.base64);
    }

    /**
     * Test constructor with encode argument
     * @throws IOException
     */
    @Test
    public void testEncodeConstructor() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), true);
        Assert.assertTrue(base64InputStream.doEncode);
        Assert.assertNotNull(base64InputStream.base64);
    }

    /**
     * Test read method with default constructor
     * @throws IOException
     */
    @Test
    public void testReadDefaultConstructor() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        int read = base64InputStream.read();
        Assert.assertEquals(-1, read);
    }

    /**
     * Test read method with encode constructor
     * @throws IOException
     */
    @Test
    public void testReadEncodeConstructor() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), true);
        byte[] input = new byte[]{'A', 'B', 'C', 'D'};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        base64InputStream = new Base64InputStream(inputStream, true);
        int read;
        byte[] buffer = new byte[4];
        while ((read = base64InputStream.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, read);
        }
        Assert.assertArrayEquals("Invalid output", new byte[]{'A', 'B', 'C', '=', 'D', '='}, outputStream.toByteArray());
    }

    /**
     * Test read method with decode argument and empty input
     * @throws IOException
     */
    @Test
    public void testReadDecodeConstructorEmptyInput() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), false);
        int read = base64InputStream.read();
        Assert.assertEquals(-1, read);
    }

    /**
     * Test read method with decode argument and valid input
     * @throws IOException
     */
    @Test
    public void testReadDecodeConstructorValidInput() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{'A', 'B', 'C', '=', 'D', '='}), false);
        byte[] buffer = new byte[6];
        int read = base64InputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(5, read);
        Assert.assertArrayEquals("Invalid output", new byte[]{'A', 'B', 'C', 0, 'D'}, buffer);
    }

    /**
     * Test read method with decode argument and invalid input
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testReadDecodeConstructorInvalidInput() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{'A', 'B'}), false);
        byte[] buffer = new byte[3];
        int read = base64InputStream.read(buffer, 0, buffer.length);
    }

    /**
     * Test markSupported method
     * @throws IOException
     */
    @Test
    public void testMarkSupported() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        Assert.assertFalse(base64InputStream.markSupported());
    }
}