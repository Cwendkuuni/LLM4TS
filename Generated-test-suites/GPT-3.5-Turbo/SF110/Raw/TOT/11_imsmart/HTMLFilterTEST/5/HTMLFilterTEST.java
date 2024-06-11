import static org.junit.Assert.*;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage_ReturnsNull() {
        assertEquals(null, HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_EmptyMessage_ReturnsEmpty() {
        assertEquals("", HTMLFilter.filter(""));
    }

    @Test
    public void testFilter_NoSpecialCharacters_ReturnsSameMessage() {
        String message = "Hello, World!";
        assertEquals(message, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_MessageWithSpecialCharacters_ReturnsFilteredMessage() {
        String message = "<script>alert('Hello, World!');</script>";
        String filteredMessage = "&lt;script&gt;alert(&#39;Hello, World!&#39;);&lt;/script&gt;";
        assertEquals(filteredMessage, HTMLFilter.filter(message));
    }

}