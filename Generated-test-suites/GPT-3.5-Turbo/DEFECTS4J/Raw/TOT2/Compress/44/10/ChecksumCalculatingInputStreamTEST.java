import org.junit.Test;
import static org.junit.Assert.*;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testReadSingleByte() {
        // Arrange
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(new byte[] {1, 2, 3}));
        
        // Act
        int result = inputStream.read();
        
        // Assert
        assertEquals(1, result);
        assertEquals(49, inputStream.getValue()); // CRC32 checksum for byte 1
    }
    
    @Test
    public void testReadByteArray() {
        // Arrange
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(new byte[] {1, 2, 3}));
        byte[] buffer = new byte[3];
        
        // Act
        int result = inputStream.read(buffer);
        
        // Assert
        assertEquals(3, result);
        assertEquals(2489709587L, inputStream.getValue()); // CRC32 checksum for bytes 1, 2, 3
    }
    
    @Test
    public void testReadByteArrayWithOffsetAndLength() {
        // Arrange
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(new byte[] {1, 2, 3}));
        byte[] buffer = new byte[3];
        
        // Act
        int result = inputStream.read(buffer, 0, 2);
        
        // Assert
        assertEquals(2, result);
        assertEquals(730782452L, inputStream.getValue()); // CRC32 checksum for bytes 1, 2
    }
    
    @Test
    public void testSkip() {
        // Arrange
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(new byte[] {1, 2, 3}));
        
        // Act
        long result = inputStream.skip(1);
        
        // Assert
        assertEquals(1, result);
        assertEquals(0L, inputStream.getValue()); // Skipped byte should not affect checksum
    }

}