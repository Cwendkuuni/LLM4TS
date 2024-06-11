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

/**
 * Test file for {@link Base64InputStream} class
 *
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with default settings
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] expected = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        byte[] actual = new byte[10];
        int len = base64InputStream.read(actual, 0, 10);
        assertEquals(len, 10);
        assertArrayEquals(expected, actual);
    }

    /**
     * Test constructor with decode setting
     */
    @org.junit.Test
    public void testDecodeConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43, 0x3d, 0x3d, 0x3d, 0x0d, 0x0a, 0x41, 0x42, 0x43, 0x3d, 0x3d, 0x3d });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] expected = new byte[] { 0x01, 0x02, 0x03 };
        byte[] actual = new byte[3];
        int len = base64InputStream.read(actual, 0, 3);
        assertEquals(len, 3);
        assertArrayEquals(expected, actual);
    }

    /**
     * Test constructor with encode setting
     */
    @org.junit.Test
    public void testEncodeConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 });
        Base64 base64 = new Base64();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, Base64.CRLF);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] readByte =new byte[1];
        int len;
        do {
            len = base64InputStream.read(readByte, 0, 1);
            outputStream.write(readByte, 0, len < 0 ? 1 : len);
        } while (len > 0);
        byte[] expected = base64.encode(new byte[] { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08 });
        byte[] actual = outputStream.toByteArray();
        assertArrayEquals(expected, actual);
    }

    /**
     * Test constructor with encode and custom line length setting
     */
    @org.junit.Test
    public void testEncodeCustomLineLengthConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 });
        Base64 base64 = new Base64(72);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 72, Base64.CRLF);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] readByte =new byte[1];
        int len;
        do {
            len = base64InputStream.read(readByte, 0, 1);
            outputStream.write(readByte, 0, len < 0 ? 1 : len);
        } while (len > 0);
        byte[] expected = base64.encode(new byte[] { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08 });
        byte[] actual = outputStream.toByteArray();
        String expectedString = new String(expected);
        String actualString = new String(actual);
        assertEquals(expectedString.length(), actualString.length());
        for (int i = 0; i < expectedString.length(); i++) {
            assertEquals(expectedString.charAt(i), actualString.charAt(i));
        }
    }

    /**
     * Test read method with default settings
     */
    @org.junit.Test
    public void testReadDefaultSettings() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] expected = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        byte[] actual = new byte[8];
        int len = base64InputStream.read(actual, 0, 8);
        assertEquals(len, 8);
        assertArrayEquals(expected, actual);
    }

    /**
     * Test read method with decode setting
     */
    @org.junit.Test
    public void testReadDecodeSettings() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43, 0x3d, 0x3d, 0x3d, 0x0d, 0x0a, 0x41, 0x42, 0x43, 0x3d, 0x3d, 0x3d });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] expected = new byte[] { 0x01, 0x02, 0x03 };
        byte[] actual = new byte[3];
        int len = base64InputStream.read(actual, 0, 3);
        assertEquals(len, 3);
        assertArrayEquals(expected, actual);
    }

    /**
     * Test read method with encode setting
     */
    @org.junit.Test
    public void testReadEncodeSettings() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 });
        Base64 base64 = new Base64(76, Base64.CRLF);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, Base64.CRLF);
        byte[] expected = new byte[13];
        byte[] actual = new byte[13];
        int len = base64InputStream.read(actual, 0, 13);
        assertEquals(len, 13);
        assertArrayEquals(expected, actual);
    }

    /**
     * Test markSupported method
     */
    @org.junit.Test
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertFalse(base64InputStream.markSupported());
    }

    /**
     * Test close method
     */
    @org.junit.Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.close();
        assertNull(base64InputStream.in);
    }

}