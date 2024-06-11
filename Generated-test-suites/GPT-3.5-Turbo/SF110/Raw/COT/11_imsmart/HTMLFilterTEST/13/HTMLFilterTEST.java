
import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter() {
        String input = "<script>alert('XSS')</script>";
        String expectedOutput = "&lt;script&gt;alert('XSS')&lt;/script&gt;";
        String actualOutput = HTMLFilter.filter(input);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testFilterWithNullInput() {
        assertNull(HTMLFilter.filter(null));
    }
    
    @Test
    public void testFilterWithEmptyString() {
        assertEquals("", HTMLFilter.filter(""));
    }
}
