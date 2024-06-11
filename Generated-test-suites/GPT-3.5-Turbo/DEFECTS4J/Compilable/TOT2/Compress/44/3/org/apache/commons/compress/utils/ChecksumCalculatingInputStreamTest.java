package org.apache.commons.compress.utils;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead_singleByte() throws IOException {
        // Arrange
        byte[] data = {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, inputStream);
        
        // Act
        int result = cis.read();
        
        // Assert
        assertEquals(1, result);
        assertEquals(1, checksum.getValue());
    }

    @Test
    public void testRead_byteArray() throws IOException {
        // Arrange
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] buffer = new byte[3];
        
        // Act
        int result = cis.read(buffer);
        
        // Assert
        assertEquals(3, result);
        assertEquals(0x674f6156, checksum.getValue());
    }

    @Test
    public void testRead_byteArrayWithOffset() throws IOException {
        // Arrange
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] buffer = new byte[5];
        
        // Act
        int result = cis.read(buffer, 1, 3);
        
        // Assert
        assertEquals(3, result);
        assertEquals(0x674f6156, checksum.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        // Arrange
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, inputStream);
        
        // Act
        long result = cis.skip(3);
        
        // Assert
        assertEquals(0, result);
    }

}