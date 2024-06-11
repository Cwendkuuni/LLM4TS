package org.apache.commons.compress.utils;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {
    
    @Test
    public void testReadByte() throws IOException {
        Checksum mockChecksum = new MockChecksum();
        byte[] testData = {1, 2, 3};
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData);
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(mockChecksum, testInputStream);
        
        for (int i = 0; i < testData.length; i++) {
            int result = checksumStream.read();
            assertEquals(testData[i], result);
        }
        
        long checksumValue = checksumStream.getValue();
        assertEquals(mockChecksum.getValue(), checksumValue);
    }
    
    @Test
    public void testReadByteArray() throws IOException {
        Checksum mockChecksum = new MockChecksum();
        byte[] testData = {1, 2, 3, 4, 5};
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData);
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(mockChecksum, testInputStream);
        
        byte[] buffer = new byte[3];
        int bytesRead = checksumStream.read(buffer);
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{1, 2, 3}, buffer);
        
        bytesRead = checksumStream.read(buffer);
        assertEquals(2, bytesRead);
        assertArrayEquals(new byte[]{4, 5, 3}, buffer);
        
        long checksumValue = checksumStream.getValue();
        assertEquals(mockChecksum.getValue(), checksumValue);
    }
    
    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        Checksum mockChecksum = new MockChecksum();
        byte[] testData = {1, 2, 3, 4, 5};
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData);
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(mockChecksum, testInputStream);
        
        byte[] buffer = new byte[5];
        int bytesRead = checksumStream.read(buffer, 1, 3);
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{0, 1, 2, 3, 0}, buffer);
        
        bytesRead = checksumStream.read(buffer, 2, 2);
        assertEquals(2, bytesRead);
        assertArrayEquals(new byte[]{0, 1, 2, 3, 4}, buffer);
        
        long checksumValue = checksumStream.getValue();
        assertEquals(mockChecksum.getValue(), checksumValue);
    }
    
    @Test
    public void testSkip() throws IOException {
        Checksum mockChecksum = new MockChecksum();
        byte[] testData = {1, 2, 3, 4, 5};
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData);
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(mockChecksum, testInputStream);
        
        long bytesSkipped = checksumStream.skip(3);
        assertEquals(3, bytesSkipped);
        
        int result = checksumStream.read();
        assertEquals(4, result);
        
        long checksumValue = checksumStream.getValue();
        assertEquals(mockChecksum.getValue(), checksumValue);
    }
    
    private class MockChecksum implements Checksum {
        private long value = 0;
        
        @Override
        public void update(int b) {
            value += b;
        }
        
        @Override
        public void update(byte[] b, int off, int len) {
            for (int i = off; i < off + len; i++) {
                value += b[i];
            }
        }
        
        @Override
        public long getValue() {
            return value;
        }
        
        @Override
        public void reset() {
            value = 0;
        }
    }
}