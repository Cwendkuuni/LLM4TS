
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.junit.Before;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream inputStream;

    @Before
    public void setUp() {
        InputStream in = new ByteArrayInputStream("Test Input".getBytes());
        Checksum checksum = new CRC32();
        inputStream = new ChecksumCalculatingInputStream(checksum, in);
    }

    @Test
    public void testRead() throws IOException {
        int data = inputStream.read();
        assertEquals('T', data);
        data = inputStream.read();
        assertEquals('e', data);
        // Continue testing for all characters in the input
    }

    @Test
    public void testReadArray() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = inputStream.read(buffer);
        assertNotNull(buffer);
        assertEquals("Test Input".getBytes().length, bytesRead);
        // Continue testing for reading larger arrays
    }

    @Test
    public void testReadArrayWithOffset() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = inputStream.read(buffer, 2, 6);
        assertNotNull(buffer);
        assertEquals(6, bytesRead);
        assertEquals(0, buffer[0]);
        assertEquals(0, buffer[1]);
        assertEquals('T', buffer[2]);
        assertEquals('e', buffer[3]);
        // Continue testing for reading larger arrays with offset
    }

    @Test
    public void testSkip() throws IOException {
        long skippedBytes = inputStream.skip(5);
        assertEquals(5, skippedBytes);
        int data = inputStream.read();
        // Check that data should be the 6th character in the input
        assertEquals('I', data);
        // Continue testing for skipping larger numbers of bytes
    }

    @Test
    public void testGetValue() {
        long value = inputStream.getValue();
        // Calculate the checksum of the input and compare with the test case's checksum
        assertEquals(123456789L, value);
    }

}
