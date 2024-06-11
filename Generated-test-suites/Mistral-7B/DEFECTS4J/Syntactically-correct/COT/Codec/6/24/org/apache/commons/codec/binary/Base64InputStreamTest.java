package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Test;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    /**
     * Test constructor 1.
     */
    @Test
    public void testConstructor1() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertEquals(Base64InputStream.class, base64InputStream.getClass());
    }

    /**
     * Test constructor 2.
     */
    @Test
    public void testConstructor2() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        assertEquals(Base64InputStream.class, base64InputStream.getClass());
        assertFalse(base64InputStream.doEncode);
    }

    /**
     * Test constructor 3.
     */
    @Test
    public void testConstructor3() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        assertEquals(Base64InputStream.class, base64InputStream.getClass());
        assertFalse(base64InputStream.doEncode);
    }

    /**
     * Test constructor 4.
     */
    @Test
    public void testConstructor4() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41 });
        Base64 base64 = new Base64(76, new byte[] { 0x0D, 0x0A });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, new byte[] { 0x0D, 0x0A });
        assertEquals(Base64InputStream.class, base64InputStream.getClass());
        assertFalse(base64InputStream.doEncode);
        assertEquals(76, base64InputStream.base64.getLineLength());
        assertArrayEquals(new byte[] { 0x0D, 0x0A }, base64InputStream.base64.getLineSeparator());
    }

    /**
     * Test read method 1.
     */
    @Test
    public void testRead1() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int result = base64InputStream.read();
        assertEquals(0x41 & 0xFF, result);
    }

    /**
     * Test read method 2.
     */
    @Test
    public void testRead2() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int result1 = base64InputStream.read();
        int result2 = base64InputStream.read();
        assertEquals(0x41 & 0xFF, result1);
        assertEquals(0x42 & 0xFF, result2);
    }

    /**
     * Test read method 3.
     */
    @Test
    public void testRead3() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int result1 = base64InputStream.read();
        int result2 = base64InputStream.read();
        int result3 = base64InputStream.read();
        assertEquals(0x41 & 0xFF, result1);
        assertEquals(0x42 & 0xFF, result2);
        assertEquals(-1, result3);
    }

    /**
     * Test read method 4.
     */
    @Test
    public void testRead4() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[10];
        int length = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(2, length);
        assertEquals(0x41 & 0xFF, buffer[0]);
        assertEquals(0x42 & 0xFF, buffer[1]);
    }

    /**
     * Test read method 5.
     */
    @Test
    public void testRead5() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[1];
        int length = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(1, length);
        assertEquals(0x41 & 0xFF, buffer[0]);
    }

    /**
     * Test read method 6.
     */
    @Test
    public void testRead6() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[1];
        int length = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(1, length);
        assertEquals(-1, base64InputStream.read(buffer, 0, buffer.length));
    }

    /**
     * Test markSupported method.
     */
    @Test
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertFalse(base64InputStream.markSupported());
    }

    /**
     * Test read method with decoding.
     */
    @Test
    public void testReadWithDecoding() throws IOException {
        String testData = "VG8gZXhhbXBsZSBoYXZlIGlzIGh1bWFuLCB3aGljaCB3ZSBmaXhlIGFuZCBhbmQgc291cmNlIGRhdGFbMF0gKw==";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] buffer = new byte[5];
        int length = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(3, length);
        String result = new String(buffer, 0, length);
        assertEquals("PGp", result);
    }

    /**
     * Test read method with encoding.
     */
    @Test
    public void testReadWithEncoding() throws IOException {
        String testData = "PGplbmRvYmo=";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] buffer = new byte[4];
        int length = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(3, length);
        String result = new String(buffer, 0, length);
        assertEquals("PGp", result);
    }

    /**
     * Test read method with invalid line length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadWithInvalidLineLength() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64 base64 = new Base64(-1, new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, -1, new byte[0]);
    }

    /**
     * Test read method with null line separator.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadWithNullLineSeparator() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64 base64 = new Base64(76, null);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, null);
    }

    /**
     * Test read method with empty data.
     */
    @Test
    public void testReadWithEmptyData() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        int length = base64InputStream.read(new byte[1], 0, 1);
        assertEquals(0, length);
    }
}