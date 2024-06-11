package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Test;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with default doEncode value of false.
     */
    @Test
    public void testBase64InputStreamDefaultConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertFalse(base64InputStream.isEncoding());
        assertEquals(Base64.DEFAULT, base64InputStream.getBase64());
    }

    /**
     * Test constructor with doEncode value of true.
     */
    @Test
    public void testBase64InputStreamWithEncodingTrue() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        assertTrue(base64InputStream.isEncoding());
        assertEquals(Base64.DEFAULT, base64InputStream.getBase64());
    }

    /**
     * Test constructor with doEncode value of false and custom Base64 instance.
     */
    @Test
    public void testBase64InputStreamWithCustomBase64() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64 base64 = new Base64(128, new byte[2]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 128, new byte[2]);
        assertFalse(base64InputStream.isEncoding());
        assertEquals(base64, base64InputStream.getBase64());
    }

    /**
     * Test constructor with doEncode value of true and custom Base64 instance.
     */
    @Test
    public void testBase64InputStreamWithCustomBase64AndEncodingTrue() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64 base64 = new Base64(128, new byte[2]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 128, new byte[2]);
        assertTrue(base64InputStream.isEncoding());
        assertEquals(base64, base64InputStream.getBase64());
    }

    /**
     * Test read method with default Base64InputStream instance.
     * @throws IOException
     */
    @Test
    public void testReadWithDefaultBase64InputStream() throws IOException {
        String inputBase64Data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        InputStream inputStream = new ByteArrayInputStream(inputBase64Data.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] expectedOutput = new byte[] { 1, 2, 3, 4 };
        byte[] output = new byte[4];
        int read = base64InputStream.read(output, 0, output.length);
        assertEquals(read, 4);
        assertArrayEquals(expectedOutput, output);
    }

    /**
     * Test read method with encoding set to true.
     * @throws IOException
     */
    @Test
    public void testReadWithBase64InputStreamEncoded() throws IOException {
        String inputData = "Hello, World!";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base64OutputStream bos = new Base64OutputStream(baos);
        bos.write(inputData.getBytes());
        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] expectedOutput = new byte[] { 'H', 'e', 'l', 'l', 'o', ',' };
        byte[] output = new byte[6];
        int read = base64InputStream.read(output, 0, output.length);
        assertEquals(read, 6);
        assertArrayEquals(expectedOutput, output);
    }

    /**
     * Test read method with different lineLength and lineSeparator values.
     * @throws IOException
     */
    @Test
    public void testReadWithCustomBase64InputStream() throws IOException {
        String inputBase64Data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        InputStream inputStream = new ByteArrayInputStream(inputBase64Data.getBytes());
        Base64 base64 = new Base64(128, new byte[] { (byte) 0x0D, (byte) 0x0A });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 128, new byte[] { (byte) 0x0D, (byte) 0x0A });
        byte[] expectedOutput = new byte[] { 1, 2, 3, 4 };
        byte[] output = new byte[4];
        int read = base64InputStream.read(output, 0, output.length);
        assertEquals(read, 4);
        assertArrayEquals(expectedOutput, output);
    }

    /**
     * Test markSupported method.
     */
    @Test
    public void testMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        assertFalse(base64InputStream.markSupported());
    }

    /**
     * Test read method when no data is available.
     * @throws IOException
     */
    @Test
    public void testReadWhenNoDataIsAvailable() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] output = new byte[1];
        int read = base64InputStream.read(output, 0, output.length);
        assertEquals(read, -1);
    }

    /**
     * Test read method when custom Base64InputStream instance does not have any data.
     * @throws IOException
     */
    @Test
    public void testReadWhenCustomBase64InputStreamHasNoData() throws IOException {
        Base64 base64 = new Base64();
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), false) {
            @Override
            public void reset() throws IOException {
                base64.reset();
            }
        };
        byte[] output = new byte[1];
        int read = base64InputStream.read(output, 0, output.length);
        assertEquals(read, -1);
    }

    /**
     * Test read method when custom Base64InputStream instance has corrupted data.
     * @throws IOException
     */
    @Test
    public void testReadWhenCustomBase64InputStreamHasCorruptedData() throws IOException {
        Base64 base64 = new Base64();
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[] { 0x00, 0x01, 0x02 }), false) {
            @Override
            public void reset() throws IOException {
                base64.reset();
            }
        };
        byte[] output = new byte[1];
        int read = base64InputStream.read(output, 0, output.length);
        assertNull(output);
        assertThrows(IOException.class, () -> base64InputStream.read(output, 0, output.length));
    }

    /**
     * Test read method when custom Base64InputStream instance has non-Base64 data.
     * @throws IOException
     */
    @Test
    public void testReadWhenCustomBase64InputStreamHasNonBase64Data() throws IOException {
        Base64 base64 = new Base64();
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[] { 0x00, 0x01, 0x02 }), false) {
            @Override
            public void reset() throws IOException {
                base64.reset();
            }
        };
        byte[] output = new byte[1];
        int read = base64InputStream.read(output, 0, output.length);
        assertEquals(read, -1);
        assertThrows(IOException.class, () -> base64InputStream.read(output, 0, output.length));
    }
}