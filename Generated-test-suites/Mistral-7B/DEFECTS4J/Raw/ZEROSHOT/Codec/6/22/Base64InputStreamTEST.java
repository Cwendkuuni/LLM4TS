package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
 * Test class for {@link Base64InputStream}
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with default settings
     */
    @Test
    public void testDefaultConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 });
        Base64InputStream inputStream = new Base64InputStream(input);
        byte[] expected = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 };
        byte[] actual = new byte[10];
        int read = inputStream.read(actual);
        assertEquals("Read incorrect number of bytes", 10, read);
        assertArrayEquals("Data not decoded correctly", expected, actual);
    }

    /**
     * Test constructor with encode flag set
     */
    @Test
    public void testEncodeConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 });
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream outputStream = new Base64OutputStream(output);
        byte[] inputData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 };
        outputStream.write(inputData);
        outputStream.flush();
        byte[] expected = output.toByteArray();
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(expected), true);
        byte[] actual = new byte[10];
        int read = inputStream.read(actual);
        assertEquals("Read incorrect number of bytes", 10, read);
        assertArrayEquals("Data not encoded and decoded correctly", inputData, actual);
    }

    /**
     * Test constructor with custom line length and line separator
     */
    @Test
    public void testCustomConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 });
        byte[] lineSeparator = new byte[] { (byte) 0x0D, (byte) 0x0A };
        Base64InputStream inputStream = new Base64InputStream(input, true, 72, lineSeparator);
        byte[] expected = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 };
        byte[] actual = new byte[10];
        int read = inputStream.read(actual);
        assertEquals("Read incorrect number of bytes", 10, read);
        assertArrayEquals("Data not decoded correctly", expected, actual);
    }

    /**
     * Test that markSupported method returns false
     */
    @Test
    public void testMarkSupported() {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 });
        Base64InputStream inputStream = new Base64InputStream(input);
        assertFalse("markSupported should return false", inputStream.markSupported());
    }
}