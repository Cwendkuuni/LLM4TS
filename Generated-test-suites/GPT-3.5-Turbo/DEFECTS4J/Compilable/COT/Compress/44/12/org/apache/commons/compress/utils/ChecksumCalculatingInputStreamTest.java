package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream inputStream;
    private MockChecksum checksum;
    private byte[] testData;

    @Before
    public void setUp() {
        testData = "Hello, world!".getBytes();
        checksum = new MockChecksum();
        inputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(testData));
    }

    @Test
    public void testRead() throws IOException {
        int data = inputStream.read();
        assertEquals('H', data); // Check if the first character is read correctly
        assertEquals(1, checksum.getUpdateCount()); // Check if the checksum is updated once
    }

    @Test
    public void testReadArray() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer);
        assertEquals("Hello", new String(buffer)); // Check if the first 5 characters are read correctly
        assertEquals(5, bytesRead); // Check if the correct number of bytes is read
        assertEquals(1, checksum.getUpdateCount()); // Check if the checksum is updated once
    }

    @Test
    public void testReadArrayWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer, 1, 3);
        assertEquals("\0H\0e\0", new String(buffer)); // Check if the characters at the correct positions are read correctly
        assertEquals(3, bytesRead); // Check if the correct number of bytes is read
        assertEquals(1, checksum.getUpdateCount()); // Check if the checksum is updated once
    }

    @Test
    public void testSkip() throws IOException {
        long bytesSkipped = inputStream.skip(6);
        assertEquals(6, bytesSkipped); // Check if the correct number of bytes is skipped
        assertEquals(1, checksum.getUpdateCount()); // Check if the checksum is updated once
    }

    @Test
    public void testGetValue() {
        long value = inputStream.getValue();
        assertEquals(0, value); // Check if the initial value of the checksum is returned
    }
}

class MockChecksum implements Checksum {

    private int updateCount = 0;

    public int getUpdateCount() {
        return updateCount;
    }

    @Override
    public void update(int b) {
        updateCount++;
    }

    @Override
    public void update(byte[] b, int off, int len) {
        updateCount++;
    }

    @Override
    public long getValue() {
        return 0;
    }

    @Override
    public void reset() {

    }
}
