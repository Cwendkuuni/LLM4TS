package org.apache.commons.compress.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException {
        // Test case 1
        byte[] byteArray1 = "test".getBytes();
        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(byteArray1);
        Checksum checksum1 = new Checksum();
        ChecksumCalculatingInputStream testObj1 = new ChecksumCalculatingInputStream(checksum1, inputStream1);
        int result1 = testObj1.read();
        assertEquals(byteArray1[0], (byte) result1);

        // Test case 2 (empty stream)
        byte[] byteArray2 = "".getBytes();
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(byteArray2);
        Checksum checksum2 = new Checksum();
        ChecksumCalculatingInputStream testObj2 = new ChecksumCalculatingInputStream(checksum2, inputStream2);
        int result2 = testObj2.read();
        assertEquals(-1, result2);
    }

    @Test
    public void testReadWithByteArray() throws IOException {
        // Test case 1
        byte[] byteArray1 = "test".getBytes();
        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(byteArray1);
        Checksum checksum1 = new Checksum();
        ChecksumCalculatingInputStream testObj1 = new ChecksumCalculatingInputStream(checksum1, inputStream1);
        byte[] buffer1 = new byte[4];
        int result1 = testObj1.read(buffer1);
        assertArrayEquals(byteArray1, buffer1);

        // Test case 2 (empty stream)
        byte[] byteArray2 = "".getBytes();
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(byteArray2);
        Checksum checksum2 = new Checksum();
        ChecksumCalculatingInputStream testObj2 = new ChecksumCalculatingInputStream(checksum2, inputStream2);
        byte[] buffer2 = new byte[0];
        int result2 = testObj2.read(buffer2);
        assertEquals(-1, result2);
    }

    @Test
    public void testReadWithByteArrayAndOffset() throws IOException {
        // Test case 1
        byte[] byteArray1 = "test".getBytes();
        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(byteArray1);
        Checksum checksum1 = new Checksum();
        ChecksumCalculatingInputStream testObj1 = new ChecksumCalculatingInputStream(checksum1, inputStream1);
        byte[] buffer1 = new byte[4];
        int offset1 = 0;
        int length1 = 4;
        int result1 = testObj1.read(buffer1, offset1, length1);
        assertArrayEquals(byteArray1, buffer1);

        // Test case 2 (empty stream)
        byte[] byteArray2 = "".getBytes();
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(byteArray2);
        Checksum checksum2 = new Checksum();
        ChecksumCalculatingInputStream testObj2 = new ChecksumCalculatingInputStream(checksum2, inputStream2);
        byte[] buffer2 = new byte[0];
        int offset2 = 0;
        int length2 = 0;
        int result2 = testObj2.read(buffer2, offset2, length2);
        assertEquals(-1, result2);
    }

    @Test
    public void testSkip() throws IOException {
        // Test case 1
        byte[] byteArray1 = "test".getBytes();
        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(byteArray1);
        Checksum checksum1 = new Checksum();
        ChecksumCalculatingInputStream testObj1 = new ChecksumCalculatingInputStream(checksum1, inputStream1);
        long result1 = testObj1.skip(1);
        assertEquals(1, result1);

        // Test case 2 (empty stream)
        byte[] byteArray2 = "".getBytes();
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(byteArray2);
        Checksum checksum2 = new Checksum();
        ChecksumCalculatingInputStream testObj2 = new ChecksumCalculatingInputStream(checksum2, inputStream2);
        long result2 = testObj2.skip(1);
        assertEquals(0, result2);
    }

    @Test
    public void testGetValue() throws IOException {
        // Test case 1
        byte[] byteArray1 = "test".getBytes();
        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(byteArray1);
        Checksum checksum1 = new Checksum();
        ChecksumCalculatingInputStream testObj1 = new ChecksumCalculatingInputStream(checksum1, inputStream1);
        testObj1.read();
        long result1 = testObj1.getValue();
        assertEquals(2090733, result1);

        // Test case 2 (empty stream)
        byte[] byteArray2 = "".getBytes();
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(byteArray2);
        Checksum checksum2 = new Checksum();
        ChecksumCalculatingInputStream testObj2 = new ChecksumCalculatingInputStream(checksum2, inputStream2);
        testObj2.read();
        long result2 = testObj2.getValue();
        assertEquals(0, result2);
    }

}