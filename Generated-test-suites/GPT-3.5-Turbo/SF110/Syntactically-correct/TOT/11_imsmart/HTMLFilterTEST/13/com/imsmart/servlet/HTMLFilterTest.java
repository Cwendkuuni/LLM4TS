import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage() {
        assertNull(HTMLFilter.filter(null));
    }
    
    @Test
    public void testFilter_EmptyMessage() {
        assertEquals("", HTMLFilter.filter(""));
    }
    
    @Test
    public void testFilter_NoSpecialCharacters() {
        String message = "This is a normal message.";
        assertEquals(message, HTMLFilter.filter(message));
    }
    
    @Test
    public void testFilter_SingleSpecialCharacterLessThan() {
        String message = "<";
        assertEquals("&lt;", HTMLFilter.filter(message));
    }
    
    @Test
    public void testFilter_SingleSpecialCharacterGreaterThan() {
        String message = ">";
        assertEquals("&gt;", HTMLFilter.filter(message));
    }
    
    @Test
    public void testFilter_SingleSpecialCharacterAmpersand() {
        String message = "&";
        assertEquals("&amp;", HTMLFilter.filter(message));
    }
    
    @Test
    public void testFilter_SingleSpecialCharacterDoubleQuote() {
        String message = "\"";
        assertEquals("&quot;", HTMLFilter.filter(message));
    }
    
    @Test
    public void testFilter_MultipleSpecialCharacters() {
        String message = "<> & \"";
        assertEquals("&lt;&gt; &amp; &quot;", HTMLFilter.filter(message));
    }
    
    @Test
    public void testFilter_LongMessage() {
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            messageBuilder.append("abcd<>\"");
        }
        String message = messageBuilder.toString();
        String expected = "";
        for (int i = 0; i < 10000; i++) {
            expected += "abcd&lt;&gt;&quot;";
        }
        assertEquals(expected, HTMLFilter.filter(message));
    }
    
    @Test
    public void testFilter_LargeMessage() {
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 0; i < 1000000; i++) {
            messageBuilder.append("a");
        }
        String message = messageBuilder.toString();
        String expected = "";
        for (int i = 0; i < 1000000; i++) {
            expected += "a";
        }
        assertEquals(expected, HTMLFilter.filter(message));
    }
}