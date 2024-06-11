import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_WithNullMessage_ReturnsNull() {
        // Arrange
        String message = null;

        // Act
        String result = HTMLFilter.filter(message);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testFilter_WithEmptyMessage_ReturnsEmptyString() {
        // Arrange
        String message = "";

        // Act
        String result = HTMLFilter.filter(message);

        // Assert
        Assert.assertEquals("", result);
    }

    @Test
    public void testFilter_WithMessageContainingNoHTMLTags_ReturnsOriginalMessage() {
        // Arrange
        String message = "This is a test message without any HTML tags.";

        // Act
        String result = HTMLFilter.filter(message);

        // Assert
        Assert.assertEquals(message, result);
    }

    @Test
    public void testFilter_WithMessageContainingHTMLEncodableCharacters_ReturnsFilteredMessage() {
        // Arrange
        String message = "This message contains <, >, & and \" characters.";

        // Act
        String result = HTMLFilter.filter(message);

        // Assert
        Assert.assertEquals("This message contains &lt;, &gt;, &amp; and &quot; characters.", result);
    }

    @Test
    public void testFilter_WithMessageContainingMultipleInstancesOfHTMLTags_ReturnsFilteredMessage() {
        // Arrange
        String message = "<p>This is a <b>test</b> message</p>";

        // Act
        String result = HTMLFilter.filter(message);

        // Assert
        Assert.assertEquals("&lt;p&gt;This is a &lt;b&gt;test&lt;/b&gt; message&lt;/p&gt;", result);
    }
}