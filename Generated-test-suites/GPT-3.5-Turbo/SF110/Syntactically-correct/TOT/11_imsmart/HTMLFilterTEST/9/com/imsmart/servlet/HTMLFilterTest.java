import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage_ReturnsNull() {
        String message = null;
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals(null, filteredMessage);
    }
    
    @Test
    public void testFilter_EmptyMessage_ReturnsEmptyString() {
        String message = "";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals("", filteredMessage);
    }
    
    @Test
    public void testFilter_MessageWithNoSpecialCharacters_ReturnsSameMessage() {
        String message = "This is a test message";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals(message, filteredMessage);
    }
    
    @Test
    public void testFilter_MessageWithLessThan_ReturnsFilteredMessage() {
        String message = "This is < a test message";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals("This is &lt; a test message", filteredMessage);
    }
    
    @Test
    public void testFilter_MessageWithGreaterThan_ReturnsFilteredMessage() {
        String message = "This is > a test message";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals("This is &gt; a test message", filteredMessage);
    }
    
    @Test
    public void testFilter_MessageWithAmpersand_ReturnsFilteredMessage() {
        String message = "This is & a test message";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals("This is &amp; a test message", filteredMessage);
    }
    
    @Test
    public void testFilter_MessageWithDoubleQuote_ReturnsFilteredMessage() {
        String message = "This is \" a test message";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals("This is &quot; a test message", filteredMessage);
    }
    
    @Test
    public void testFilter_MessageWithAllSpecialCharacters_ReturnsFilteredMessage() {
        String message = "<This> is \"& a test message";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals("&lt;This&gt; is &quot;&amp; a test message", filteredMessage);
    }
    
    @Test
    public void testFilter_MessageWithDifferentSpecialCharacters_ReturnsFilteredMessage() {
        String message = "<This> is \"& <another> test message";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals("&lt;This&gt; is &quot;&amp; &lt;another&gt; test message", filteredMessage);
    }
    
    @Test
    public void testFilter_MessageWithSpecialCharacterAtEnd_ReturnsFilteredMessage() {
        String message = "This is a test message<";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals("This is a test message&lt;", filteredMessage);
    }
    
    @Test
    public void testFilter_MessageWithSpecialCharacterAtStart_ReturnsFilteredMessage() {
        String message = ">This is a test message";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals("&gt;This is a test message", filteredMessage);
    }
    
    @Test
    public void testFilter_MessageWithSpecialCharacterAtBothEnds_ReturnsFilteredMessage() {
        String message = "<This is a test message>";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals("&lt;This is a test message&gt;", filteredMessage);
    }
    
}