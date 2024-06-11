package org.apache.commons.codec.binary;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link Base64InputStream}
 */
public class Base64InputStreamTest {

    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;
    private Base64InputStream base64InputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream encodedOutputStream = new ByteArrayOutputStream();
        Base64 base64 = new Base64();

        String input = "This is a test for Base64InputStream.";

        // Encode the input string and write it to the encodedOutputStream
        base64.encode(input.getBytes(), encodedOutputStream.toByteArray(), false);
        outputStream.write(encodedOutputStream.toByteArray());

        // Create a ByteArrayInputStream from the encoded byte array
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        // Create a Base64InputStream for decoding
        base64InputStream = new Base64InputStream(inputStream, false);
    }

    @After
    public void tearDown() throws IOException {
        inputStream.close();
        outputStream.close();
    }

    /**
     * Test reading a single byte
     */
    @Test
    public void testReadByte() throws IOException {
        int readByte = base64InputStream.read();
        assertEquals("Expected valid byte", 'T', readByte);
    }

    /**
     * Test reading multiple bytes
     */
    @Test
    public void testReadMultipleBytes() throws IOException {
        byte[] expected = new byte[]{
                (byte) 0x42,
                (byte) 0x69,
                (byte) 0x6e,
                (byte) 0x74,
                (byte) 0x68,
                (byte) 0x65,
                (byte) 0x72,
                (byte) 0x73,
                (byte) 0x65,
                (byte) 0x61,
                (byte) 0x74,
                (byte) 0x68,
                (byte) 0x65,
                (byte) 0x72,
                (byte) 0x73,
                (byte) 0x69,
                (byte) 0x65,
                (byte) 0x6E,
                (byte) 0x64,
                (byte) 0x6C,
                (byte) 0x65,
                (byte) 0x2E
        };

        byte[] actual = new byte[expected.length];
        int bytesRead = base64InputStream.read(actual);
        assertArrayEquals("Expected to read all bytes", expected, actual);
        assertEquals("Expected number of bytes read", bytesRead, expected.length);
    }

    /**
     * Test reading multiple bytes with a specific offset and length
     */
    @Test
    public void testReadMultipleBytesWithOffsetAndLength() throws IOException {
        byte[] expected = new byte[]{
                (byte) 0x69,
                (byte) 0x6e,
                (byte) 0x6e,
                (byte) 0x61,
                (byte) 0x6d,
                (byte) 0x65
        };

        int offset = 6;
        int length = 6;
        byte[] actual = new byte[length];

        int bytesRead = base64InputStream.read(actual, offset);
        assertArrayEquals("Expected to read all bytes", expected, actual);
        assertEquals("Expected number of bytes read", bytesRead, length);
    }

    /**
     * Test markSupported method
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() throws IOException {
        base64InputStream.markSupported();
    }

    /**
     * Test creating a Base64InputStream for encoding
     */
    @Test
    public void testCreateBase64InputStreamForEncoding() throws IOException {
        ByteArrayOutputStream encodedOutputStream = new ByteArrayOutputStream();
        Base64 base64 = new Base64(true);

        String input = "This is a test for Base64InputStream.";

        // Encode the input string and write it to the encodedOutputStream
        base64.encode(input.getBytes(), encodedOutputStream.toByteArray(), true);

        // Create a ByteArrayInputStream from the encoded byte array
        inputStream = new ByteArrayInputStream(encodedOutputStream.toByteArray());

        // Create a Base64InputStream for encoding
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        // Read all bytes and compare them with the encoded bytes
        byte[] expected = encodedOutputStream.toByteArray();
        byte[] actual = new byte[expected.length];
        int bytesRead = base64InputStream.read(actual);
        assertArrayEquals("Expected to read all bytes", expected, actual);
        assertEquals("Expected number of bytes read", bytesRead, expected.length);
    }
}