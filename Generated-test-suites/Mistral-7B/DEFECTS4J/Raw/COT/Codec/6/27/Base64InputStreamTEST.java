package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
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
 * Base64InputStreamTest
 */
public class Base64InputStreamTest {

    /*
     * Test methods for Base64InputStream class
     */

    /**
     * Test constructor Base64InputStream
     */
    @Test
    public void testBase64InputStream() {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}));
        assertEquals(false, inputStream.doEncode);
        assertFalse(inputStream.markSupported());
    }

    /**
     * Test constructor Base64InputStream(InputStream in)
     */
    @Test
    public void testBase64InputStreamInputStream() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] testData = new byte[]{'A', 'B', 'C', 'D', 'E'};
        outputStream.write(testData);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertEquals(false, base64InputStream.doEncode);
        assertFalse(base64InputStream.markSupported());

        byte[] decodedData = new byte[inputStream.available()];
        int bytesRead = base64InputStream.read(decodedData, 0, decodedData.length);
        assertArrayEquals(testData, decodedData);
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode)
     */
    @Test
    public void testBase64InputStreamInputStreamBoolean() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] testData = new byte[]{'A', 'B', 'C', 'D', 'E'};
        outputStream.write(testData);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        assertEquals(true, base64InputStream.doEncode);
        assertFalse(base64InputStream.markSupported());

        ByteArrayOutputStream encodedOutputStream = new ByteArrayOutputStream();
        Base64 base64 = new Base64();
        base64.encode(testData, 0, testData.length, encodedOutputStream.toByteArray(), 0);

        byte[] encodedData = encodedOutputStream.toByteArray();
        byte[] decodedData = new byte[inputStream.available()];
        int bytesRead = base64InputStream.read(decodedData, 0, decodedData.length);
        assertArrayEquals(testData, decodedData);
        assertArrayEquals(encodedData, base64InputStream.toByteArray());
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator)
     */
    @Test
    public void testBase64InputStreamInputStreamBooleanIntByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] testData = new byte[]{'A', 'B', 'C', 'D', 'E'};
        outputStream.write(testData);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64 base64 = new Base64(72);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 72, new byte[]{'\r', '\n'});
        assertEquals(true, base64InputStream.doEncode);
        assertFalse(base64InputStream.markSupported());

        ByteArrayOutputStream encodedOutputStream = new ByteArrayOutputStream();
        base64.encode(testData, 0, testData.length, encodedOutputStream.toByteArray(), 0, 72, new byte[]{'\r', '\n'});

        byte[] encodedData = encodedOutputStream.toByteArray();
        byte[] decodedData = new byte[inputStream.available()];
        int bytesRead = base64InputStream.read(decodedData, 0, decodedData.length);
        assertArrayEquals(testData, decodedData);
        assertArrayEquals(encodedData, base64InputStream.toByteArray());
    }

    /**
     * Test method read
     */
    @Test
    public void testRead() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] testData = new byte[]{'A', 'B', 'C', 'D', 'E'};
        outputStream.write(testData);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] singleByte = new byte[1];
        byte[] decodedData = new byte[inputStream.available()];

        int firstByteRead = base64InputStream.read(singleByte, 0, 1);
        assertEquals(testData[0], singleByte[0]);
        assertEquals(firstByteRead, 1);

        int bytesRead = base64InputStream.read(decodedData, 0, decodedData.length);
        assertArrayEquals(testData, decodedData);
        assertEquals(bytesRead, decodedData.length);

        bytesRead = base64InputStream.read(decodedData, decodedData.length - 1, 1);
        assertEquals(testData[testData.length - 1], decodedData[decodedData.length - 1]);
        assertEquals(bytesRead, 1);
    }

    /**
     * Test method read
     */
    @Test(expected = IOException.class)
    public void testReadEmptyInputStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] singleByte = new byte[1];
        base64InputStream.read(singleByte, 0, 1);
    }

    /**
     * Test method read
     */
    @Test(expected = IOException.class)
    public void testReadInvalidBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] testData = new byte[]{'A', (byte) 0x7F, 'C', 'D', 'E'};
        outputStream.write(testData);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] singleByte = new byte[1];
        base64InputStream.read(singleByte, 0, 1);
        int readByte = singleByte[0] & 0xFF;
        assertEquals(readByte, testData[0] | 0x80);

        base64InputStream.read(singleByte, 0, 1);
        readByte = singleByte[0] & 0xFF;
        assertEquals(readByte, testData[1] & 0x7F);

        base64InputStream.read(singleByte, 0, 1);
        readByte = singleByte[0] & 0xFF;
        assertEquals(readByte, testData[2]);

        base64InputStream.read(singleByte, 0, 1);
        readByte = singleByte[0] & 0xFF;
        assertEquals(readByte, testData[3]);

        base64InputStream.read(singleByte, 0, 1);
        readByte = singleByte[0] & 0xFF;
        assertEquals(readByte, testData[4]);

        base64InputStream.read(singleByte, 0, 1);
        readByte = singleByte[0] & 0xFF;
        assertEquals(-1, readByte);
    }

    /**
     * Test method read
     */
    @Test
    public void testReadLineLength() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] testData = new byte[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};
        outputStream.write(testData);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64 base64 = new Base64(60);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 60, null);

        byte[] decodedData = new byte[testData.length];
        int bytesRead = base64InputStream.read(decodedData, 0, decodedData.length);
        assertArrayEquals(testData, decodedData);
        assertEquals(bytesRead, decodedData.length);

        byte[] buffer = new byte[bytesRead + 1];
        int length = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, length);
    }

    /**
     * Test method read
     */
    @Test
    public void testReadLineSeparator() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] testData = new byte[]{'A', 'B', 'C', 'D', 'E'};
        outputStream.write(testData);
        outputStream.write('\r');
        outputStream.write('\n');
        outputStream.write('F');
        outputStream.write('G');
        outputStream.write('H');
        outputStream.write('I');
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64 base64 = new Base64(72, new byte[]{'\\r', '\\n'});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 72, new byte[]{'\\r', '\\n'});

        byte[] decodedData = new byte[testData.length + 2];
        int bytesRead = base64InputStream.read(decodedData, 0, decodedData.length);
        assertArrayEquals(testData, decodedData, 0, 5);
        assertEquals(bytesRead, decodedData.length);

        assertEquals('\r', decodedData[5]);
        assertEquals('\n', decodedData[6]);
    }

    /**
     * Test method markSupported
     */
    @Test
    public void testMarkSupported() {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}));
        assertFalse(inputStream.markSupported());
    }

    /**
     * Test method markSupported
     */
    @Test
    public void testMarkSupportedWithMark() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] testData = new byte[]{'A', 'B', 'C', 'D', 'E'};
        outputStream.write(testData);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        base64InputStream.mark(1);
        assertEquals(true, base64InputStream.markSupported());
        assertEquals(1, base64InputStream.markLimit());
        assertEquals(true, base64InputStream.markSupported());
        assertEquals(-1, base64InputStream.read());
        assertEquals(testData[0], base64InputStream.read());
        assertEquals(testData[1], base64InputStream.read());
        assertEquals(testData[2], base64InputStream.read());
        assertEquals(testData[3], base64InputStream.read());
        assertEquals(testData[4], base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
        assertEquals(false, base64InputStream.markSupported());
    }

}