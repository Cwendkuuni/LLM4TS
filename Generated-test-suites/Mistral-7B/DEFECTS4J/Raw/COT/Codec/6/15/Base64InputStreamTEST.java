package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Test;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    /**
     * Test constructor Base64InputStream(InputStream in)
     */
    @Test
    public void testBase64InputStreamConstructor1() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x68, 0x65, 0x61, 0x64, 0x65, 0x63, 0x74, 0x32,
                0x30, 0x31, 0x34, (byte) 0xC3, 0x31, 0x35, 0x32, 0x36, 0x37, 0x38, 0x39 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // Verify instance variables
        assertEquals(false, base64InputStream.doEncode);
        assertSame(new Base64(false), base64InputStream.base64);

        // Test read method
        byte[] result = new byte[12];
        int read = base64InputStream.read(result, 0, result.length);
        assertEquals(result.length, read);
        assertArrayEquals(new byte[] { 0x65, 0x65, 0x32, 0x30, 0x31, 0x34, 0x31, (byte) 0x83, 0x31, 0x35, 0x32 }, result);

        // Test read method with EOF
        byte[] result2 = new byte[12];
        int read2 = base64InputStream.read(result2, 0, result2.length);
        assertEquals(0, read2);
        assertArrayEquals(new byte[] {}, result2);

        // Test markSupported method
        assertFalse(base64InputStream.markSupported());

        // Test close method
        base64InputStream.close();
        assertNull(base64InputStream.in);
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode)
     */
    @Test
    public void testBase64InputStreamConstructor2() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x68, 0x65, 0x61, 0x64, 0x65, 0x63, 0x74, 0x32,
                0x30, 0x31, 0x34, (byte) 0xC3, 0x31, 0x35, 0x32, 0x36, 0x37, 0x38, 0x39 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        // Verify instance variables
        assertEquals(true, base64InputStream.doEncode);
        assertSame(new Base64(false), base64InputStream.base64);

        // Test read method
        byte[] result = new byte[12];
        int read = base64InputStream.read(result, 0, result.length);
        assertEquals(12, read);
        assertArrayEquals(new byte[] { 0x32, 0x30, 0x31, 0x34, 0x2E, 0x31, 0x35, 0x32, 0x2E, 0x36, 0x37, 0x38 }, result);

        // Test read method with EOF
        byte[] result2 = new byte[12];
        int read2 = base64InputStream.read(result2, 0, result2.length);
        assertEquals(0, read2);
        assertArrayEquals(new byte[] {}, result2);

        // Test markSupported method
        assertFalse(base64InputStream.markSupported());

        // Test close method
        base64InputStream.close();
        assertNull(base64InputStream.in);
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator)
     */
    @Test
    public void testBase64InputStreamConstructor3() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x68, 0x65, 0x61, 0x64, 0x65, 0x63, 0x74, 0x32,
                0x30, 0x31, 0x34, (byte) 0xC3, 0x31, 0x35, 0x32, 0x36, 0x37, 0x38, 0x39 });
        byte[] lineSeparator = new byte[] { 0x0D, 0x0A };
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, lineSeparator);

        // Verify instance variables
        assertEquals(true, base64InputStream.doEncode);
        assertSame(new Base64(76, lineSeparator), base64InputStream.base64);

        // Test read method
        byte[] result = new byte[12];
        int read = base64InputStream.read(result, 0, result.length);
        assertEquals(11, read); // The last byte will be incomplete and discarded
        assertArrayEquals(new byte[] { 0x32, 0x30, 0x31, 0x34, 0x2E, 0x31, 0x35, 0x32, 0x0D, 0x0A }, result, 0, 10);
        assertArrayEquals(new byte[] { (byte) 0xFF }, result, 11);

        // Test read method with EOF
        byte[] result2 = new byte[11];
        int read2 = base64InputStream.read(result2, 0, result2.length);
        assertEquals(11, read2); // The last byte will be incomplete and read
        assertArrayEquals(new byte[] { 0x32, 0x30, 0x31, 0x34, 0x2E, 0x31, 0x35, 0x32, 0x0D, 0x0A }, result2, 0, 10);
        assertArrayEquals(new byte[] { 0x2E }, result2, 10);

        // Test markSupported method
        assertFalse(base64InputStream.markSupported());

        // Test close method
        base64InputStream.close();
        assertNull(base64InputStream.in);

        // Test decode invalid Base64 data
        InputStream inputStream2 = new ByteArrayInputStream(new byte[] { 0x68, 0x65, 0x61, 0x64, 0x65, 0x63, 0x74, 0x32,
                0x30, 0x31, 0x34, 0x31, 0x35, 0x32, 0x36, 0x37, 0x38, 0x39, 0x39 });
        assertThrows(IOException.class, () -> new Base64InputStream(inputStream2, false));
    }
}