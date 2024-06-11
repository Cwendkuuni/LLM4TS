package org.apache.commons.codec.binary;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    @Test
    public void testConstructor_InputStream() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertNotNull(base64InputStream);
    }

    @Test
    public void testConstructor_InputStream_Boolean() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(in, false);
        assertNotNull(base64InputStream);
    }

    @Test
    public void testConstructor_InputStream_Boolean_Int_ByteArray() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(in, false, 0, new byte[]{});
        assertNotNull(base64InputStream);
    }

    @Test
    public void testRead() throws Exception {
        byte[] input = {72, 101, 108, 108, 111, 33};
        InputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertEquals(72, base64InputStream.read());
        assertEquals(101, base64InputStream.read());
        assertEquals(108, base64InputStream.read());
        assertEquals(108, base64InputStream.read());
        assertEquals(111, base64InputStream.read());
        assertEquals(33, base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testRead_WithOffsetAndLength() throws Exception {
        byte[] input = {72, 101, 108, 108, 111, 33};
        byte[] buffer = new byte[3];
        InputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertEquals(3, base64InputStream.read(buffer, 0, 3));
        assertArrayEquals(new byte[]{72, 101, 108}, buffer);
        assertEquals(3, base64InputStream.read(buffer, 0, 3));
        assertArrayEquals(new byte[]{108, 111, 33}, buffer);
        assertEquals(0, base64InputStream.read(buffer, 0, 3));
    }

    @Test(expected = NullPointerException.class)
    public void testRead_NullBuffer() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(in);
        base64InputStream.read(null, 0, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_InvalidOffset() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(in);
        base64InputStream.read(new byte[0], -1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_InvalidLength() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(in);
        base64InputStream.read(new byte[0], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_InvalidBufferAndOffset() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(in);
        base64InputStream.read(new byte[0], 1, 1);
    }

    @Test
    public void testMarkSupported() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertFalse(base64InputStream.markSupported());
    }
}