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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
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
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x42, 0x69, 0x62, 0x62, 0x65, 0x66, 0x66, 0x65 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] expected = new byte[] { (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x2E, (byte) 0x2E, (byte) 0x2E, (byte) 0x2E, (byte) 0x2E, (byte) 0x2E };
        byte[] actual = new byte[13];
        int read = 0;

        while (read < 13) {
            read += base64InputStream.read(actual, read, 13 - read);
        }

        assertArrayEquals(expected, actual);
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode)
     */
    @Test
    public void testBase64InputStreamConstructor2() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x42, 0x69, 0x62, 0x62, 0x65, 0x66, 0x66, 0x65 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        byte[] expected = new byte[] { 0x41, 0x41, 0x41, 0x33, 0x33, 0x33, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E };
        int read = 0;

        while (read < expected.length) {
            read += base64InputStream.read(expected, read, expected.length - read);
        }

        assertEquals(expected.length, read);
        assertEquals(expected.length, outputStream.size());
        assertArrayEquals(expected, outputStream.toByteArray());

        base64InputStream.close();
        outputStream.close();
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator)
     */
    @Test
    public void testBase64InputStreamConstructor3() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x42, 0x69, 0x62, 0x62, 0x65, 0x66, 0x66, 0x65 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, new byte[] { 0x0D, 0x0A });

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        byte[] expected = new byte[] { 0x41, 0x41, 0x41, 0x33, 0x33, 0x33, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x2E, 0x0D, 0x0A, 0x0D, 0x0A, 0x0D, 0x0A, 0x0D, 0x0A, 0x0D, 0x0A, 0x0D, 0x0A };
        int read = 0;

        while (read < expected.length) {
            read += base64InputStream.read(expected, read, expected.length - read);
        }

        assertEquals(expected.length, read);
        assertEquals(expected.length, outputStream.size());
        assertArrayEquals(expected, outputStream.toByteArray());

        base64InputStream.close();
        outputStream.close();
    }

    /**
     * Test method read()
     */
    @Test
    public void testRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x42, 0x69, 0x62, 0x62, 0x65, 0x66, 0x66, 0x65 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] expected = new byte[] { 0x42, 0x69, 0x62, 0x62, 0x65, 0x66, 0x66, 0x65 };
        byte[] actual = new byte[8];
        int read = 0;

        while (read < expected.length) {
            read += base64InputStream.read(actual, read, expected.length - read);
        }

        assertArrayEquals(expected, actual);
        assertEquals(expected.length, read);

        base64InputStream.close();
    }

    /**
     * Test method read(byte[] b, int offset, int length)
     */
    @Test
    public void testReadByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x42, 0x69, 0x62, 0x62, 0x65, 0x66, 0x66, 0x65 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] expected = new byte[] { 0x42, 0x69, 0x62, 0x62, 0x65, 0x66, 0x66, 0x65 };
        byte[] actual = new byte[8];
        int read = 0;

        while (read < expected.length) {
            read += base64InputStream.read(actual, read, expected.length - read);
        }

        assertArrayEquals(expected, actual);
        assertEquals(expected.length, read);

        base64InputStream.close();
    }

    /**
     * Test method markSupported()
     */
    @Test
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x42, 0x69, 0x62, 0x62, 0x65, 0x66, 0x66, 0x65 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        assertFalse(base64InputStream.markSupported());

        base64InputStream.close();
    }

    /**
     * Test method read(byte[] b, int offset, int length) with length lesser than array size
     */
    @Test
    public void testReadByteArrayWithLessLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x42, 0x69, 0x62, 0x62, 0x65, 0x66, 0x66, 0x65 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] expected = new byte[] { 0x42, 0x69 };
        byte[] actual = new byte[2];
        int read = 0;

        while (read < expected.length) {
            read += base64InputStream.read(actual, read, expected.length - read);
        }

        assertArrayEquals(expected, actual);
        assertEquals(expected.length, read);

        base64InputStream.close();
    }

    /**
     * Test method read(byte[] b, int offset, int length) with length greater than array size
     */
    @Test
    public void testReadByteArrayWithGreaterLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x42, 0x69, 0x62, 0x62, 0x65, 0x66, 0x66, 0x65 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] expected = new byte[] { 0x42, 0x69, 0x62 };
        byte[] actual = new byte[3];
        int read = 0;

        while (read < expected.length) {
            read += base64InputStream.read(actual, read, expected.length - read);
        }

        assertArrayEquals(expected, actual);
        assertEquals(expected.length, read);

        base64InputStream.close();
    }

    /**
     * Test method read(byte[] b, int offset, int length) with EOF
     */
    @Test
    public void testReadByteArrayWithEOF() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x42, 0x69, 0x62, 0x62, 0x65, 0x66, 0x66, 0x65 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] expected = new byte[] { 0x42, 0x69, 0x62 };
        byte[] actual = new byte[3];
        int read = 0;

        while (read < expected.length) {
            read += base64InputStream.read(actual, read, expected.length - read);
        }

        assertArrayEquals(expected, actual);
        assertEquals(expected.length, read);

        byte[] b = new byte[1];
        read = base64InputStream.read(b, 0, 1);
        assertFalse(read > 0);

        base64InputStream.close();
    }

    /**
     * Test method read(byte[] b, int offset, int length) with null
     */
    @Test(expected = NullPointerException.class)
    public void testReadByteArrayWithNull() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x42, 0x69, 0x62, 0x62, 0x65, 0x66, 0x66, 0x65 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] b = null;
        int length = 1;

        int read = base64InputStream.read(b, 0, length);
    }

    /**
     * Test method read(byte[] b, int offset, int length) with negative offset
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithNegativeOffset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x42, 0x69, 0x62, 0x62, 0x65, 0x66, 0x66, 0x65 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] actual = new byte[8];
        int length = 2;
        int offset = -1;

        int read = base64InputStream.read(actual, offset, length);
    }

    /**
     * Test method read(byte[] b, int offset, int length) with negative length
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithNegativeLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x42, 0x69, 0x62, 0x62, 0x65, 0x66, 0x66, 0x65 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] actual = new byte[8];
        int offset = 0;
        int length = -1;

        int read = base64InputStream.read(actual, offset, length);
    }
}