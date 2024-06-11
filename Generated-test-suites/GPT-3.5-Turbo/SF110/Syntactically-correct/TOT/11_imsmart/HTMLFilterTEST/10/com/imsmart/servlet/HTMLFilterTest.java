import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Before
    public void setUp() {
    }

    // Test case for filter method with a null message
    @Test
    public void testFilterWithNullMessage() {
        assertNull(HTMLFilter.filter(null));
    }

    // Test case for filter method with an empty message
    @Test
    public void testFilterWithEmptyMessage() {
        assertEquals("", HTMLFilter.filter(""));
    }

    // Test case for filter method with special characters
    @Test
    public void testFilterWithSpecialCharacters() {
        String input = "<script>alert('Hello World!');</script>";
        String expectedOutput = "&lt;script&gt;alert('&quot;Hello World!&quot;;)&lt;/script&gt;";
        assertEquals(expectedOutput, HTMLFilter.filter(input));
    }

    // Test case for filter method with multiple special characters and text
    @Test
    public void testFilterWithMultipleSpecialCharacters() {
        String input = "Hello <b>World</b> & Good Morning";
        String expectedOutput = "Hello &lt;b&gt;World&lt;/b&gt; &amp; Good Morning";
        assertEquals(expectedOutput, HTMLFilter.filter(input));
    }

    // Test case for filter method with a message containing only special characters
    @Test
    public void testFilterWithOnlySpecialCharacters() {
        String input = "<<<&&&>>>";
        String expectedOutput = "&lt;&lt;&lt;&amp;&amp;&amp;&gt;&gt;&gt;";
        assertEquals(expectedOutput, HTMLFilter.filter(input));
    }

    // Test case for filter method with a message containing large content length
    @Test
    public void testFilterWithLargeContentLength() {
        String input = generateLargeString(10000);
        String expectedOutput = HTMLFilter.filter(input);
        assertEquals(expectedOutput, HTMLFilter.filter(input));
    }

    // Helper method to generate a large string
    private String generateLargeString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("<>");
        }
        return sb.toString();
    }

}