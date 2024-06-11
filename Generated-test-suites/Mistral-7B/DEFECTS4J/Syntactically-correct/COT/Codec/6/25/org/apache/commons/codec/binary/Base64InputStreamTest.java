package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Test;

/**
 * JUnit test class for Base64InputStream class.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor Base64InputStream(InputStream in)
     *
     * @throws IOException
     */
    @Test
    public void testBase64InputStream() throws IOException {
        InputStream input = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        assertEquals(input.available(), base64InputStream.available());
        input.close();
        base64InputStream.close();
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode)
     *
     * @throws IOException
     */
    @Test
    public void testBase64InputStreamWithEncodeFlag() throws IOException {
        InputStream input = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, true);
        byte[] expected = new byte[Base64.BASE64_STANDARD_LINE_LENGTH];
        int read = 0;
        while ((read = base64InputStream.read(expected, 0, expected.length)) != -1) {
            // For Base64InputStream with encode flag set to true, the output should be same as original input
            assertArrayEquals(input.readAllBytes(), base64InputStream.readNBytes(read));
        }
        input.close();
        base64InputStream.close();
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator)
     *
     * @throws IOException
     */
    @Test
    public void testBase64InputStreamWithEncodeFlagAndCustomLineLength() throws IOException {
        InputStream input = new ByteArrayInputStream("TestData".getBytes());
        byte[] lineSeparator = new byte[]{0x0D, 0x0A};
        Base64InputStream base64InputStream = new Base64InputStream(input, true, 50, lineSeparator);
        byte[] expected = new byte[Base64.BASE64_STANDARD_LINE_LENGTH * 2];
        int read = 0;
        while ((read = base64InputStream.read(expected, 0, expected.length)) != -1) {
            // For Base64InputStream with encode flag set to true, the output should be same as original input
            assertArrayEquals(input.readAllBytes(), base64InputStream.readNBytes(read));
            // Check line length
            byte[] readLine = new byte[Base64.BASE64_STANDARD_LINE_LENGTH];
            int index = 0;
            for (int i = 0; i < read; i += 4) {
                System.arraycopy(base64InputStream.readNBytes(4), 0, readLine, index, 4);
                index += 4;
                if (i + 4 < read) {
                    byte lineSeparatorByte = base64InputStream.read();
                    assertEquals(lineSeparatorByte, lineSeparator[0]);
                    assertEquals(base64InputStream.read(), lineSeparatorByte);
                }
            }
            assertArrayEquals(Base64.encodeBase64(input.readNBytes(50)), readLine);
        }
        input.close();
        base64InputStream.close();
    }

    /**
     * Test method read()
     *
     * @throws IOException
     */
    @Test
    public void testRead() throws IOException {
        InputStream input = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte b;
        // Test read() returns EOF when the end of stream is reached
        assertFalse(base64InputStream.read() > -1);
        b = (byte) base64InputStream.read();
        assertEquals((byte) 'T', b);
        b = (byte) base64InputStream.read();
        assertEquals((byte) 'e', b);
        b = (byte) base64InputStream.read();
        assertEquals((byte) 's', b);
        b = (byte) base64InputStream.read();
        assertEquals((byte) 't', b);
        b = (byte) base64InputStream.read();
        assertEquals((byte) 'D', b);
        b = (byte) base64InputStream.read();
        assertFalse(base64InputStream.read() > -1);
        base64InputStream.close();
        input.close();
    }

    /**
     * Test method read(byte[] b, int off, int len)
     *
     * @throws IOException
     */
    @Test
    public void testReadByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] buffer = new byte[5];
        int read = base64InputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"T", "e", "s", "t", "D"}, buffer);
        assertEquals(5, read);
        read = base64InputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[0], buffer);
        assertEquals(-1, read);
        base64InputStream.close();
        input.close();
    }

    /**
     * Test method markSupported()
     *
     * @throws IOException
     */
    @Test
    public void testMarkSupported() throws IOException {
        InputStream input = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        assertFalse(base64InputStream.markSupported());
        base64InputStream.close();
        input.close();
    }

    /**
     * Test method readNBytes()
     *
     * @throws IOException
     */
    private byte[] readNBytes(Base64InputStream inputStream, int length) throws IOException {
        byte[] buffer = new byte[length];
        int bytesRead = inputStream.read(buffer, 0, length);
        if (bytesRead == length) {
            return buffer;
        } else {
            byte[] result = new byte[bytesRead];
            System.arraycopy(buffer, 0, result, 0, bytesRead);
            return result;
        }
    }

}