package org.apache.commons.compress.utils;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testReadSingleByte() throws IOException {
        byte[] testData = {65, 66, 67}; // ASCII values for A, B, C
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, input);

        int readByte = stream.read();
        assertEquals(65, readByte);

        long expectedValue = 4296485223L; // Checksum value of ABC using CRC32
        assertEquals(expectedValue, stream.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] testData = {65, 66, 67}; // ASCII values for A, B, C
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, input);

        byte[] buffer = new byte[3];
        int bytesRead = stream.read(buffer);

        assertEquals(3, bytesRead);
        assertArrayEquals(testData, buffer);

        long expectedValue = 4296485223L; // Checksum value of ABC using CRC32
        assertEquals(expectedValue, stream.getValue());
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] testData = {65, 66, 67}; // ASCII values for A, B, C
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, input);

        byte[] buffer = new byte[5];
        int bytesRead = stream.read(buffer, 1, 3);

        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{0, 65, 66, 67, 0}, buffer);

        long expectedValue = 4296485223L; // Checksum value of ABC using CRC32
        assertEquals(expectedValue, stream.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] testData = {65, 66, 67}; // ASCII values for A, B, C
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, input);

        long skippedBytes = stream.skip(2);

        assertEquals(1, skippedBytes);
        long expectedValue = 4296485223L; // Checksum value of ABC using CRC32
        assertEquals(expectedValue, stream.getValue());
    }

    @Test(expected = IOException.class)
    public void testReadException() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{});
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, input);

        stream.read();
    }

    @Test
    public void testReadByteArrayException() {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{});
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, input);

        byte[] buffer = new byte[3];
        try {
            stream.read(buffer);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testReadByteArrayWithOffsetException() {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{});
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, input);

        byte[] buffer = new byte[3];
        try {
            stream.read(buffer, 0, 3);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

}