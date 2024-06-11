package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

public class Base64InputStreamTest {
    
    @Test
    public void testConstructorAndGetters() {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        assertNotNull(base64InputStream);
        assertEquals(in, base64InputStream.getIn());
        assertEquals(false, base64InputStream.isDoEncode());
    }
    
    @Test
    public void testConstructorWithDoEncodeAndGetters() {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(in, true);
        
        assertNotNull(base64InputStream);
        assertEquals(in, base64InputStream.getIn());
        assertEquals(true, base64InputStream.isDoEncode());
    }
    
    @Test
    public void testConstructorWithLineLengthAndGetters() {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(in, false, 76, new byte[] { 13, 10 });
        
        assertNotNull(base64InputStream);
        assertEquals(in, base64InputStream.getIn());
        assertEquals(false, base64InputStream.isDoEncode());
        assertEquals(76, base64InputStream.getBase64().getLineLength());
        Assert.assertArrayEquals(new byte[] { 13, 10 }, base64InputStream.getBase64().getLineSeparator());
    }
    
    @Test
    public void testRead() throws IOException {
        byte[] data = new byte[] { -128, 0, 127 };
        InputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        assertEquals(128, base64InputStream.read());
        assertEquals(0, base64InputStream.read());
        assertEquals(255, base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
    }
    
    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] data = new byte[] { -128, 0, 127 };
        InputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        byte[] buffer = new byte[2];
        
        assertEquals(2, base64InputStream.read(buffer, 0, 2));
        Assert.assertArrayEquals(new byte[] { (byte) 128, 0 }, buffer);
        
        assertEquals(1, base64InputStream.read(buffer, 0, 2));
        Assert.assertArrayEquals(new byte[] { (byte) 127, 0 }, buffer);
        
        assertEquals(-1, base64InputStream.read(buffer, 0, 2));
    }
    
    @Test
    public void testReadWithBufferAndOffset() throws IOException {
        byte[] data = new byte[] { -128, 0, 127 };
        InputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        byte[] buffer = new byte[3];
        
        assertEquals(2, base64InputStream.read(buffer, 1, 2));
        Assert.assertArrayEquals(new byte[] { 0, (byte) 128, 0 }, buffer);
        
        assertEquals(1, base64InputStream.read(buffer, 1, 2));
        Assert.assertArrayEquals(new byte[] { 0, (byte) 127, 0 }, buffer);
        
        assertEquals(-1, base64InputStream.read(buffer, 1, 2));
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        byte[] data = new byte[] { -128, 0, 127 };
        InputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        base64InputStream.read(null, 0, 2);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] data = new byte[] { -128, 0, 127 };
        InputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        byte[] buffer = new byte[2];
        
        base64InputStream.read(buffer, -1, 2);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        byte[] data = new byte[] { -128, 0, 127 };
        InputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        byte[] buffer = new byte[2];
        
        base64InputStream.read(buffer, 0, -1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws IOException {
        byte[] data = new byte[] { -128, 0, 127 };
        InputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        byte[] buffer = new byte[3];
        
        base64InputStream.read(buffer, 0, 4);
    }
    
    @Test
    public void testMarkSupported() {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        assertEquals(false, base64InputStream.markSupported());
    }
}