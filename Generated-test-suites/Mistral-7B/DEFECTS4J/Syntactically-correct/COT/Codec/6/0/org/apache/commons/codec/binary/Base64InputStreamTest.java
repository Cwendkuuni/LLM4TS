package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
 * Test cases for Base64InputStream class.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor without encoding.
     */
    @Test
    public void testBase64InputStreamWithoutEncoding() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x31, 0x32, 0x33});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] actualByteArray = new byte[3];
        int length = base64InputStream.read(actualByteArray, 0, actualByteArray.length);
        assertArrayEquals(new byte[] { 0x31, 0x32, 0x33 }, actualByteArray);
        assertEquals(3, length);
    }

    /**
     * Test constructor with encoding.
     */
    @Test
    public void testBase64InputStreamWithEncoding() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x31, 0x32, 0x33 });
        Base64 base64 = new Base64();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream, false);
        inputStream.transferTo(base64OutputStream);
        byte[] encodedBytes = byteArrayOutputStream.toByteArray();
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(encodedBytes), true);
        byte[] actualByteArray = new byte[3];
        int length = base64InputStream.read(actualByteArray, 0, actualByteArray.length);
        assertArrayEquals(new byte[] { 0x31, 0x32, 0x33 }, actualByteArray);
        assertEquals(3, length);
    }

    /**
     * Test constructor with encoding, lineLength and lineSeparator.
     */
    @Test
    public void testBase64InputStreamWithEncodingLineLengthAndLineSeparator() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x31, 0x32, 0x33 });
        Base64 base64 = new Base64(76, new byte[] { (byte) 0x0D, (byte) 0x0A });
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream, false, 76, new byte[] { (byte) 0x0D, (byte) 0x0A });
        inputStream.transferTo(base64OutputStream);
        byte[] encodedBytes = byteArrayOutputStream.toByteArray();
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(encodedBytes), true, 76, new byte[] { (byte) 0x0D, (byte) 0x0A });
        byte[] actualByteArray = new byte[3];
        int length = base64InputStream.read(actualByteArray, 0, actualByteArray.length);
        assertArrayEquals(new byte[] { 0x31, 0x32, 0x33 }, actualByteArray);
        assertEquals(3, length);
    }

    /**
     * Test read method with empty input stream.
     */
    @Test
    public void testReadWithEmptyInputStream() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[] {}), false);
        byte[] actualByteArray = new byte[0];
        int length = base64InputStream.read(actualByteArray, 0, actualByteArray.length);
        assertArrayEquals(new byte[] {}, actualByteArray);
        assertEquals(0, length);
    }

    /**
     * Test read method when end of stream reached.
     */
    @Test
    public void testReadWhenEndOfStreamReached() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x31, 0x32, 0x33 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] actualByteArray = new byte[3];
        int length = base64InputStream.read(actualByteArray, 0, actualByteArray.length);
        assertArrayEquals(new byte[] { 0x31, 0x32, 0x33 }, actualByteArray);
        assertEquals(3, length);
        length = base64InputStream.read(actualByteArray, 0, actualByteArray.length);
        assertArrayEquals(new byte[] {}, actualByteArray);
        assertEquals(-1, length);
    }

    /**
     * Test read method when encoding is set to true but input stream contains non-base64 data.
     */
    @Test
    public void testReadWithNonBase64Data() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x31, 0x32, (byte) 0x80 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] actualByteArray = new byte[3];
        int length = base64InputStream.read(actualByteArray, 0, actualByteArray.length);
        assertArrayEquals(new byte[] { -1 }, actualByteArray);
        assertEquals(-1, length);
    }

    /**
     * Test markSupported method.
     */
    @Test
    public void testMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[] {}), false);
        assertFalse(base64InputStream.markSupported());
    }
}