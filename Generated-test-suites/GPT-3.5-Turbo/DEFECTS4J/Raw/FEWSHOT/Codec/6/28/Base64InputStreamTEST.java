package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testRead_singleByte() throws IOException {
        byte[] input = "SGVsbG8gd29ybGQ=".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        int result = base64InputStream.read();
        
        assertEquals(72, result);
    }
    
    @Test
    public void testRead_byteArray() throws IOException {
        byte[] input = "SGVsbG8gd29ybGQ=".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] output = new byte[10];
        
        int result = base64InputStream.read(output, 0, 10);
        
        assertEquals(10, result);
        assertEquals(72, output[0]);
        assertEquals(101, output[1]);
        assertEquals(108, output[2]);
        assertEquals(108, output[3]);
        assertEquals(111, output[4]);
        assertEquals(32, output[5]);
        assertEquals(119, output[6]);
        assertEquals(111, output[7]);
        assertEquals(114, output[8]);
        assertEquals(108, output[9]);
    }
    
    @Test
    public void testRead_byteArray_offset() throws IOException {
        byte[] input = "SGVsbG8gd29ybGQ=".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] output = new byte[10];
        
        int result = base64InputStream.read(output, 5, 5);
        
        assertEquals(5, result);
        assertEquals(0, output[0]);
        assertEquals(0, output[1]);
        assertEquals(0, output[2]);
        assertEquals(0, output[3]);
        assertEquals(0, output[4]);
        assertEquals(72, output[5]);
        assertEquals(101, output[6]);
        assertEquals(108, output[7]);
        assertEquals(108, output[8]);
        assertEquals(111, output[9]);
    }
    
    @Test
    public void testRead_byteArray_lenGreaterThanAvailableData() throws IOException {
        byte[] input = "SGVsbG8gd29ybGQ=".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] output = new byte[15];
        
        int result = base64InputStream.read(output, 0, 15);
        
        assertEquals(13, result);
        assertEquals(72, output[0]);
        assertEquals(101, output[1]);
        assertEquals(108, output[2]);
        assertEquals(108, output[3]);
        assertEquals(111, output[4]);
        assertEquals(32, output[5]);
        assertEquals(119, output[6]);
        assertEquals(111, output[7]);
        assertEquals(114, output[8]);
        assertEquals(108, output[9]);
        assertEquals(100, output[10]);
        assertEquals(32, output[11]);
        assertEquals(61, output[12]);
        assertEquals(61, output[13]);
        assertEquals(0, output[14]);
    }
    
    @Test(expected = IOException.class)
    public void testRead_byteArray_exceptionOnClosedStream() throws IOException {
        byte[] input = "SGVsbG8gd29ybGQ=".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        base64InputStream.close();
        
        byte[] output = new byte[10];
        base64InputStream.read(output, 0, 10);
    }
    
    @Test
    public void testMarkSupported() {
        byte[] input = "SGVsbG8gd29ybGQ=".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        boolean result = base64InputStream.markSupported();
        
        assertFalse(result);
    }
    
    @Test
    public void testDoEncode() throws IOException {
        byte[] input = "Hello world=".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(in, true);
        byte[] output = new byte[15];
        
        int result = base64InputStream.read(output, 0, 15);
        
        assertEquals(16, result);
        assertEquals(72, output[0]);
        assertEquals(101, output[1]);
        assertEquals(108, output[2]);
        assertEquals(108, output[3]);
        assertEquals(111, output[4]);
        assertEquals(32, output[5]);
        assertEquals(119, output[6]);
        assertEquals(111, output[7]);
        assertEquals(114, output[8]);
        assertEquals(108, output[9]);
        assertEquals(100, output[10]);
        assertEquals(61, output[11]);
        assertEquals(119, output[12]);
        assertEquals(111, output[13]);
        assertEquals(114, output[14]);
        assertEquals(108, output[15]);
    }
    
    @Test
    public void testDoEncode_withLineLengthAndLineSeparator() throws IOException {
        byte[] input = "Hello world test test".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        byte[] lineSeparator = {13, 10};
        Base64InputStream base64InputStream = new Base64InputStream(in, true, 5, lineSeparator);
        byte[] output = new byte[20];
        
        int result = base64InputStream.read(output, 0, 20);
        
        assertEquals(17, result);
        assertEquals(72, output[0]);
        assertEquals(101, output[1]);
        assertEquals(108, output[2]);
        assertEquals(108, output[3]);
        assertEquals(111, output[4]);
        assertEquals(32, output[5]);
        assertEquals(119, output[6]);
        assertEquals(111, output[7]);
        assertEquals(114, output[8]);
        assertEquals(108, output[9]);
        assertEquals(100, output[10]);
        assertEquals(32, output[11]);
        assertEquals(116, output[12]);
        assertEquals(101, output[13]);
        assertEquals(115, output[14]);
        assertEquals(116, output[15]);
        assertEquals(32, output[16]);
        assertEquals(116, output[17]);
        assertEquals(101, output[18]);
        assertEquals(115, output[19]);
    }

    @Test
    public void testDoDecode() throws IOException {
        byte[] input = "SGVsbG8gd29ybGQ=".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(in, false);
        byte[] output = new byte[10];
        
        int result = base64InputStream.read(output, 0, 10);
        
        assertEquals(13, result);
        assertEquals(72, output[0]);
        assertEquals(101, output[1]);
        assertEquals(108, output[2]);
        assertEquals(108, output[3]);
        assertEquals(111, output[4]);
        assertEquals(32, output[5]);
        assertEquals(119, output[6]);
        assertEquals(111, output[7]);
        assertEquals(114, output[8]);
        assertEquals(108, output[9]);
        assertEquals(100, output[10]);
        assertEquals(32, output[11]);
        assertEquals(0, output[12]);
    }
}