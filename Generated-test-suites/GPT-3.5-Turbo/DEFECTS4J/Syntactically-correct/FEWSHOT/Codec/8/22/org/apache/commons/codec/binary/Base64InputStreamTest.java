
package org.apache.commons.codec.binary;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        // Test when no data is available
        InputStream in = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(in, false);
        assertEquals(-1, base64InputStream.read());

        // Test when one byte is available
        in = new ByteArrayInputStream("VGVzdCBiYXNlNjQ=".getBytes());
        base64InputStream = new Base64InputStream(in, false);
        assertEquals("Test base64".getBytes()[0], (byte) base64InputStream.read());

        // Test when multiple bytes are available
        in = new ByteArrayInputStream("VGVzdCBiYXNlNjQ=".getBytes());
        base64InputStream = new Base64InputStream(in, false);
        byte[] expectedBytes = "Test base64".getBytes();
        byte[] actualBytes = new byte[expectedBytes.length];
        base64InputStream.read(actualBytes);
        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], actualBytes[i]);
        }
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        // Test when no data is available
        InputStream in = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(in, false);
        assertEquals(0, base64InputStream.read(new byte[10], 0, 10));

        // Test when offset and length are smaller than available data
        in = new ByteArrayInputStream("VGVzdCBiYXNlNjQ=".getBytes());
        base64InputStream = new Base64InputStream(in, false);
        byte[] expectedBytes = "Test base64".getBytes();
        byte[] actualBytes = new byte[expectedBytes.length];
        assertEquals(expectedBytes.length, base64InputStream.read(actualBytes, 3, 5));
        for (int i = 0; i < 5; i++) {
            assertEquals(expectedBytes[i], actualBytes[i + 3]);
        }

        // Test when offset is larger than available data
        in = new ByteArrayInputStream("VGVzdCBiYXNlNjQ=".getBytes());
        base64InputStream = new Base64InputStream(in, false);
        assertEquals(0, base64InputStream.read(new byte[10], 20, 10));

        // Test when length is larger than available data
        in = new ByteArrayInputStream("VGVzdCBiYXNlNjQ=".getBytes());
        base64InputStream = new Base64InputStream(in, false);
        expectedBytes = "Test base64".getBytes();
        actualBytes = new byte[expectedBytes.length - 2];
        assertEquals(expectedBytes.length - 2, base64InputStream.read(actualBytes, 0, 10));
        for (int i = 0; i < expectedBytes.length - 2; i++) {
            assertEquals(expectedBytes[i], actualBytes[i]);
        }
    }

    @Test
    public void testMarkSupported() {
        InputStream in = new ByteArrayInputStream("VGVzdCBiYXNlNjQ=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in, false);
        assertFalse(base64InputStream.markSupported());
    }

    @Test
    public void testConstructorWithEncodeFlag() {
        InputStream in = new ByteArrayInputStream("VGVzdCBiYXNlNjQ=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in, true);
        assertTrue(base64InputStream.doEncode);
    }

    @Test
    public void testConstructorWithEncodeFlagAndLineLengthAndLineSeparator() {
        InputStream in = new ByteArrayInputStream("VGVzdCBiYXNlNjQ=".getBytes());
        byte[] lineSeparator = "\n".getBytes();
        Base64InputStream base64InputStream = new Base64InputStream(in, true, 10, lineSeparator);
        assertTrue(base64InputStream.doEncode);
        assertEquals(10, base64InputStream.base64.getLineLength());
        assertEquals(lineSeparator, base64InputStream.base64.getLineSeparator());
    }

}
