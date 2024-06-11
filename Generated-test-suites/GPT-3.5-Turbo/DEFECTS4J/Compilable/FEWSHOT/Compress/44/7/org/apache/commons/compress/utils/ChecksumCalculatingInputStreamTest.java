package org.apache.commons.compress.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] byteArray = "Test".getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream cs = new ChecksumCalculatingInputStream(checksum, inputStream);

        // Read the first character
        int result = cs.read();

        // Check if the checksum has been updated correctly
        assertEquals(84L, checksum.getValue());

        // Check if the read is successful
        assertEquals(84, result);

        // Check if the stream is exhausted
        assertEquals(-1, cs.read());

        // Check if the checksum is unchanged after the stream is exhausted
        assertEquals(84L, checksum.getValue());

        cs.close();
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] byteArray = "Test".getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream cs = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] buffer = new byte[4];

        // Read the byte array from the stream
        int result = cs.read(buffer);

        // Check if the checksum has been updated correctly
        assertEquals(84L, checksum.getValue());

        // Check if the read is successful
        assertEquals(4, result);

        // Check if the bytes read match the expected values
        assertArrayEquals(byteArray, buffer);

        cs.close();
    }

    @Test
    public void testReadByteArrayOffLength() throws IOException {
        byte[] byteArray = "Test".getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream cs = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] buffer = new byte[4];

        // Read the byte array from the stream starting at index 1
        int result = cs.read(buffer, 1, 2);

        // Check if the checksum has been updated correctly
        assertEquals(168L, checksum.getValue());

        // Check if the read is successful
        assertEquals(2, result);

        // Check if the bytes read match the expected values
        assertArrayEquals(new byte[] {0, 84, 0, 0}, buffer);

        cs.close();
    }

    @Test
    public void testSkip() throws IOException {
        byte[] byteArray = "Test".getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream cs = new ChecksumCalculatingInputStream(checksum, inputStream);

        // Skip the first character
        long result = cs.skip(1);

        // Check if the checksum has been updated correctly
        assertEquals(168L, checksum.getValue());

        // Check if the skip is successful
        assertEquals(1, result);

        // Check if the next character is read correctly
        assertEquals(101, cs.read());

        cs.close();
    }

    @Test
    public void testGetValue() throws IOException {
        byte[] byteArray = "Test".getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream cs = new ChecksumCalculatingInputStream(checksum, inputStream);

        // Read the entire stream
        while (cs.read() >= 0) {}

        // Check if the calculated checksum matches the expected value
        assertEquals(117689942L, cs.getValue());

        cs.close();
    }

}