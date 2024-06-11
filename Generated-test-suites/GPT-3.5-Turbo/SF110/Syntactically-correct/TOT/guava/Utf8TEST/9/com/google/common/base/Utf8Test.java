import org.junit.Assert;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength_withEmptyString_returnsZero() {
        // Arrange
        String emptyString = "";
        
        // Act
        int result = Utf8.encodedLength(emptyString);
        
        // Assert
        Assert.assertEquals(0, result);
    }
    
    @Test
    public void testEncodedLength_withAsciiOnlyString_returnsUtf8Length() {
        // Arrange
        String asciiString = "Hello world";
        
        // Act
        int result = Utf8.encodedLength(asciiString);
        
        // Assert
        Assert.assertEquals(asciiString.length(), result);
    }
    
    @Test
    public void testEncodedLength_withUtf8Characters_returnsUtf8Length() {
        // Arrange
        String utf8String = "你好，世界";
        
        // Act
        int result = Utf8.encodedLength(utf8String);
        
        // Assert
        Assert.assertEquals(utf8String.getBytes().length, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_withInvalidUtf8Length_throwsIllegalArgumentException() {
        // Arrange
        String invalidUtf8String = "你好，世界";
        
        // Act
        Utf8.encodedLength(invalidUtf8String);
        
        // Assert (expected exception)
    }
    
    @Test
    public void testIsWellFormed_withWellFormedByteArray_returnsTrue() {
        // Arrange
        byte[] wellFormedBytes = {-26, -120, -111, -24, -81, -107, -26, -119, -109};
        
        // Act
        boolean result = Utf8.isWellFormed(wellFormedBytes);
        
        // Assert
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsWellFormed_withMalformedByteArray_returnsFalse() {
        // Arrange
        byte[] malformedBytes = {-26, -120, -111, -24, -81, -107, -26, -119, 65};
        
        // Act
        boolean result = Utf8.isWellFormed(malformedBytes);
        
        // Assert
        Assert.assertFalse(result);
    }
    
    @Test
    public void testIsWellFormed_withPartialByteArray_returnsFalse() {
        // Arrange
        byte[] partialBytes = {-26, -120, -111, -24, -81, -107};
        
        // Act
        boolean result = Utf8.isWellFormed(partialBytes);
        
        // Assert
        Assert.assertFalse(result);
    }
    
    @Test
    public void testIsWellFormed_withEmptyByteArray_returnsTrue() {
        // Arrange
        byte[] emptyBytes = {};
        
        // Act
        boolean result = Utf8.isWellFormed(emptyBytes);
        
        // Assert
        Assert.assertTrue(result);
    }

}