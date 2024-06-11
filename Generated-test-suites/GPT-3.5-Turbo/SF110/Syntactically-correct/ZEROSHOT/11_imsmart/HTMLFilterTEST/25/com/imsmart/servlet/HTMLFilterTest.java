import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {
    
    @Test
    public void filter_NullMessage_ReturnsNull() {
        // Arrange
        String message = null;
        
        // Act
        String result = HTMLFilter.filter(message);
        
        // Assert
        Assert.assertNull(result);
    }
    
    @Test
    public void filter_EmptyMessage_ReturnsEmptyString() {
        // Arrange
        String message = "";
        
        // Act
        String result = HTMLFilter.filter(message);
        
        // Assert
        Assert.assertEquals("", result);
    }
    
    @Test
    public void filter_MessageWithNoHtmlTags_ReturnsOriginalMessage() {
        // Arrange
        String message = "This is a test message";
        
        // Act
        String result = HTMLFilter.filter(message);
        
        // Assert
        Assert.assertEquals("This is a test message", result);
    }
    
    @Test
    public void filter_MessageWithHtmlTags_ReturnsFilteredMessage() {
        // Arrange
        String message = "<b>This is a bold message</b>";
        
        // Act
        String result = HTMLFilter.filter(message);
        
        // Assert
        Assert.assertEquals("&lt;b&gt;This is a bold message&lt;/b&gt;", result);
    }
    
    @Test
    public void filter_MessageWithMultipleHtmlTags_ReturnsFilteredMessage() {
        // Arrange
        String message = "<b>This is a bold message</b> <i>This is an italic message</i>";
        
        // Act
        String result = HTMLFilter.filter(message);
        
        // Assert
        Assert.assertEquals("&lt;b&gt;This is a bold message&lt;/b&gt; &lt;i&gt;This is an italic message&lt;/i&gt;", result);
    }
    
    @Test
    public void filter_MessageWithOtherSpecialCharacters_ReturnsFilteredMessage() {
        // Arrange
        String message = "Special characters: < > &";
        
        // Act
        String result = HTMLFilter.filter(message);
        
        // Assert
        Assert.assertEquals("Special characters: &lt; &gt; &amp;", result);
    }
}