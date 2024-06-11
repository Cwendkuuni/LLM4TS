package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

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
     * @throws IOException
     */
    @Test
    public void testBase64InputStream_InputStream() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64Input = new Base64InputStream(input);
        // Basic functionality test
        byte[] expected = new byte[] { 0x42, 0x65, 0x73, 0x74, 0x61, 0x72, 0x74, 0x65 };
        byte[] actual = new byte[8];
        int read = base64Input.read(actual, 0, 8);
        assertArrayEquals("Read incorrect data", expected, actual);
        assertEquals("Read incorrect number of bytes", read, 8);
        // Edge case test - EOF
        byte[] eofTest = new byte[1];
        int eofRead = base64Input.read(eofTest, 0, 1);
        assertEquals("Unexpected data read after EOF", -1, eofRead);
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode)
     * @throws IOException
     */
    @Test
    public void testBase64InputStream_InputStream_boolean() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64Input = new Base64InputStream(input, true);
        // Basic functionality test
        byte[] expected = new byte[] { 0x42, 0x65, 0x73, 0x73, 0x61, 0x6d, 0x73, 0x73, 0x61, 0x72, 0x74, 0x61, 0x6b, 0x65, 0x6e, 0x63, 0x74, 0x72, 0x69, 0x6e, 0x67, 0x0A };
        byte[] actual = new byte[17];
        int read = base64Input.read(actual, 0, 17);
        assertArrayEquals("Read incorrect data", expected, actual);
        assertEquals("Read incorrect number of bytes", read, 17);
        // Edge case test - EOF
        byte[] eofTest = new byte[1];
        int eofRead = base64Input.read(eofTest, 0, 1);
        assertEquals("Unexpected data read after EOF", -1, eofRead);
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator)
     * @throws IOException
     */
    @Test
    public void testBase64InputStream_InputStream_boolean_int_byteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64Input = new Base64InputStream(input, true, 76, new byte[] { 0x0A, 0x0D });
        // Basic functionality test
        byte[] expected = new byte[] { 0x42, 0x65, 0x73, 0x73, 0x61, 0x6d, 0x73, 0x3A, 0x2F, 0x2F, 0x42, 0x65, 0x73, 0x73, 0x61, 0x6d, 0x2D, 0x65, 0x78, 0x70, 0x6F, 0x72, 0x74, 0x61, 0x6B, 0x65, 0x6E, 0x63, 0x74, 0x72, 0x69, 0x6E, 0x67, 0x0A };
        byte[] actual = new byte[25];
        int read = base64Input.read(actual, 0, 25);
        assertArrayEquals("Read incorrect data", expected, actual);
        assertEquals("Read incorrect number of bytes", read, 25);
        // Edge case test - EOF
        byte[] eofTest = new byte[1];
        int eofRead = base64Input.read(eofTest, 0, 1);
        assertEquals("Unexpected data read after EOF", -1, eofRead);
    }

    /**
     * Test method read()
     * @throws IOException
     */
    @Test
    public void testRead() throws IOException {
        // Test Base64InputStream(InputStream in)
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64Input = new Base64InputStream(input);
        byte[] expected = new byte[] { 0x42, 0x65, 0x73, 0x74, 0x61, 0x72, 0x74, 0x65 };
        byte[] actual = new byte[8];
        int read = base64Input.read(actual, 0, 8);
        assertArrayEquals("Read incorrect data", expected, actual);
        assertEquals("Read incorrect number of bytes", read, 8);

        // Test Base64InputStream(InputStream in, boolean doEncode)
        Base64InputStream base64InputEncoded = new Base64InputStream(input, true);
        byte[] expectedEncoded = new byte[] { 0x42, 0x65, 0x73, 0x73, 0x61, 0x6d, 0x73, 0x3A, 0x2F, 0x2F, 0x42, 0x65, 0x73, 0x73, 0x61, 0x6d, 0x2D, 0x65, 0x78, 0x70, 0x6F, 0x72, 0x74, 0x61, 0x6B, 0x65, 0x6E, 0x63, 0x74, 0x72, 0x69, 0x6E, 0x67, 0x0A };
        byte[] actualEncoded = new byte[22];
        int readEncoded = base64InputEncoded.read(actualEncoded, 0, 22);
        assertArrayEquals("Read incorrect data", expectedEncoded, actualEncoded);
        assertEquals("Read incorrect number of bytes", readEncoded, 22);

        // Test Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator)
        InputStream inputEncoded = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
        Base64InputStream base64InputEncodedLineSep = new Base64InputStream(inputEncoded, true, 76, new byte[] { 0x0A, 0x0D });
        byte[] expectedEncodedLineSep = new byte[] { 0x56, 0x53, 0x61, 0x72, 0x79, 0x41, 0x67, 0x65, 0x74, 0x53, 0x61, 0x72, 0x79, 0x65, 0x65, 0x72, 0x41, 0x67, 0x65, 0x72, 0x45, 0x6C, 0x69, 0x6E, 0x65, 0x64, 0x49, 0x6E, 0x74, 0x65, 0x72, 0x6E, 0x61, 0x74, 0x65, 0x64, 0x72, 0x65, 0x64, 0x2D, 0x65, 0x78, 0x70, 0x6F, 0x72, 0x74, 0x61, 0x6B, 0x65, 0x6E, 0x63, 0x74, 0x72, 0x69, 0x6E, 0x67, 0x0A, 0x49, 0x6E, 0x74, 0x65, 0x72, 0x6E, 0x61, 0x74, 0x65, 0x64, 0x72, 0x65, 0x64, 0x2D, 0x65, 0x78, 0x70, 0x6F, 0x72, 0x74, 0x61, 0x6B, 0x65, 0x6E, 0x63, 0x74, 0x72, 0x69, 0x6E, 0x67, 0x0A };
        byte[] actualEncodedLineSep = new byte[85];
        int readEncodedLineSep = base64InputEncodedLineSep.read(actualEncodedLineSep, 0, 85);
        assertArrayEquals("Read incorrect data", expectedEncodedLineSep, actualEncodedLineSep);
        assertEquals("Read incorrect number of bytes", readEncodedLineSep, 85);
    }

    /**
     * Test method markSupported()
     */
    @Test
    public void testMarkSupported() {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64Input = new Base64InputStream(input);
        assertFalse("Unexpected mark support", base64Input.markSupported());
    }
}