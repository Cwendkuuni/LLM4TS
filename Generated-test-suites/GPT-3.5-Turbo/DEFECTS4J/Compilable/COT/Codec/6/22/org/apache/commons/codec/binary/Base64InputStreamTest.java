package org.apache.commons.codec.binary;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    @Test
    public void testRead_EmptyInputStream() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        int result = base64InputStream.read();
        
        Assert.assertEquals(-1, result);
    }
    
    @Test
    public void testRead_SingleByte() throws IOException {
        byte[] data = { 65 }; // "A"
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        int result = base64InputStream.read();
        
        Assert.assertEquals(65, result);
    }
    
    @Test
    public void testRead_NullByteArray() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        try {
            base64InputStream.read(null, 0, 10);
            Assert.fail("NullPointerException not thrown");
        }
        catch (NullPointerException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testRead_NegativeOffset() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] output = new byte[10];
        
        try {
            base64InputStream.read(output, -1, 10);
            Assert.fail("IndexOutOfBoundsException not thrown");
        }
        catch (IndexOutOfBoundsException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testRead_NegativeLen() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] output = new byte[10];
        
        try {
            base64InputStream.read(output, 0, -1);
            Assert.fail("IndexOutOfBoundsException not thrown");
        }
        catch (IndexOutOfBoundsException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testRead_InvalidOffset() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] output = new byte[10];
        
        try {
            base64InputStream.read(output, 20, 5);
            Assert.fail("IndexOutOfBoundsException not thrown");
        }
        catch (IndexOutOfBoundsException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testRead_InvalidOffsetAndLen() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] output = new byte[10];
        
        try {
            base64InputStream.read(output, 5, 10);
            Assert.fail("IndexOutOfBoundsException not thrown");
        }
        catch (IndexOutOfBoundsException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testRead_ZeroLen() throws IOException {
        byte[] data = { 65 }; // "A"
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] output = new byte[1];
        
        int result = base64InputStream.read(output, 0, 0);
        
        Assert.assertEquals(0, result);
    }
    
    @Test
    public void testRead_WriteSingleByte() throws IOException {
        byte[] data = { 65 }; // "A"
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] output = new byte[1];
        
        int result = base64InputStream.read(output, 0, 1);
        
        Assert.assertEquals(1, result);
        Assert.assertEquals(65, output[0]);
    }
    
    @Test
    public void testRead_WriteMultipleBytes() throws IOException {
        byte[] data = { 65, 66, 67 }; // "ABC"
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] output = new byte[3];
        
        int result = base64InputStream.read(output, 0, 3);
        
        Assert.assertEquals(3, result);
        Assert.assertEquals(65, output[0]);
        Assert.assertEquals(66, output[1]);
        Assert.assertEquals(67, output[2]);
    }
    
    @Test
    public void testMarkSupported() {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        boolean result = base64InputStream.markSupported();
        
        Assert.assertFalse(result);
    }
}