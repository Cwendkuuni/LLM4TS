package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] buffer = new byte[10];
        int read = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, read);
    }

    /**
     * Test decoding constructor.
     */
    @Test
    public void testDecodingConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[] { (byte) 'A'});
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] buffer = new byte[10];
        int read = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(1, read);
        assertEquals('A', buffer[0]);
    }

    /**
     * Test encoding constructor.
     */
    @Test
    public void testEncodingConstructor() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { (byte) 'A'});
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, false);
        Base64InputStream base64InputStream = new Base64InputStream(input, true, Base64.NO_LINE_BREAKS, Base64.CRLF);
        int read = 0;
        while ((read = base64InputStream.read()) != -1) {
            base64OutputStream.write(read);
        }
        byte[] expected = Base64.encodeBase64String("A".getBytes()).getBytes();
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    /**
     * Test decoding and line length constructor.
     */
    @Test
    public void testDecodingAndLineLengthConstructor() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { (byte) 'A' });
        byte[] lineSeparator = new byte[] { (byte) '\r', (byte) '\n' };
        Base64InputStream base64InputStream = new Base64InputStream(input, false, 76, lineSeparator);
        byte[] buffer = new byte[15];
        int read = 0;
        while ((read = base64InputStream.read(buffer)) != -1) {
            System.out.write(buffer, 0, read);
        }
        byte[] expected = new byte[] { 'A' };
        assertArrayEquals(expected, buffer);
    }

    /**
     * Test decoding and line separator constructor.
     */
    @Test
    public void testDecodingAndLineSeparatorConstructor() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { (byte) 'A' });
        byte[] lineSeparator = new byte[] { (byte) '\r', (byte) '\n' };
        Base64InputStream base64InputStream = new Base64InputStream(input, false, Base64.NO_LINE_BREAKS, lineSeparator);
        byte[] buffer = new byte[10];
        int read = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, read);
    }

    /**
     * Test encoding constructor with invalid line length.
     */
    @Test
    public void testEncodingConstructorWithInvalidLineLength() {
        assertThrows(IllegalArgumentException.class, () -> new Base64InputStream(new ByteArrayInputStream(new byte[0]), true, -1, null));
    }

    /**
     * Test encoding constructor with null line separator.
     */
    @Test
    public void testEncodingConstructorWithNullLineSeparator() {
        assertThrows(NullPointerException.class, () -> new Base64InputStream(new ByteArrayInputStream(new byte[0]), true, Base64.NO_LINE_BREAKS, null));
    }

    /**
     * Test decoding constructor with null input stream.
     */
    @Test
    public void testDecodingConstructorWithNullInput() {
        assertThrows(NullPointerException.class, () -> new Base64InputStream(null));
    }

}