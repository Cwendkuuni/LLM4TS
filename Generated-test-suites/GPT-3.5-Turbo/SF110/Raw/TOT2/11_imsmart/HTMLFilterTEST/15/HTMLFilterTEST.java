import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class HTMLFilterTest {
    
    @Test
    public void testFilter_basic() {
        String message = "<script>alert('Hello')</script>";
        String expected = "&lt;script&gt;alert('Hello')&lt;/script&gt;";
        assertEquals(expected, HTMLFilter.filter(message));
    }
    
    @Test
    public void testFilter_nullInput() {
        String message = null;
        assertEquals(null, HTMLFilter.filter(message));
    }
    
    @Test
    public void testFilter_emptyInput() {
        String message = "";
        assertEquals("", HTMLFilter.filter(message));
    }
    
    @Test
    public void testFilter_edgeCases() {
        String message = "<><&\"";
        String expected = "&lt;&gt;&amp;&quot;";
        assertEquals(expected, HTMLFilter.filter(message));
    }
    
    @Test
    public void testFilter_specialChars() {
        String message = "!@#$%^*()-_=+";
        assertEquals("!@#$%^*()-_=+", HTMLFilter.filter(message));
    }
    
    @Test
    public void testFilter_mixedChars() {
        String message = "Hello <b>World</b>!";
        String expected = "Hello &lt;b&gt;World&lt;/b&gt;!";
        assertEquals(expected, HTMLFilter.filter(message));
    }
}