import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class HTMLFilterTest {
    
    @Test
    public void testFilter_NullInput_ReturnsNull() {
        // Arrange
        String message = null;
        
        // Act
        String filteredMessage = HTMLFilter.filter(message);
        
        // Assert
        assertEquals(null, filteredMessage);
    }
    
    @Test
    public void testFilter_EmptyInput_ReturnsEmptyString() {
        // Arrange
        String message = "";
        
        // Act
        String filteredMessage = HTMLFilter.filter(message);
        
        // Assert
        assertEquals("", filteredMessage);
    }
    
    @Test
    public void testFilter_InputWithoutSpecialCharacters_ReturnsSameInput() {
        // Arrange
        String message = "This is a normal message.";
        
        // Act
        String filteredMessage = HTMLFilter.filter(message);
        
        // Assert
        assertEquals(message, filteredMessage);
    }

    @Test
    public void testFilter_InputWithLessThanSign_ReturnsFilteredMessage() {
        // Arrange
        String message = "This is < important.";
        String expected = "This is &lt; important.";
        
        // Act
        String filteredMessage = HTMLFilter.filter(message);
        
        // Assert
        assertEquals(expected, filteredMessage);
    }
    
    @Test
    public void testFilter_InputWithGreaterThanSign_ReturnsFilteredMessage() {
        // Arrange
        String message = "This is > important.";
        String expected = "This is &gt; important.";
        
        // Act
        String filteredMessage = HTMLFilter.filter(message);
        
        // Assert
        assertEquals(expected, filteredMessage);
    }
    
    @Test
    public void testFilter_InputWithAmpersand_ReturnsFilteredMessage() {
        // Arrange
        String message = "This is & important.";
        String expected = "This is &amp; important.";
        
        // Act
        String filteredMessage = HTMLFilter.filter(message);
        
        // Assert
        assertEquals(expected, filteredMessage);
    }
    
    @Test
    public void testFilter_InputWithDoubleQuote_ReturnsFilteredMessage() {
        // Arrange
        String message = "This is \"important\".";
        String expected = "This is &quot;important&quot;.";
        
        // Act
        String filteredMessage = HTMLFilter.filter(message);
        
        // Assert
        assertEquals(expected, filteredMessage);
    }
    
    @Test
    public void testFilter_InputWithAllSpecialCharacters_ReturnsFilteredMessage() {
        // Arrange
        String message = "<This> &is& \"important\".";
        String expected = "&lt;This&gt; &amp;is&amp; &quot;important&quot;.";
        
        // Act
        String filteredMessage = HTMLFilter.filter(message);
        
        // Assert
        assertEquals(expected, filteredMessage);
    }
    
    @Test
    public void testFilter_InputWithLargeContent_ReturnsFilteredMessage() {
        // Arrange
        String message = "This is a message with a very large content. This content is repeated multiple times to increase the size of the input.";
        String expected = "This is a message with a very large content. This content is repeated multiple times to increase the size of the input.";
        
        // Act
        String filteredMessage = HTMLFilter.filter(message);
        
        // Assert
        assertEquals(expected, filteredMessage);
    }

}