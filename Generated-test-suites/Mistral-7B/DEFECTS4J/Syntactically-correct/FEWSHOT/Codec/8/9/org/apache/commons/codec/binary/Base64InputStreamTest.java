package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {

    /**
     * Tests default constructor.
     *
     * @throws IOException
     */
    @Test
    public void testDefaultConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x2a, 0x2a, (byte) 0x61, (byte) 0x62,
                0x63 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] readBuffer = new byte[5];
        int readLength = base64InputStream.read(readBuffer);
        assertArrayEquals(new byte[] { 0x30, 0x31, 0x32, 0x33, 0x0 }, readBuffer);
        assertEquals(5, readLength);
    }

    /**
     * Tests constructor with doEncode set to true.
     *
     * @throws IOException
     */
    @Test
    public void testEncodeConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x61, 0x62, 0x63 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] readBuffer = new byte[5];
        int readLength = base64InputStream.read(readBuffer);
        assertArrayEquals(new byte[] { 0x31, 0x32, 0x33, 0x3d, 0x3d }, readBuffer);
        assertEquals(5, readLength);
    }

    /**
     * Tests constructor with doEncode set to false.
     *
     * @throws IOException
     */
    @Test
    public void testDecodeConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x31, 0x32, 0x33, 0x3d, 0x3d });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] readBuffer = new byte[5];
        int readLength = base64InputStream.read(readBuffer);
        assertArrayEquals(new byte[] { 0x61, 0x62, 0x63 }, readBuffer);
        assertEquals(3, readLength);
    }

    /**
     * Tests constructor with doEncode set to false, lineLength and lineSeparator.
     *
     * @throws IOException
     */
    @Test
    public void testDecodeConstructorWithLineLimitations() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x31, 0x32, 0x33, 0x3d, 0x3d, 0x0d, 0x0a,
                0x34, 0x35, 0x36, 0x3d, 0x3d, 0x0d, 0x0a });
        byte[] lineSeparator = new byte[] { 0x0d, 0x0a };
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 72, lineSeparator);
        byte[] readBuffer = new byte[11];
        int readLength = base64InputStream.read(readBuffer);
        assertArrayEquals(new byte[] { 0x31, 0x32, 0x33, 0x0d, 0x0a, 0x34, 0x35, 0x36, 0x3d, 0x3d }, readBuffer);
        assertEquals(10, readLength);
    }

    /**
     * Tests read method when no data is available.
     *
     * @throws IOException
     */
    @Test
    public void testReadWhenNoDataAvailable() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] readBuffer = new byte[5];
        int readLength = base64InputStream.read(readBuffer);
        assertArrayEquals(new byte[] {}, readBuffer);
        assertEquals(-1, readLength);
    }

    /**
     * Tests markSupported method.
     */
    @Test
    public void testMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(null, false);
        assertFalse(base64InputStream.markSupported());
    }

}