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

    @Test
    public void testBase64InputStream() {
        InputStream is = new ByteArrayInputStream("RnJvbSBvZiB1c2VyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICA8cGF0Y2g=".getBytes());
        Base64InputStream bis = new Base64InputStream(is);
        // test default decoding
        byte[] b = new byte[10];
        int len = bis.read(b, 0, b.length);
        assertEquals(len, 10);
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, b);

        // test custom encoding
        Base64OutputStream bos = new Base64OutputStream(new ByteArrayOutputStream());
        bos.write("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!\"#$%&'()*+,-./01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!\"#$%&'()*+,-./".getBytes("ASCII"));
        bos.flush();
        InputStream os = new ByteArrayInputStream(bos.toByteArray());
        Base64InputStream bisEnc = new Base64InputStream(os, true);
        b = new byte[10];
        len = bisEnc.read(b, 0, b.length);
        assertArrayEquals(new byte[] { 'R', 'n', 'J', 'v', 'b', 'S', 'B', 'v', 'Z', 'i' }, b);
    }

    @Test
    public void testCustomLineLengthAndLineSeparator() {
        InputStream is = new ByteArrayInputStream("VG8gZXhpc3QgYW5kIHlvdSBtZW50aW9uIGZvbGx5IGZvdWwgdGhpbmdzIHdlbmVyMQ==".getBytes());
        Base64InputStream bis = new Base64InputStream(is, false, 5, new byte[] { 0x0A, 0x0D });
        byte[] b = new byte[20];
        int len = bis.read(b, 0, b.length);
        assertEquals(len, 13);
        assertArrayEquals(new byte[] { 0x5B, 0x31, 0x33, 0x36, 0x38, 0x39, 0x61, 0x63, 0x63, 0x35, 0x37, 0x38, 0x61 }, b);
    }

    @Test
    public void testMarkSupported() {
        InputStream is = new ByteArrayInputStream("RnJvbSBvZiB1c2VyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICA8cGF0Y2g=".getBytes());
        Base64InputStream bis = new Base64InputStream(is);
        assertFalse(bis.markSupported());
    }

    @Test
    public void testRead() throws IOException {
        InputStream is = new ByteArrayInputStream("RnJvbSBvZiB1c2VyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICA8cGF0Y2g=".getBytes());
        Base64InputStream bis = new Base64InputStream(is);

        byte[] b = new byte[10];
        int len;

        len = bis.read(b, 0, b.length);
        assertEquals(len, 10);
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, b);

        len = bis.read(b, 0, b.length);
        assertEquals(len, -1);

        len = bis.read(b, 1, b.length - 1);
        assertEquals(len, 9);
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 }, b);

        len = bis.read(b, 0, 0);
        assertEquals(len, -1);

        len = bis.read(b, 2, b.length - 2);
        assertEquals(len, 8);
        assertArrayEquals(new byte[] { 3, 4, 5, 6, 7, 8, 9 }, b);

        len = bis.read(b, 13, b.length - 13);
        assertEquals(len, -1);
    }

    @Test
    public void testReadBytes() throws IOException {
        InputStream is = new ByteArrayInputStream("RnJvbSBvZiB1c2VyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICA8cGF0Y2g=".getBytes());
        Base64InputStream bis = new Base64InputStream(is);

        byte[] b = new byte[10];
        int len;
        byte[] result = new byte[20];

        len = bis.read(b, 0, b.length);
        assertEquals(len, 10);
        System.arraycopy(b, 0, result, 0, len);

        len = bis.read(b, 0, b.length);
        assertEquals(len, -1);

        len = bis.read(b, 1, b.length - 1);
        assertEquals(len, 9);
        System.arraycopy(b, 1, result, 0, len);

        len = bis.read(b, 0, 0);
        assertEquals(len, -1);

        len = bis.read(b, 2, b.length - 2);
        assertEquals(len, 8);
        System.arraycopy(b, 2, result, 0, len);

        len = bis.read(b, 13, b.length - 13);
        assertEquals(len, -1);

        assertEquals(result.length, 31);
        assertEquals(result[0], 0);
        assertEquals(result[1], 1);
        assertEquals(result[2], 2);
        assertEquals(result[3], 3);
        assertEquals(result[4], 4);
        assertEquals(result[5], 5);
        assertEquals(result[6], 6);
        assertEquals(result[7], 7);
        assertEquals(result[8], 8);
        assertEquals(result[9], 9);
        assertEquals(result[10], 49);
        assertEquals(result[11], 50);
        assertEquals(result[12], 51);
        assertEquals(result[13], 52);
        assertEquals(result[14], 53);
        assertEquals(result[15], 54);
        assertEquals(result[16], 55);
        assertEquals(result[17], 56);
        assertEquals(result[18], 57);
        assertEquals(result[19], 58);
        assertEquals(result[20], 59);
    }

    @Test
    public void testUnsupportedEncodingException() throws IOException {
        InputStream is = new ByteArrayInputStream("RnJvbSBvZiB1c2VyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICA8cGF0Y2g=".getBytes("UTF-16BE"));
        Base64InputStream bis = new Base64InputStream(is);
        assertNull(bis.getEncoder());
        assertNull(bis.getDecoder());
    }

    @Test(expected = IOException.class)
    public void testUnsupportedOperationException() throws IOException {
        InputStream is = new ByteArrayInputStream("RnJvbSBvZiB1c2VyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICA8cGF0Y2g=".getBytes());
        Base64InputStream bis = new Base64InputStream(is);
        bis.mark(1);
    }

}