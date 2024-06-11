import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage_ReturnsNull() {
        // Arrange
        String message = null;
        
        // Act
        String result = HTMLFilter.filter(message);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testFilter_EmptyMessage_ReturnsEmptyString() {
        // Arrange
        String message = "";
        
        // Act
        String result = HTMLFilter.filter(message);
        
        // Assert
        assertEquals("", result);
    }
    
    @Test
    public void testFilter_MessageWithNoSpecialCharacters_ReturnsSameMessage() {
        // Arrange
        String message = "Hello, world!";
        
        // Act
        String result = HTMLFilter.filter(message);
        
        // Assert
        assertEquals("Hello, world!", result);
    }
    
    @Test
    public void testFilter_MessageWithOpeningTag_ReturnsFilteredMessage() {
        // Arrange
        String message = "<script>";
        
        // Act
        String result = HTMLFilter.filter(message);
        
        // Assert
        assertEquals("&lt;script&gt;", result);
    }
    
    @Test
    public void testFilter_MessageWithClosingTag_ReturnsFilteredMessage() {
        // Arrange
        String message = "</script>";
        
        // Act
        String result = HTMLFilter.filter(message);
        
        // Assert
        assertEquals("&lt;/script&gt;", result);
    }
    
    @Test
    public void testFilter_MessageWithAmpersand_ReturnsFilteredMessage() {
        // Arrange
        String message = "This & that";
        
        // Act
        String result = HTMLFilter.filter(message);
        
        // Assert
        assertEquals("This &amp; that", result);
    }
    
    @Test
    public void testFilter_MessageWithDoubleQuote_ReturnsFilteredMessage() {
        // Arrange
        String message = "He said, \"Hello\"";
        
        // Act
        String result = HTMLFilter.filter(message);
        
        // Assert
        assertEquals("He said, &quot;Hello&quot;", result);
    }
}