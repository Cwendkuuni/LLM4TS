package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testReadOneByte() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFG".getBytes());
        Base64InputStream base64is = new Base64InputStream(in);
        byte readByte = base64is.read();
        assertEquals(65, readByte); // ASCII code for 'A'
        assertEquals(-1, base64is.read()); // End of Stream
    }

    @Test
    public void testReadByteArray() throws IOException {
        String input = "ABCDEFG";
        byte[] expected = input.getBytes();
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Base64InputStream base64is = new Base64InputStream(in);

        byte[] readBytes = new byte[input.length()];
        int numRead = base64is.read(readBytes, 0, readBytes.length);
        assertArrayEquals(expected, readBytes);
        assertEquals(-1, base64is.read()); // End of Stream
    }

    @Test
    public void testReadByteArrayWithDecoding() throws IOException {
        String input = "ABCDEFG==";
        byte[] expected = input.getBytes();
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Base64InputStream base64is = new Base64InputStream(in, true);

        byte[] readBytes = new byte[input.length()];
        int numRead = base64is.read(readBytes, 0, readBytes.length);
        assertArrayEquals(expected, readBytes);
        assertEquals(-1, base64is.read()); // End of Stream
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        InputStream in = null;
        new Base64InputStream(in);
    }

    @Test
    public void testEmptyInput() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64is = new Base64InputStream(in);
        assertNull(base64is.read()); // End of Stream
    }

    @Test
    public void testReadNegativeByte() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFG".getBytes());
        Base64InputStream base64is = new Base64InputStream(in);
        assertEquals(-65, base64is.read()); // -(ASCII code for 'A')
        assertEquals(-1, base64is.read()); // End of Stream
    }

    @Test
    public void testReadPartialByteArray() throws IOException {
        String input = "ABCDEFG";
        byte[] expected = new byte[] { 65, 66, 67 }; // First three bytes of input
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Base64InputStream base64is = new Base64InputStream(in);

        byte[] readBytes = new byte[3];
        int numRead = base64is.read(readBytes, 0, readBytes.length);
        assertArrayEquals(expected, readBytes);
        assertEquals(68, base64is.read()); // Next byte in the input
        assertEquals(-1, base64is.read()); // End of Stream
    }

    @Test
    public void testReadPartialByteArrayWithDecoding() throws IOException {
        String input = "ABCDEFG==";
        byte[] expected = new byte[] { 65, 66, 67 }; // First three bytes of input decoded
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Base64InputStream base64is = new Base64InputStream(in, true);

        byte[] readBytes = new byte[3];
        int numRead = base64is.read(readBytes, 0, readBytes.length);
        assertArrayEquals(expected, readBytes);
        assertEquals(68, base64is.read()); // Next byte in the input decoded
        assertEquals(-1, base64is.read()); // End of Stream
    }

    @Test
    public void testReadByteArrayWithLineLength() throws IOException {
        String input = "ABCDEFGABCDEFGABCDEFGABCDEFGABCDEFG";
        int lineLength = 10;
        byte[] expected = new byte[input.length()];
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Base64InputStream base64is = new Base64InputStream(in, true, lineLength);

        byte[] readBytes = new byte[input.length()];
        int numRead = base64is.read(readBytes, 0, readBytes.length);
        assertArrayEquals(expected, readBytes);
        assertEquals(-1, base64is.read()); // End of Stream
    }

    @Test
    public void testReadByteArrayWithLineSeparator() throws IOException {
        String input = "ABCDEFGABCDEFGABCDEFGABCDEFGABCDEFG==";
        byte[] lineSeparator = new byte[] { 13, 10 }; // CRLF
        byte[] expected = new byte[input.length()];
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Base64InputStream base64is = new Base64InputStream(in, true, 0, lineSeparator);

        byte[] readBytes = new byte[input.length()];
        int numRead = base64is.read(readBytes, 0, readBytes.length);
        assertArrayEquals(expected, readBytes);
        assertEquals(-1, base64is.read()); // End of Stream
    }
}