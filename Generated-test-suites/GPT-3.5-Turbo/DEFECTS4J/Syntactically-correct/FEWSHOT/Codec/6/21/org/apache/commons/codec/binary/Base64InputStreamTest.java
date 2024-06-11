
package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {
    @Test
    public void testConstructorWithInputStream() {
        InputStream in = null;
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertFalse(base64InputStream.doEncode);
        assertEquals(new Base64(false), base64InputStream.base64);
    }
    
    @Test
    public void testConstructorWithInputStreamAndDoEncode() {
        InputStream in = null;
        boolean doEncode = true;
        Base64InputStream base64InputStream = new Base64InputStream(in, doEncode);
        assertTrue(base64InputStream.doEncode);
        assertEquals(new Base64(false), base64InputStream.base64);
    }
    
    @Test
    public void testConstructorWithInputStreamDoEncodeLineLengthLineSeparator() {
        InputStream in = null;
        boolean doEncode = true;
        int lineLength = 0;
        byte[] lineSeparator = new byte[2];
        Base64InputStream base64InputStream = new Base64InputStream(in, doEncode, lineLength, lineSeparator);
        assertTrue(base64InputStream.doEncode);
        assertEquals(new Base64(lineLength, lineSeparator), base64InputStream.base64);
    }
    
    @Test
    public void testRead() throws IOException {
        InputStream in = null;
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertEquals(-1, base64InputStream.read());
    }
    
    @Test
    public void testReadWithByteOffsetLen() throws IOException {
        InputStream in = null;
        byte[] b = new byte[10];
        int offset = 1;
        int len = 5;
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertEquals(0, base64InputStream.read(b, offset, len));
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws IOException {
        InputStream in = null;
        base64InputStream = new Base64InputStream(in);
        base64InputStream.read(null, 1, 5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        InputStream in = null;
        byte[] b = new byte[10];
        int offset = -1;
        int len = 5;
        Base64InputStream base64InputStream = new Base64InputStream(in);
        base64InputStream.read(b, offset, len);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLen() throws IOException {
        InputStream in = null;
        byte[] b = new byte[10];
        int offset = 1;
        int len = -5;
        Base64InputStream base64InputStream = new Base64InputStream(in);
        base64InputStream.read(b, offset, len);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffsetAndLen() throws IOException {
        InputStream in = null;
        byte[] b = new byte[10];
        int offset = 11;
        int len = 5;
        Base64InputStream base64InputStream = new Base64InputStream(in);
        base64InputStream.read(b, offset, len);
    }
    
    @Test(expected = IOException.class)
    public void testReadWithIOException() throws IOException {
        InputStream in = null;
        Base64InputStream base64InputStream = new Base64InputStream(in);
        base64InputStream.read(new byte[1], 0, 1);
    }
    
    @Test
    public void testMarkSupported() {
        InputStream in = null;
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertFalse(base64InputStream.markSupported());
    }
}
