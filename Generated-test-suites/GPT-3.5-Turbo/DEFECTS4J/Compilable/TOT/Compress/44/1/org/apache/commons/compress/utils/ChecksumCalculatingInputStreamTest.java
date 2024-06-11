package org.apache.commons.compress.utils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {
   
    private ChecksumCalculatingInputStream inputStream;
    private Checksum checksum;
    private byte[] testData;
    
    @Before
    public void setup() {
        checksum = new Adler32();
        testData = "This is a test".getBytes();
        inputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(testData));
    }
    
    @Test
    public void testReadByte() throws IOException {
        int data = inputStream.read();
        assertEquals(testData[0], (byte) data);
        assertEquals(checksum.getValue(), 534297249L); // Checksum value should be calculated correctly
    }
    
    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[6];
        int bytesRead = inputStream.read(buffer);
        assertArrayEquals("This i".getBytes(), buffer); // Check if the read data matches the expected data
        assertEquals(6, bytesRead); // Check if the correct number of bytes have been read
        assertEquals(checksum.getValue(), 1888675243L); // Checksum value should be calculated correctly
    }
    
    @Test
    public void testReadPartialByteArray() throws IOException {
        byte[] buffer = new byte[14];
        int bytesRead = inputStream.read(buffer, 2, 10);
        assertArrayEquals(new byte[]{0, 0, 'T', 'h', 'i', 's', ' ', 'i', 's', ' ', 0, 0, 0, 0}, buffer);
        assertEquals(10, bytesRead); // Check if the correct number of bytes have been read
        assertEquals(checksum.getValue(), 2859475806L); // Checksum value should be calculated correctly
    }
    
    @Test
    public void testSkip() throws IOException {
        long bytesSkipped = inputStream.skip(6);
        assertEquals(6, bytesSkipped); // Check if the correct number of bytes have been skipped
        assertEquals(checksum.getValue(), 1888675243L); // Checksum value should be calculated correctly
    }
    
    @Test
    public void testChecksumValue() {
        assertEquals(0, inputStream.getValue()); // Check if the initial checksum value is 0
    }
    
}