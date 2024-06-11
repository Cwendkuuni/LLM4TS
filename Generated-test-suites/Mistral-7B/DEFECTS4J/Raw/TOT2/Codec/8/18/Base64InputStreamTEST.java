package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor 1 - without encoding.
     */
    @Test
    public void testConstructor1() {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // Check functionality
        byte[] buffer = new byte[10];
        int length = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(length, Base64InputStream.DEFAULT_BUFFER_SIZE);
        assertArrayEquals(new byte[] { 'T', 'e', 's', 't', 'D', 'a', 't', 'a' }, buffer, 0, length);

        // Test edge cases and exception handling
        assertFalse(base64InputStream.markSupported());
        byte[] emptyBuffer = new byte[0];
        try {
            base64InputStream.read(emptyBuffer, 0, 1);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        try {
            base64InputStream.read(null, 0, 1);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    /**
     * Test constructor 2 - with encoding.
     */
    @Test
    public void testConstructor2() {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        // Check functionality
        byte[] buffer = new byte[10];
        int length = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(length, Base64InputStream.DEFAULT_BUFFER_SIZE);
        assertArrayEquals(new byte[] { 'T', 'e', 's', 't', 'D', 'a', 't', 'a' }, buffer, 0, length);

        // Test edge cases and exception handling
        assertFalse(base64InputStream.markSupported());
        byte[] emptyBuffer = new byte[0];
        try {
            base64InputStream.read(emptyBuffer, 0, 1);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        try {
            base64InputStream.read(null, 0, 1);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test decoding of invalid Base64 encoded data
        byte[] invalidBase64 = new byte[] { 'T', 'e', 's', 't', 'D', (byte) 0x80, 'a', 't', 'a' }; // Invalid character added
        try {
            int len = base64InputStream.read(invalidBase64, 0, invalidBase64.length);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        assertThrows(IllegalArgumentException.class, () -> new Base64InputStream(new ByteArrayInputStream(invalidBase64), true));
    }

    /**
     * Test constructor 3 - with encoding and custom line length and line separator.
     */
    @Test
    public void testConstructor3() {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] lineSeparator = new byte[] { (byte) 0x0D, (byte) 0x0A }; // CRLF
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 50, lineSeparator);

        // Check functionality
        byte[] buffer = new byte[10];
        int length = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(length, 50);
        assertArrayEquals(new byte[] { 'T', 'e', 's', 't' }, buffer, 0, length);

        // Test edge cases and exception handling
        assertFalse(base64InputStream.markSupported());
        byte[] emptyBuffer = new byte[0];
        try {
            base64InputStream.read(emptyBuffer, 0, 1);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        try {
            base64InputStream.read(null, 0, 1);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test decoding of large Base64 encoded data
        ByteArrayOutputStream largeOutputStream = new ByteArrayOutputStream();
        InputStream largeInputStream = new ByteArrayInputStream("TestData".getBytes());
        base64InputStream = new Base64InputStream(largeInputStream, false, 50, lineSeparator);
        byte[] largeBuffer = new byte[1000];
        int largeLength = base64InputStream.read(largeBuffer, 0, largeBuffer.length);
        assertEquals(largeLength, largeBuffer.length);

        // Test invalid line length
        assertThrows(IllegalArgumentException.class, () -> new Base64InputStream(new ByteArrayInputStream("TestData".getBytes()), false, -1, lineSeparator));

        // Test invalid line separator
        assertThrows(IllegalArgumentException.class, () -> new Base64InputStream(new ByteArrayInputStream("TestData".getBytes()), false, 50, null));
    }
}