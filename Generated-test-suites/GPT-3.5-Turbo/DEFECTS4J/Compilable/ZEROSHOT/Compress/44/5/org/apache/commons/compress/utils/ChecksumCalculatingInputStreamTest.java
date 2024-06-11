package org.apache.commons.compress.utils;
import org.junit.Test;
import org.junit.Before;
import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import static org.junit.Assert.*;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream inputStream;
    private ByteArrayInputStream mockInputStream;
    private CRC32 mockChecksum;

    // Initialize test objects
    @Before
    public void setUp() {
        byte[] data = "Test data".getBytes();
        mockInputStream = new ByteArrayInputStream(data);
        mockChecksum = new CRC32();
        inputStream = new ChecksumCalculatingInputStream(mockChecksum, mockInputStream);
    }

    // Test read() method
    @Test
    public void testRead() throws IOException {
        int result = inputStream.read();
        assertEquals('T', result);
        long checksumValue = inputStream.getValue();
        assertEquals(mockChecksum.getValue(), checksumValue);
    }

    // Test read(byte[] b) method
    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[10];
        int result = inputStream.read(buffer);
        assertEquals(9, result);
        long checksumValue = inputStream.getValue();
        assertEquals(mockChecksum.getValue(), checksumValue);
    }

    // Test read(byte[] b, int off, int len) method
    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] buffer = new byte[10];
        int result = inputStream.read(buffer, 1, 8);
        assertEquals(8, result);
        long checksumValue = inputStream.getValue();
        assertEquals(mockChecksum.getValue(), checksumValue);
    }

    // Test skip() method
    @Test
    public void testSkip() throws IOException {
        long skippedBytes = inputStream.skip(5);
        assertEquals(1, skippedBytes);
        long checksumValue = inputStream.getValue();
        assertEquals(mockChecksum.getValue(), checksumValue);
    }

    // Test getValue() method
    @Test
    public void testGetValue() {
        long checksumValue = inputStream.getValue();
        assertEquals(mockChecksum.getValue(), checksumValue);
    }

}