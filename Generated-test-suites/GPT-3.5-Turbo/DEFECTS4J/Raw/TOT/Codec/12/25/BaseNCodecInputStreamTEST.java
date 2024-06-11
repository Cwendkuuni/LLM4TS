import org.junit.Test;
import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead_WhenInputStreamHasData_ReturnsByteAsIntegerInRange_ZeroTo255() {
        // Arrange
        byte[] data = {65, 66, 67};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        
        // Act
        int result = inputStream.read();
        
        // Assert
        assertEquals(65, result);
    }
    
    @Test
    public void testRead_WhenInputStreamHasNoData_ReturnsMinusOne() {
        // Arrange
        byte[] data = {};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        
        // Act
        int result = inputStream.read();
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testRead_WhenInputStreamHasPartialData_ReturnsByteAsIntegerInRange_ZeroTo255() {
        // Arrange
        byte[] data = {65, 66, 67};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        
        // Read first byte
        inputStream.read();
        
        // Act
        int result = inputStream.read();
        
        // Assert
        assertEquals(66, result);
    }
    
    @Test
    public void testRead_WhenInputStreamThrowsIOException_ThrowsIOException() {
        // Arrange
        ByteArrayInputStream in = new ByteArrayInputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        
        // Act & Assert
        assertThrows(IOException.class, () -> {
            inputStream.read();
        });
    }
    
    @Test
    public void testRead_WhenInputStreamIsNull_ThrowsNullPointerException() {
        // Arrange
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(null, baseNCodec, false);
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            inputStream.read();
        });
    }
    
    @Test
    public void testRead_WhenOffsetIsNegative_ThrowsIndexOutOfBoundsException() {
        // Arrange
        byte[] data = {65, 66, 67};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        
        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> {
            inputStream.read(null, -1, 3);
        });
    }
    
    @Test
    public void testRead_WhenLenIsNegative_ThrowsIndexOutOfBoundsException() {
        // Arrange
        byte[] data = {65, 66, 67};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        
        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> {
            inputStream.read(null, 0, -1);
        });
    }
    
    @Test
    public void testRead_WhenOffsetIsGreaterThanArrayLength_ThrowsIndexOutOfBoundsException() {
        // Arrange
        byte[] data = {65, 66, 67};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        
        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> {
            inputStream.read(null, 4, 2);
        });
    }
    
    @Test
    public void testRead_WhenOffsetPlusLenIsGreaterThanArrayLength_ThrowsIndexOutOfBoundsException() {
        // Arrange
        byte[] data = {65, 66, 67};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        
        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> {
            inputStream.read(null, 2, 2);
        });
    }
    
    @Test
    public void testRead_WhenLenIsZero_ReturnsZero() {
        // Arrange
        byte[] data = {65, 66, 67};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        
        // Act
        int result = inputStream.read(new byte[3], 0, 0);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testRead_WhenInputStreamHasData_ReturnsNumberOfBytesRead() {
        // Arrange
        byte[] data = {65, 66, 67};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        byte[] buffer = new byte[3];
        
        // Act
        int result = inputStream.read(buffer, 0, 3);
        
        // Assert
        assertEquals(3, result);
    }
    
    @Test
    public void testRead_WhenInputStreamHasNoData_ReturnsMinusOne() {
        // Arrange
        byte[] data = {};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        byte[] buffer = new byte[3];
        
        // Act
        int result = inputStream.read(buffer, 0, 3);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testRead_WhenInputStreamHasPartialData_ReturnsNumberOfBytesRead() {
        // Arrange
        byte[] data = {65, 66, 67};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        byte[] buffer = new byte[3];
        
        // Read one byte
        inputStream.read();
        
        // Act
        int result = inputStream.read(buffer, 0, 2);
        
        // Assert
        assertEquals(2, result);
    }
    
    @Test
    public void testRead_WhenInputStreamThrowsIOException_ThrowsIOException() {
        // Arrange
        ByteArrayInputStream in = new ByteArrayInputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        byte[] buffer = new byte[3];
        
        // Act & Assert
        assertThrows(IOException.class, () -> {
            inputStream.read(buffer, 0, 3);
        });
    }
    
    @Test
    public void testRead_WhenInputStreamIsNull_ThrowsNullPointerException() {
        // Arrange
        byte[] buffer = new byte[3];
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(null, baseNCodec, false);
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            inputStream.read(buffer, 0, 3);
        });
    }
    
    @Test
    public void testMarkSupported_Always_ReturnsFalse() {
        // Arrange
        ByteArrayInputStream in = new ByteArrayInputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        
        // Act
        boolean result = inputStream.markSupported();
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testAvailable_Always_ReturnsOne() {
        // Arrange
        ByteArrayInputStream in = new ByteArrayInputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        
        // Act
        int result = inputStream.available();
        
        // Assert
        assertEquals(1, result);
    }
}