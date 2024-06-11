package org.apache.commons.codec.binary;
import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100}; // Hello World
        InputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        int result = base64InputStream.read();
        Assert.assertEquals(72, result);
        
        result = base64InputStream.read();
        Assert.assertEquals(101, result);
        
        result = base64InputStream.read();
        Assert.assertEquals(108, result);
        
        // continue testing for other bytes
        // ...
    }
    
    @Test
    public void testRead_byteArray_offset_len() throws IOException {
        byte[] data = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100}; // Hello World
        InputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] result = new byte[5];
        int bytesRead = base64InputStream.read(result, 0, 5);
        
        Assert.assertEquals(5, bytesRead);
        Assert.assertArrayEquals(new byte[]{72, 101, 108, 108, 111}, result);
        
        // continue testing for other bytes and offsets
        // ...
    }
    
    @Test
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        boolean isSupported = base64InputStream.markSupported();
        
        Assert.assertFalse(isSupported);
    }
}