package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
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

    private static final byte[] EMPTY_ARR = new byte[0];

    private static final byte[] ENC_DATA = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };

    private static final byte[] DEC_DATA = { -65, -49, -48, -49, -49, -48, -49, -48, -49, -49, -48, -49, -48,
            -49, -48 };

    private static final String ENC_STR = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIGZyb20gc29tZSBjb21wYW55IGZpbGVzLCBjYW4gZW5jb2RlZCB5b3VyIGZpbGVzLg==";

    private static final String DEC_STR = "Jywgb25nb2xrJywgYW5kIGZ1dHVyZSB0byBmYWxzIGluIGZpbGVzLg==";

    @Test
    public void testBase64InputStreamDefault() throws IOException {
        InputStream in = new ByteArrayInputStream(ENC_DATA);
        Base64InputStream base64is = new Base64InputStream(in);
        byte[] readData = new byte[ENC_DATA.length];
        int len = base64is.read(readData, 0, readData.length);
        assertArrayEquals(ENC_DATA, readData);
        base64is.close();
    }

    @Test
    public void testBase64InputStreamEncode() throws IOException {
        InputStream in = new ByteArrayInputStream(ENC_DATA);
        Base64InputStream base64is = new Base64InputStream(in, true);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Base64OutputStream base64os = new Base64OutputStream(bos);
        int len = base64is.read(base64os.buffer, 0, base64os.buffer.length);
        base64os.flush();
        base64is.close();
        byte[] readData = bos.toByteArray();
        assertEquals(ENC_STR, new String(readData, "UTF-8"));
    }

    @Test
    public void testBase64InputStreamDecodeFail() throws IOException {
        InputStream in = new ByteArrayInputStream(ENC_STR.getBytes("UTF-8"));
        Base64InputStream base64is = new Base64InputStream(in);
        assertThrows(IOException.class, () -> base64is.read(EMPTY_ARR, 0, EMPTY_ARR.length));
        base64is.close();
    }

    @Test
    public void testBase64InputStreamDecodeSuccess() throws IOException {
        InputStream in = new ByteArrayInputStream(ENC_STR.getBytes("UTF-8"));
        Base64InputStream base64is = new Base64InputStream(in);
        byte[] readData = new byte[DEC_DATA.length];
        int len = base64is.read(readData, 0, readData.length);
        assertArrayEquals(DEC_DATA, readData);
        base64is.close();
    }

    @Test
    public void testBase64InputStreamMarkSupported() throws IOException {
        InputStream in = new ByteArrayInputStream(ENC_DATA);
        Base64InputStream base64is = new Base64InputStream(in);
        assertFalse(base64is.markSupported());
        base64is.close();
    }

    @Test
    public void testBase64InputStreamClose() throws IOException {
        InputStream in = new ByteArrayInputStream(ENC_DATA);
        Base64InputStream base64is = new Base64InputStream(in);
        base64is.close();
        assertThrows(IOException.class, () -> base64is.read());
    }

    @Test
    public void testBase64InputStreamCustomLineLengthAndSeparator() throws IOException {
        InputStream in = new ByteArrayInputStream(ENC_DATA);
        Base64InputStream base64is = new Base64InputStream(in, true, 60, DEC_STR.getBytes("UTF-8"));
        byte[] readData = new byte[ENC_DATA.length];
        int len = base64is.read(readData, 0, readData.length);
        assertArrayEquals(ENC_DATA, readData);
        base64is.close();
    }
}