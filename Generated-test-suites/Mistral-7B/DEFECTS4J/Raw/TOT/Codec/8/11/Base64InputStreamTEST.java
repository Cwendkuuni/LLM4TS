package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

    private Base64OutputStream output;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @BeforeEach
    public void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        output = new Base64OutputStream(byteArrayOutputStream, false);
    }

    /**
     * Test encoding and decoding of simple data.
     */
    @Test
    public void testSimpleData() throws IOException {
        byte[] data = "test".getBytes(StandardCharsets.UTF_8);
        output.write(data, 0, data.length);
        output.close();

        byteArrayInputStream = new Base64InputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), false);
        byte[] result = new byte[data.length];
        int numRead = base64InputStream.read(result, 0, data.length);
        Assertions.assertArrayEquals(data, result);
        byteArrayInputStream.close();
    }

    /**
     * Test encoding and decoding of data with line length > 76.
     */
    @Test
    public void testLargeLineLength() throws IOException {
        byte[] data = new byte[1024 * 1024];
        output.write(data, 0, data.length);
        output.close();

        byteArrayInputStream = new Base64InputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), 8192, null);
        byte[] result = new byte[data.length];
        int numRead = base64InputStream.read(result, 0, data.length);
        Assertions.assertArrayEquals(data, result);
        byteArrayInputStream.close();
    }

    /**
     * Test decoding of data with invalid characters.
     */
    @Test
    public void testInvalidData() {
        byte[] data = new byte[]{'A', 'B', 'C', (byte) 0x80};
        Assertions.assertThrows(IOException.class, () -> {
            byteArrayOutputStream = new ByteArrayOutputStream();
            output = new Base64OutputStream(byteArrayOutputStream, false);
            output.write(data, 0, data.length);
            output.close();

            byteArrayInputStream = new Base64InputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), false);
            byte[] result = new byte[data.length];
            int numRead = base64InputStream.read(result, 0, data.length);
        });
    }

    /**
     * Test decoding of data with trailing whitespace.
     */
    @Test
    public void testTrailingWhitespace() throws IOException {
        byte[] data = "test\r\n".getBytes(StandardCharsets.UTF_8);
        byte[] expectedData = Hex.decodeHex("4d657374200d0a"); // hex representation of "test.\r\n"

        output.write(data, 0, data.length);
        output.close();

        byteArrayInputStream = new Base64InputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), false);
        byte[] result = new byte[data.length];
        int numRead = base64InputStream.read(result, 0, data.length);
        Assertions.assertArrayEquals(expectedData, result);
        byteArrayInputStream.close();
    }

    /**
     * Test encoding and decoding of empty data.
     */
    @Test
    public void testEmptyData() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        output.write(new byte[0][], 0, 0);
        output.close();

        byteArrayInputStream = new Base64InputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), false);
        byte[] result = new byte[0];
        int numRead = base64InputStream.read(result, 0, result.length);
        Assertions.assertArrayEquals(new byte[0], result);
        byteArrayInputStream.close();
    }

    /**
     * Test encoding and decoding of data with EOF.
     */
    @Test
    public void testEof() throws IOException {
        output.write(new byte[]{'A'});
        output.close();

        byteArrayInputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), false);
        byte[] result = new byte[1];
        int numRead = base64InputStream.read(result, 0, result.length);
        Assertions.assertTrue(numRead == -1);
        byteArrayInputStream.close();
    }

    /**
     * Test encoding and decoding of data with doEncode = true.
     */
    @Test
    public void testEncoding() throws IOException {
        byte[] data = "test".getBytes(StandardCharsets.UTF_8);
        byteArrayOutputStream = new ByteArrayOutputStream();
        output = new Base64OutputStream(byteArrayOutputStream, true);
        output.write(data, 0, data.length);
        output.close();

        byteArrayInputStream = new Base64InputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), false);
        byte[] encodedData = new byte[data.length * 4 / 3 + 4]; // Base64 encoded data is typically 33% larger than original data
        int numRead = base64InputStream.read(encodedData, 0, encodedData.length);
        Assertions.assertArrayEquals(base64.decode(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size()), encodedData);
        byteArrayInputStream.close();
    }
}