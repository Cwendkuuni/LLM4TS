package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    private InputStream inputStream;
    private InputStream decodedInputStream;
    private InputStream encodedInputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setup() {
        String inputData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGg=";
        outputStream = new ByteArrayOutputStream();
        byte[] inputBytes = inputData.getBytes(StandardCharsets.UTF_8);

        inputStream = new ByteArrayInputStream(inputBytes);
        decodedInputStream = new Base64InputStream(inputStream);
        encodedInputStream = new Base64OutputStream(outputStream, false);
    }

    @Test
    public void testReadOneByteDecoded() throws IOException {
        int decodedByte = decodedInputStream.read();
        assertEquals(0x41, decodedByte);
    }

    @Test
    public void testReadOneByteEncoded() throws IOException {
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        int encodedByte = encodedInputStream.read();
        assertEquals(0x41, encodedByte);
    }

    @Test
    public void testReadMultipleBytesDecoded() throws IOException {
        byte[] buffer = new byte[10];
        int length = decodedInputStream.read(buffer);
        assertEquals(length, 10);
        for (int i = 0; i < length; ++i) {
            assertEquals((byte) (0x41 + i), buffer[i]);
        }
    }

    @Test
    public void testReadMultipleBytesEncoded() throws IOException {
        byte[] encodedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGg=".getBytes(StandardCharsets.UTF_8);
        inputStream = new ByteArrayInputStream(encodedData);
        byte[] buffer = new byte[20];
        int length = encodedInputStream.read(buffer);
        assertEquals(length, 14);
        for (int i = 0; i < length; ++i) {
            assertEquals(buffer[i], inputData.charAt(i));
        }
    }

    @Test
    public void testMarkSupported() {
        assertFalse(new Base64InputStream(new ByteArrayInputStream(new byte[0])).markSupported());
    }

    @Test(expected = IOException.class)
    public void testReadPastEndOfStreamDecoded() throws IOException {
        int byteRead = decodedInputStream.read();
        int byteRead2 = decodedInputStream.read();
        decodedInputStream.read(); // should return -1
        int byteRead3 = decodedInputStream.read(); // should throw IOException
    }

    @Test(expected = IOException.class)
    public void testReadPastEndOfStreamEncoded() throws IOException {
        int byteRead = encodedInputStream.read();
        int byteRead2 = encodedInputStream.read();
        int byteRead3 = encodedInputStream.read();
        encodedInputStream.read(); // should return -1
        int byteRead4 = encodedInputStream.read(); // should throw IOException
    }

    @Test
    public void testErrorReadNullInput() {
        assertThrows(NullPointerException.class, () -> new Base64InputStream(null));
    }

    @Test
    public void testErrorEncodedStreamWithoutDoDecode() {
        assertThrows(IllegalArgumentException.class, () -> new Base64InputStream(new ByteArrayInputStream(new byte[0]), true));
    }

    @Test
    public void testErrorWrongLineLength() {
        assertThrows(IllegalArgumentException.class, () -> new Base64InputStream(new ByteArrayInputStream(new byte[0]), false, 1));
    }

    @Test
    public void testErrorWrongLineSeparator() {
        byte[] lineSeparator = new byte[1];
        assertThrows(IllegalArgumentException.class, () -> new Base64InputStream(new ByteArrayInputStream(new byte[0]), false, 0, lineSeparator));
    }

    @Test
    public void testErrorNullLineSeparator() {
        assertThrows(NullPointerException.class, () -> new Base64InputStream(new ByteArrayInputStream(new byte[0]), false, 0, null));
    }

    @Test
    public void testErrorNullOutputStream() {
        assertThrows(NullPointerException.class, () -> new Base64OutputStream(null, false));
    }

    @Test
    public void testErrorNullInputStream() {
        assertThrows(NullPointerException.class, () -> new Base64OutputStream(null, true));
    }
}