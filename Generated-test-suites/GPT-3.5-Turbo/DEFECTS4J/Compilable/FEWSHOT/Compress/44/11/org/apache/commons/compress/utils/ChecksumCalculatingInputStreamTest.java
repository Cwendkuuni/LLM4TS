package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException {
        Checksum checksum = new Adler32();
        byte[] input = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);
        
        int result = stream.read();
        assertEquals(1, result);
        assertEquals(1, checksum.getValue());
        
        result = stream.read();
        assertEquals(2, result);
        assertEquals(3, checksum.getValue());
        
        // Add more test cases as needed...
    }

    @Test
    public void testReadByteArray() throws IOException {
        Checksum checksum = new Adler32();
        byte[] input = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);
        byte[] buffer = new byte[3];
        
        int result = stream.read(buffer);
        assertEquals(3, result);
        assertEquals(6, checksum.getValue());
        
        // Add more test cases as needed...
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        Checksum checksum = new Adler32();
        byte[] input = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);
        byte[] buffer = new byte[3];
        
        int result = stream.read(buffer, 1, 2);
        assertEquals(2, result);
        assertEquals(3, checksum.getValue());
        
        // Add more test cases as needed...
    }

    @Test
    public void testSkip() throws IOException {
        Checksum checksum = new Adler32();
        byte[] input = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);
        
        long result = stream.skip(2);
        assertEquals(2, result);
        assertEquals(6, checksum.getValue());
        
        // Add more test cases as needed...
    }

    @Test
    public void testGetValue() {
        Checksum checksum = new Adler32();
        byte[] input = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);
        
        long result = stream.getValue();
        assertEquals(0, result);
        
        // Add more test cases as needed...
    }
}