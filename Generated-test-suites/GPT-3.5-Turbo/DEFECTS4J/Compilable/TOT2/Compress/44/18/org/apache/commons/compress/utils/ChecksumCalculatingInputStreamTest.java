package org.apache.commons.compress.utils;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Adler32;

public class ChecksumCalculatingInputStreamTest {
    
    @Test
    public void testReadSingleByte() throws IOException {
        byte[] testData = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        Adler32 checksum = new Adler32();
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(checksum, input);
        
        int result = checksumStream.read();
        assertEquals(testData[0], result);
        assertEquals(checksum.getValue(), 1); // Ensure checksum updated correctly
    }
    
    @Test
    public void testReadByteArray() throws IOException {
        byte[] testData = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        Adler32 checksum = new Adler32();
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(checksum, input);
        byte[] buffer = new byte[3];
        
        int bytesRead = checksumStream.read(buffer);
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{1, 2, 3}, buffer);
        assertEquals(checksum.getValue(), 3); // Ensure checksum updated correctly
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] testData = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        Adler32 checksum = new Adler32();
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(checksum, input);
        
        long skipped = checksumStream.skip(2);
        assertEquals(2, skipped);
        assertEquals(checksum.getValue(), 4081); // Ensure checksum updated correctly
    }
    
    @Test
    public void testGetValue() throws IOException {
        byte[] testData = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        Adler32 checksum = new Adler32();
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(checksum, input);
        
        while (checksumStream.read() != -1) {
            // Consume the stream
        }
        
        assertEquals(checksum.getValue(), 4851); // Ensure final checksum value is correct
    }
}