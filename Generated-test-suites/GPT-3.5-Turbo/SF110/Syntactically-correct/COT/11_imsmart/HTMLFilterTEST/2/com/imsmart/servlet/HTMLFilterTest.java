import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage_ReturnsNull() {
        assertEquals(null, HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_EmptyMessage_ReturnsEmptyString() {
        assertEquals("", HTMLFilter.filter(""));
    }

    @Test
    public void testFilter_MessageWithNoSpecialCharacters_ReturnsSameMessage() {
        String message = "This is a test message.";
        assertEquals(message, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_MessageWithLeftAngleBracket_ReplacesWithEntity() {
        String message = "This message contains < symbol.";
        String expected = "This message contains &lt; symbol.";
        assertEquals(expected, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_MessageWithRightAngleBracket_ReplacesWithEntity() {
        String message = "This message contains > symbol.";
        String expected = "This message contains &gt; symbol.";
        assertEquals(expected, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_MessageWithAmpersand_ReplacesWithEntity() {
        String message = "This message contains & symbol.";
        String expected = "This message contains &amp; symbol.";
        assertEquals(expected, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_MessageWithDoubleQuotes_ReplacesWithEntity() {
        String message = "This message contains \" symbol.";
        String expected = "This message contains &quot; symbol.";
        assertEquals(expected, HTMLFilter.filter(message));
    }
    
    // Additional test cases for edge cases and exceptions

    @Test
    public void testFilter_MessageWithNewLineCharacter_ReplacesWithEntity() {
        String message = "This message contains\nnew line.";
        String expected = "This message contains<br/>new line.";
        assertEquals(expected, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_MessageWithSingleQuote_ReplacesWithEntity() {
        String message = "This message contains ' symbol.";
        String expected = "This message contains &#39; symbol.";
        assertEquals(expected, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_MessageWithCombinationOfCharacters_ReplacesWithEntities() {
        String message = "<This> &is& 'a' \"test\" message.";
        String expected = "&lt;This&gt; &amp;is&amp; &#39;a&#39; &quot;test&quot; message.";
        assertEquals(expected, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_MessageWithLongMatchPattern_ReplacesWithEntity() {
        String message = "This message contains >>>55<<< pattern.";
        String expected = "This message contains &gt;&gt;&gt;55&lt;&lt;&lt; pattern.";
        assertEquals(expected, HTMLFilter.filter(message));
    }
    
    // 

    // Add more test cases here

    
    // 
}