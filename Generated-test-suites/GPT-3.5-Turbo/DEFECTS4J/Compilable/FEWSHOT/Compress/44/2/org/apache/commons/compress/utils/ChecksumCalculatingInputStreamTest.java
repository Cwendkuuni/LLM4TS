package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {
    @Test
    public void testRead() throws IOException {
        byte[] testData = { 0x01, 0x02, 0x03, 0x04, 0x05 };
        ChecksumChecksumCalculator checksumCalculator = new ChecksumChecksumCalculator(new Adler32());
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksumCalculator, new ByteArrayInputStream(testData));
        
        int byteRead = 0;
        while ((byteRead = inputStream.read()) != -1) {
            // do nothing, just read the data
        }
        
        assertEquals(checksumCalculator.getValue(), 0x2352c47L);
    }
    
    @Test
    public void testReadBuffer() throws IOException {
        byte[] testData = { 0x01, 0x02, 0x03, 0x04, 0x05 };
        ChecksumChecksumCalculator checksumCalculator = new ChecksumChecksumCalculator(new Adler32());
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksumCalculator, new ByteArrayInputStream(testData));
        
        byte[] buffer = new byte[3];
        int bytesRead = inputStream.read(buffer);
        
        assertTrue(bytesRead == 3);
        assertEquals(checksumCalculator.getValue(), 0x9ab0ec9L);
    }
    
    @Test
    public void testReadBufferOffset() throws IOException {
        byte[] testData = { 0x01, 0x02, 0x03, 0x04, 0x05 };
        ChecksumChecksumCalculator checksumCalculator = new ChecksumChecksumCalculator(new Adler32());
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksumCalculator, new ByteArrayInputStream(testData));
        
        byte[] buffer = new byte[10];
        int bytesRead = inputStream.read(buffer, 2, 3);
        
        assertTrue(bytesRead == 3);
        assertEquals(checksumCalculator.getValue(), 0xef41ef10L);
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] testData = { 0x01, 0x02, 0x03, 0x04, 0x05 };
        ChecksumChecksumCalculator checksumCalculator = new ChecksumChecksumCalculator(new Adler32());
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksumCalculator, new ByteArrayInputStream(testData));
        
        long bytesSkipped = inputStream.skip(2);
        assertEquals(bytesSkipped, 2);
        assertEquals(checksumCalculator.getValue(), 0x32f0L);
    }
    
    @Test
    public void testGetValue() {
        byte[] testData = { 0x01, 0x02, 0x03, 0x04, 0x05 };
        ChecksumChecksumCalculator checksumCalculator = new ChecksumChecksumCalculator(new Adler32());
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksumCalculator, new ByteArrayInputStream(testData));
        
        long value = inputStream.getValue();
        assertEquals(value, 0x2f58d20L);
    }
    
    private class ChecksumChecksumCalculator implements Checksum {
        private Checksum checksum;
        
        public ChecksumChecksumCalculator(Checksum checksum) {
            this.checksum = checksum;
        }
        
        @Override
        public void update(int b) {
            checksum.update(b);
        }
    
        @Override
        public void update(byte[] b, int off, int len) {
            checksum.update(b, off, len);
        }
    
        @Override
        public long getValue() {
            return checksum.getValue();
        }
    
        @Override
        public void reset() {
            checksum.reset();
        }
        
    }
}