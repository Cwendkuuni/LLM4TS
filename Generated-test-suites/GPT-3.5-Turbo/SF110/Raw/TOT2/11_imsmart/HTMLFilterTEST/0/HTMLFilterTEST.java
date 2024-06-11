import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class HTMLFilterTest {
    
    @Test
    public void testNullMessage() {
        String result = HTMLFilter.filter(null);
        assertEquals(null, result);
    }
    
    @Test
    public void testEmptyMessage() {
        String result = HTMLFilter.filter("");
        assertEquals("", result);
    }
    
    @Test
    public void testHtmlCharacters() {
        String message = "<script>alert('Hello')</script>";
        String expected = "&lt;script&gt;alert(&#039;Hello&#039;)&lt;/script&gt;";
        String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }
    
    @Test
    public void testNoHtmlCharacters() {
        String message = "This is a plain message";
        String result = HTMLFilter.filter(message);
        assertEquals(message, result);
    }
    
    @Test
    public void testSpecialCharacters() {
        String message = "This is a message with & symbol";
        String expected = "This is a message with &amp; symbol";
        String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }
}