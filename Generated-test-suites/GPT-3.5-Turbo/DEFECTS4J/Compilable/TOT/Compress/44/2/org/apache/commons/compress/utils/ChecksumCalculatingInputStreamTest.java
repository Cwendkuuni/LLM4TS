package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testReadReturnsMinusOneWhenExhausted() throws IOException {
        // Arrange
        byte[] data = new byte[10];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum,
                inputStream);

        // Act
        int result = 0;
        while (result != -1) {
            result = calculatingInputStream.read();
        }

        // Assert
        assertEquals(-1, result);
    }

    @Test
    public void testReadReturnsExpectedValueWhenNotExhausted() throws IOException {
        // Arrange
        byte[] data = new byte[] { 1, 2, 3 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum,
                inputStream);

        // Act
        int result = calculatingInputStream.read();

        // Assert
        assertEquals(1, result);
    }

    @Test
    public void testReadAcceptsByteArray() throws IOException {
        // Arrange
        byte[] data = new byte[] { 1, 2, 3 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum,
                inputStream);
        byte[] buffer = new byte[3];

        // Act
        int result = calculatingInputStream.read(buffer);

        // Assert
        assertEquals(3, result);
    }

    @Test
    public void testReadAcceptsByteArrayWithOffsetAndLength() throws IOException {
        // Arrange
        byte[] data = new byte[] { 1, 2, 3 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum,
                inputStream);
        byte[] buffer = new byte[3];

        // Act
        int result = calculatingInputStream.read(buffer, 0, 2);

        // Assert
        assertEquals(2, result);
    }

    @Test
    public void testSkipReturnsZeroWhenExhausted() throws IOException {
        // Arrange
        byte[] data = new byte[10];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum,
                inputStream);

        // Act
        long result = calculatingInputStream.skip(1);

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testSkipReturnsOneWhenNotExhausted() throws IOException {
        // Arrange
        byte[] data = new byte[] { 1 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum,
                inputStream);

        // Act
        long result = calculatingInputStream.skip(1);

        // Assert
        assertEquals(1, result);
    }

    @Test
    public void testGetValueReturnsExpectedValue() throws IOException {
        // Arrange
        byte[] data = new byte[] { 1, 2, 3 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum,
                inputStream);

        // Act
        calculatingInputStream.read();
        calculatingInputStream.read();
        calculatingInputStream.read();
        long result = calculatingInputStream.getValue();

        // Assert
        assertEquals(66052, result);
    }
}

