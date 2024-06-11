package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    private static final byte[] TEST_DATA = { 0x42, 0x69, 0x62, 0x62, 0x61, 0x67, 0x21, 0x20, 0x43, 0x6F, 0x6E, 0x6E, 0x65, 0x6E, 0x74, 0x20, 0x42, 0x61, 0x6E, 0x64, 0x65, 0x64, 0x21, 0x21, 0x3D, 0x3D, 0x00 }; // "Base64Decode"

    private static InputStream createTestInputStream() {
        return new ByteArrayInputStream(TEST_DATA);
    }

    @Test
    public void testNewInstanceDecodesData() throws IOException {
        InputStream is = new Base64InputStream(createTestInputStream());

        byte[] decodedData = new byte[TEST_DATA.length];
        int bytesRead = is.read(decodedData);
        assertEquals(TEST_DATA.length, bytesRead);
        assertArrayEquals(TEST_DATA, decodedData);
    }

    @Test
    public void testNewInstanceHandlesEOF() throws IOException {
        InputStream is = new Base64InputStream(createTestInputStream());
        byte[] buffer = new byte[1];

        assertTrue(is.read(buffer) >= 0);
        assertFalse(is.markSupported());

        // Read all available data from the stream
        int bytesRead;
        do {
            bytesRead = is.read(buffer);
        } while (bytesRead > 0);

        assertNull(is.read(buffer));
    }

    @Test
    public void testNewInstanceThrowsIoeOnNullInputStream() {
        InputStream in = null;
        try {
            new Base64InputStream(in);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testNewInstanceWithDecodingFalseDoesNotDecodeData() throws IOException {
        InputStream is = new Base64InputStream(createTestInputStream(), false);

        byte[] data = new byte[TEST_DATA.length];
        int bytesRead = is.read(data);
        assertEquals(TEST_DATA.length, bytesRead);
        assertArrayEquals(TEST_DATA, data);
    }

    @Test
    public void testNewInstanceWithEncodingTrueEncodesData() throws IOException {
        InputStream dataInputStream = new ByteArrayInputStream(TEST_DATA);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Base64OutputStream base64Output = new Base64OutputStream(byteArrayOutputStream);
        Base64InputStream base64InputStream = new Base64InputStream(dataInputStream, true);

        int bytesRead;
        do {
            bytesRead = base64InputStream.read(singleByte);
            base64Output.write(singleByte);
        } while (bytesRead > 0);

        byte[] encodedData = byteArrayOutputStream.toByteArray();
        String encodedString = new String(encodedData, "US-ASCII");
        String expected = "VmFzdGVtcy8xMDE2IEtleQ0K";
        assertEquals(expected, encodedString);
    }

    @Test
    public void testNewInstanceWithLineLengthAndLineSeparator() throws IOException {
        InputStream dataInputStream = new ByteArrayInputStream(TEST_DATA);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Base64OutputStream base64Output = new Base64OutputStream(byteArrayOutputStream, 76, "\n".getBytes("US-ASCII"));
        Base64InputStream base64InputStream = new Base64InputStream(dataInputStream, true, 76, "\n".getBytes("US-ASCII"));

        int bytesRead;
        do {
            bytesRead = base64InputStream.read(singleByte);
            base64Output.write(singleByte);
        } while (bytesRead > 0);

        byte[] encodedData = byteArrayOutputStream.toByteArray();
        String encodedString = new String(encodedData, "US-ASCII");
        String expected = "VmFzdGVtcy8xMDE2\n"
                + "QkFTRS4gV19BQUFFX1RJVG9PV19BQUFFX1RJVG9P\n"
                + "UlNBQUFFX1RJVG9PV19BQUFFX1RJVG9P\n"
                + "UlNBQUFFX1RJVG5JRjUgW1RJRjUgW1RJRjUgW1RJ\n"
                + "RjUgW1RJRjUgW1RJRjUgV19BQUFFX1RJ";
        assertEquals(expected, encodedString);
    }
}