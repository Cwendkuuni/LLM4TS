package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {
    private ChecksumCalculatingInputStream inputStream;
    private byte[] data = {0x45, 0x54, 0x49, 0x20, 0x76, 0x61, 0x6C, 0x75, 0x65}; // "ETI value"

    @Before
    public void setup() {
        InputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        inputStream = new ChecksumCalculatingInputStream(checksum, in);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        int expectedChecksumValue = 0x36AC853F; // the expected checksum value for the data
        int byteRead;
        
        // Reading the first byte
        byteRead = inputStream.read();
        assertEquals(0x45, byteRead);
        
        // Reading the second byte
        byteRead = inputStream.read();
        assertEquals(0x54, byteRead);
        
        // Reading the remaining bytes
        for (int i = 0; i < 7; i++) {
            byteRead = inputStream.read();
        }

         // Verification of the checksum value
        long actualChecksumValue = inputStream.getValue();
        assertEquals(expectedChecksumValue, actualChecksumValue);
    }

    @Test
    public void testReadByteArray() throws IOException {
        int expectedChecksumValue = 0x36AC853F; // the expected checksum value for the data
        byte[] buffer = new byte[4];
        int bytesRead;

        // Reading the first 4 bytes
        bytesRead = inputStream.read(buffer);
        assertArrayEquals(new byte[]{0x45, 0x54, 0x49, 0x20}, buffer);
        
        // Reading the remaining bytes
        bytesRead = inputStream.read(buffer);
        assertArrayEquals(new byte[]{0x76, 0x61, 0x6C, 0x75}, buffer);

        // Verification of the checksum value
        long actualChecksumValue = inputStream.getValue();
        assertEquals(expectedChecksumValue, actualChecksumValue);
    }
    
    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        int expectedChecksumValue = 0x36AC853F; // the expected checksum value for the data
        byte[] buffer = new byte[6];
        int bytesRead;

        // Reading the first 4 bytes into the buffer starting at offset 1
        bytesRead = inputStream.read(buffer, 1, 4);
        assertArrayEquals(new byte[]{0x00, 0x45, 0x54, 0x49, 0x00, 0x00}, buffer);

        // Reading the remaining bytes into the buffer starting at offset 2
        bytesRead = inputStream.read(buffer, 2, 4);
        assertArrayEquals(new byte[]{0x00, 0x00, 0x76, 0x61, 0x6C, 0x75}, buffer);
        
        // Verification of the checksum value
        long actualChecksumValue = inputStream.getValue();
        assertEquals(expectedChecksumValue, actualChecksumValue);
    }
    
    @Test
    public void testSkip() throws IOException {
        int expectedChecksumValue = 0x36AC853F; // the expected checksum value for the data
        long bytesSkipped;

        // Skipping 5 bytes
        bytesSkipped = inputStream.skip(5);
        assertEquals(5, bytesSkipped);

        // Verification of the checksum value
        long actualChecksumValue = inputStream.getValue();
        assertEquals(expectedChecksumValue, actualChecksumValue);
    }

}
