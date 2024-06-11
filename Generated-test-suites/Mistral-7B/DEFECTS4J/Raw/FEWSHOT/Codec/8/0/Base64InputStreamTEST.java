package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testDefaultConstructor() {
        InputStream in = new ByteArrayInputStream("ABCDEFGHijklMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
        Base64InputStream base64is = new Base64InputStream(in);
        byte[] expected = new byte[] { 65, 66, 67, 68, 69, 72, 73, 74, 75, 76, 77, 78, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111 };
        byte[] actual = new byte[26];
        int len = base64is.read(actual, 0, 26);
        assertTrue(len == 26);
        assertArrayEquals(expected, actual);
        assertFalse(base64is.markSupported());
        base64is.close();
    }

    @Test
    public void testDoEncodeConstructor() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFGHijklMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] src = "QWertyUiopasdfghjKLPOIUZABCxyz1234567890-_".getBytes();
        Base64OutputStream bos64 = new Base64OutputStream(bos);
        bos64.write(src, 0, src.length);
        bos64.close();
        InputStream srcStream = new ByteArrayInputStream(bos.toByteArray());
        Base64InputStream base64is = new Base64InputStream(srcStream, true);
        byte[] expected = src;
        byte[] actual = new byte[src.length];
        int len = base64is.read(actual, 0, src.length);
        assertTrue(len == src.length);
        assertArrayEquals(expected, actual);
        assertFalse(base64is.markSupported());
        base64is.close();
    }

    @Test
    public void testDoEncodeConstructorWithParameters() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFGHijklMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] src = "QWertyUiopasdfghjKLPOIUZABCxyz1234567890-_".getBytes();
        Base64OutputStream bos64 = new Base64OutputStream(bos, 64, "\r\n");
        bos64.write(src, 0, src.length);
        bos64.close();
        InputStream srcStream = new ByteArrayInputStream(bos.toByteArray());
        Base64InputStream base64is = new Base64InputStream(srcStream, true, 64, "\r\n".getBytes());
        byte[] expected = src;
        byte[] actual = new byte[src.length];
        int len = base64is.read(actual, 0, src.length);
        assertTrue(len == src.length);
        assertArrayEquals(expected, actual);
        assertTrue(base64is.markSupported());
        base64is.close();
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        Base64InputStream base64is = new Base64InputStream(null);
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFGHijklMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] src = "QWertyUiopasdfghjKLPOIUZABCxyz1234567890-_".getBytes();
        Base64OutputStream bos64 = new Base64OutputStream(bos);
        bos64.write(src, 0, src.length);
        bos64.close();
        InputStream srcStream = new ByteArrayInputStream(bos.toByteArray());
        Base64InputStream base64is = new Base64InputStream(srcStream, true);
        byte[] expected = new byte[0];
        byte[] actual = new byte[1];
        int len = base64is.read(actual, 0, 1);
        assertFalse(len > 0);
        InputStream badIn = new ByteArrayInputStream(new byte[] { (byte) 0xFF });
        Base64InputStream badBase64is = new Base64InputStream(badIn, true);
        badBase64is.close();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeLength() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFGHijklMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
        Base64InputStream base64is = new Base64InputStream(in);
        byte[] actual = new byte[1];
        base64is.read(actual, -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeOffset() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFGHijklMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
        Base64InputStream base64is = new Base64InputStream(in);
        byte[] actual = new byte[1];
        base64is.read(actual, -1, 1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeLengthAndNegativeOffset() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFGHijklMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
        Base64InputStream base64is = new Base64InputStream(in);
        byte[] actual = new byte[1];
        base64is.read(actual, -1, -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeLengthAndNegativeOffset2() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFGHijklMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
        Base64InputStream base64is = new Base64InputStream(in);
        byte[] actual = new byte[1];
        base64is.read(actual, 1, -1, 1);
    }

    @Test
    public void testOffset() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFGHijklMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
        Base64InputStream base64is = new Base64InputStream(in);
        byte[] expected = new byte[] { 65, 66, 67, 68 };
        byte[] actual = new byte[4];
        int len = base64is.read(actual, 0, 4);
        assertTrue(len == 4);
        assertArrayEquals(expected, actual);
        base64is.close();
    }

    @Test
    public void testLength() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFGHijklMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
        Base64InputStream base64is = new Base64InputStream(in);
        byte[] expected = new byte[] { 65, 66, 67, 68, 69, 72, 73, 74, 75, 76 };
        byte[] actual = new byte[11];
        int len = base64is.read(actual, 0, 11);
        assertTrue(len == 11);
        assertArrayEquals(expected, actual);
        base64is.close();
    }

    @Test
    public void testLengthWithPartialRead() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFGHijklMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
        Base64InputStream base64is = new Base64InputStream(in);
        byte[] expected = new byte[] { 65, 66, 67, 68 };
        byte[] actual = new byte[4];
        int len = base64is.read(actual, 0, 4);
        assertTrue(len == 4);
        assertArrayEquals(expected, actual);
        byte[] expected2 = new byte[] { 72, 73, 74, 75, 76 };
        byte[] actual2 = new byte[5];
        len = base64is.read(actual2, 0, 5);
        assertTrue(len == 5);
        assertArrayEquals(expected2, actual2);
        base64is.close();
    }

    @Test
    public void testClose() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFGHijklMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
        Base64InputStream base64is = new Base64InputStream(in);
        byte[] expected = new byte[] { 65, 66, 67, 68 };
        byte[] actual = new byte[4];
        int len = base64is.read(actual, 0, 4);
        assertTrue(len == 4);
        assertArrayEquals(expected, actual);
        base64is.close();
    }

    @Test(expected = IOException.class)
    public void testCloseTwice() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFGHijklMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
        Base64InputStream base64is = new Base64InputStream(in);
        byte[] expected = new byte[] { 65, 66, 67, 68 };
        byte[] actual = new byte[4];
        int len = base64is.read(actual, 0, 4);
        assertTrue(len == 4);
        assertArrayEquals(expected, actual);
        base64is.close();
        base64is.close();
    }

    @Test(expected = NullPointerException.class)
    public void testCloseWithNull() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFGHijklMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
        Base64InputStream base64is = new Base64InputStream(in);
        byte[] expected = new byte[] { 65, 66, 67, 68 };
        byte[] actual = new byte[4];
        int len = base64is.read(actual, 0, 4);
        assertTrue(len == 4);
        assertArrayEquals(expected, actual);
        base64is.close();
        base64is = null;
        base64is.close();
    }
}