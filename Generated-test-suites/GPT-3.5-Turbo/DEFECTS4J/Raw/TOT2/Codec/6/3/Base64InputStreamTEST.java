package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStream() {
        // Create a Base64InputStream and check if it is not null
        Base64InputStream inputStream = new Base64InputStream(null);
        assertNotNull(inputStream);
    }

    @Test
    public void testBase64InputStreamWithEncoding() {
        // Create a Base64InputStream with encoding flag true and check if it is not null
        Base64InputStream inputStream = new Base64InputStream(null, true);
        assertNotNull(inputStream);
    }

    @Test
    public void testReadSingleByte() {
        // Create a Base64InputStream with encoding flag true
        Base64InputStream inputStream = new Base64InputStream(null, true);

        // Try to read a single byte from the input stream and check if it is within the valid range
        int byteRead = inputStream.read();
        assertTrue(byteRead >= 0 && byteRead <= 255);
    }

    @Test
    public void testReadByteArray() {
        // Create a Base64InputStream with encoding flag false
        Base64InputStream inputStream = new Base64InputStream(null, false);

        // Try to read a byte array and check if the number of bytes read is valid
        byte[] byteArray = new byte[10];
        int bytesRead = inputStream.read(byteArray, 0, 10);
        assertEquals(10, bytesRead);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws Exception {
        // Create a Base64InputStream with encoding flag false
        Base64InputStream inputStream = new Base64InputStream(null, false);

        // Try to read into a null byte array and expect NullPointerException
        inputStream.read(null, 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws Exception {
        // Create a Base64InputStream with encoding flag false
        Base64InputStream inputStream = new Base64InputStream(null, false);

        // Try to read with an invalid offset and expect IndexOutOfBoundsException
        byte[] byteArray = new byte[10];
        inputStream.read(byteArray, 11, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws Exception {
        // Create a Base64InputStream with encoding flag false
        Base64InputStream inputStream = new Base64InputStream(null, false);

        // Try to read with invalid length and expect IndexOutOfBoundsException
        byte[] byteArray = new byte[10];
        inputStream.read(byteArray, 0, 11);
    }

    @Test
    public void testMarkSupported() {
        // Create a Base64InputStream
        Base64InputStream inputStream = new Base64InputStream(null);

        // Check if mark is supported (should return false)
        assertFalse(inputStream.markSupported());
    }
}