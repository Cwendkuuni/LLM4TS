import org.junit.Test;
import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead_singleByte() {
        // Arrange
        byte[] byteArray = {65}; // 'A' in ASCII
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);
        
        // Act
        int result = baseNCodecInputStream.read();
        
        // Assert
        assertEquals(65, result);
    }
    
    @Test
    public void testRead_multipleBytes() {
        // Arrange
        byte[] byteArray = {65, 66, 67}; // 'A', 'B', 'C' in ASCII
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);
        byte[] resultArray = new byte[3];
        
        // Act
        int result = baseNCodecInputStream.read(resultArray, 0, 3);
        
        // Assert
        assertEquals(3, result);
        assertArrayEquals(byteArray, resultArray);
    }
    
    @Test
    public void testRead_nullByteArray() {
        // Arrange
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(null);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);
        byte[] resultArray = new byte[3];
        
        // Act
        try {
            baseNCodecInputStream.read(resultArray, 0, 3);
        } catch (NullPointerException e) {
            // Assert
            assertNotNull(e);
        }
    }
    
    @Test
    public void testRead_negativeOffset() {
        // Arrange
        byte[] byteArray = {65, 66, 67}; // 'A', 'B', 'C' in ASCII
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);
        byte[] resultArray = new byte[3];
        
        // Act
        try {
            baseNCodecInputStream.read(resultArray, -1, 3);
        } catch (IndexOutOfBoundsException e) {
            // Assert
            assertNotNull(e);
        }
    }
    
    @Test
    public void testRead_negativeLength() {
        // Arrange
        byte[] byteArray = {65, 66, 67}; // 'A', 'B', 'C' in ASCII
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);
        byte[] resultArray = new byte[3];
        
        // Act
        try {
            baseNCodecInputStream.read(resultArray, 0, -1);
        } catch (IndexOutOfBoundsException e) {
            // Assert
            assertNotNull(e);
        }
    }
    
    @Test
    public void testRead_invalidOffset() {
        // Arrange
        byte[] byteArray = {65, 66, 67}; // 'A', 'B', 'C' in ASCII
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);
        byte[] resultArray = new byte[3];
        
        // Act
        try {
            baseNCodecInputStream.read(resultArray, 4, 3);
        } catch (IndexOutOfBoundsException e) {
            // Assert
            assertNotNull(e);
        }
    }
    
    @Test
    public void testRead_zeroLength() {
        // Arrange
        byte[] byteArray = {65, 66, 67}; // 'A', 'B', 'C' in ASCII
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);
        byte[] resultArray = new byte[3];
        
        // Act
        int result = baseNCodecInputStream.read(resultArray, 0, 0);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testMarkSupported() {
        // Arrange
        byte[] byteArray = {65, 66, 67}; // 'A', 'B', 'C' in ASCII
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);
        
        // Act
        boolean result = baseNCodecInputStream.markSupported();
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testRead_EOF() {
        // Arrange
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(null);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);
        
        // Act
        int result = baseNCodecInputStream.read();
        
        // Assert
        assertEquals(-1, result);
    }
}