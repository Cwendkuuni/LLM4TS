import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HTMLFilterTest {

    @Test
    public void testFilter_nullMessage_shouldReturnNull() {
        String filteredMessage = HTMLFilter.filter(null);
        assertNull(filteredMessage);
    }
    
    @Test
    public void testFilter_emptyMessage_shouldReturnEmptyString() {
        String filteredMessage = HTMLFilter.filter("");
        assertEquals("", filteredMessage);
    }
    
    @Test
    public void testFilter_noSpecialCharacters_shouldReturnSameMessage() {
        String message = "This is a test message.";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals(message, filteredMessage);
    }
    
    @Test
    public void testFilter_messageWithLessThanSymbol_shouldReplaceWithXmlEntity() {
        String message = "<Hello>";
        String expectedFilteredMessage = "&lt;Hello&gt;";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals(expectedFilteredMessage, filteredMessage);
    }
    
    @Test
    public void testFilter_messageWithGreaterThanSymbol_shouldReplaceWithXmlEntity() {
        String message = "This is > important.";
        String expectedFilteredMessage = "This is &gt; important.";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals(expectedFilteredMessage, filteredMessage);
    }
    
    @Test
    public void testFilter_messageWithAmpersandSymbol_shouldReplaceWithXmlEntity() {
        String message = "This & that.";
        String expectedFilteredMessage = "This &amp; that.";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals(expectedFilteredMessage, filteredMessage);
    }
    
    @Test
    public void testFilter_messageWithDoubleQuoteSymbol_shouldReplaceWithXmlEntity() {
        String message = "She said, \"Hello\" to him.";
        String expectedFilteredMessage = "She said, &quot;Hello&quot; to him.";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals(expectedFilteredMessage, filteredMessage);
    }
    
    @Test
    public void testFilter_messageWithMultipleSpecialCharacters_shouldReplaceAllWithXmlEntities() {
        String message = "<Hello> & \"world\".";
        String expectedFilteredMessage = "&lt;Hello&gt; &amp; &quot;world&quot;.";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals(expectedFilteredMessage, filteredMessage);
    }
    
}