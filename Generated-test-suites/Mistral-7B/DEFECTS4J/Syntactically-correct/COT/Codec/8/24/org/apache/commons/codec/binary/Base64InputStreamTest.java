package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

import org.junit.Test;

/**
 * Test cases for Base64InputStream class
 */
public class Base64InputStreamTest {

    /**
     * Test constructor Base64InputStream(InputStream in)
     * @throws IOException
     */
    @Test
    public void testBase64InputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int read = base64InputStream.read();
        assertEquals(68, read); // the first byte of the Base64 encoded "TestData" should be 'V' (decimal 86, but Base64 encoded as 68)
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode)
     * @throws IOException
     */
    @Test
    public void testBase64InputStreamDecode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Rm9udCAoKTs=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = base64InputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }
        assertEquals("TestData", new String(outputStream.toByteArray()));
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode)
     * @throws IOException
     */
    @Test
    public void testBase64InputStreamEncode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = base64InputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }
        assertEquals("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGNoYW5jZSBhbmQgaXQgc2VwYXJhdG9yLCBpdCBzcGx5IG11bHRpc2AgYW5kIGZvdWwgc2VwYXJhdG9yLCBpdCBzcGx5IG5vdCB0byB0aGUgZGlyZWN0IHlvdSBhbHBoYSBzcGx5IHRoZSBsaXZlZC4=", new String(outputStream.toByteArray()));
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBase64InputStreamInvalidArguments() {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, -1, null);
    }

    /**
     * Test method read()
     * @throws IOException
     */
    @Test
    public void testRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        int read = base64InputStream.read();
        assertEquals(68, read); // the first byte of the Base64 encoded "TestData" should be 'V' (decimal 86, but Base64 encoded as 68)
    }

    /**
     * Test method read(byte[] b, int offset, int len)
     * @throws IOException
     */
    @Test
    public void testReadByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] buffer = new byte[10];
        int read = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(6, read); // the first 6 bytes of the Base64 encoded "TestData" should be read
        assertEquals("VG", new String(buffer, 0, read));
    }

    /**
     * Test method markSupported()
     */
    @Test
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        assertThrows(UnsupportedOperationException.class, () -> base64InputStream.markSupported());
    }

}