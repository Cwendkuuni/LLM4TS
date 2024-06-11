package org.apache.commons.compress.compressors.deflate;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {
    
    @Test
    public void testReadSingleByte() throws IOException {
        byte[] testData = {65, 66, 67}; // ASCII values for 'A', 'B', 'C'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(inputStream);
        int result = dcis.read();
        
        assertEquals(65, result); // Expected result is the ASCII value of 'A'
    }
    
    @Test
    public void testReadByteArray() throws IOException {
        byte[] testData = {65, 66, 67}; // ASCII values for 'A', 'B', 'C'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[3];
        dcis.read(buffer, 0, 3);
        
        assertArrayEquals(testData, buffer); // Expected result is the original data
    }
    
    @Test
    public void testSkipBytes() throws IOException {
        byte[] testData = {65, 66, 67}; // ASCII values for 'A', 'B', 'C'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(inputStream);
        dcis.skip(2);
        
        int result = dcis.read(); // Should read the last byte 'C'
        assertEquals(67, result);
    }
    
    @Test
    public void testAvailableBytes() throws IOException {
        byte[] testData = {65, 66, 67}; // ASCII values for 'A', 'B', 'C'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(inputStream);
        
        int available = dcis.available();
        assertEquals(3, available); // Expected available bytes is 3
    }
    
    @Test
    public void testCloseStream() throws IOException {
        byte[] testData = {65, 66}; // ASCII values for 'A', 'B'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(inputStream);
        dcis.close();
        
        // Attempt to read after closing should throw IOException
        assertThrows(IOException.class, () -> dcis.read());
    }
    
    @Test
    public void testZlibHeaderFlag() {
        byte[] testData = {}; // Empty data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(inputStream, parameters);
        
        assertFalse(parameters.withZlibHeader()); // Expected zlib header flag is false
    }
    
}