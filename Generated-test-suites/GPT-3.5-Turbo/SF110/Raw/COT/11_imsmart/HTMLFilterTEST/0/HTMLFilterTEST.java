import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter() {
        // Basic test case
        String message = "<html>";
        String expected = "&lt;html&gt;";
        String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }

    @Test
    public void testFilter_NullMessage() {
        // Test case for null message input
        String message = null;
        String expected = null;
        String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }

    @Test
    public void testFilter_EmptyMessage() {
        // Test case for empty message input
        String message = "";
        String expected = "";
        String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }

    @Test
    public void testFilter_MessageWithTags() {
        // Test case for message containing HTML tags
        String message = "<h1>Hello, world!</h1>";
        String expected = "&lt;h1&gt;Hello, world!&lt;/h1&gt;";
        String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }

    @Test
    public void testFilter_MessageWithSpecialCharacters() {
        // Test case for message containing special characters
        String message = "Hello & \"World\"";
        String expected = "Hello &amp; &quot;World&quot;";
        String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }
}

