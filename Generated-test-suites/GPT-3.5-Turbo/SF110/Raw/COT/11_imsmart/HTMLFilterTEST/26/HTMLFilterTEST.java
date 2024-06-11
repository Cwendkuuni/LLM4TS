import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {
    
    @Test
    public void testFilter() {
        String message = "<p>Hello World!</p>";
        String expected = "&lt;p&gt;Hello World!&lt;/p&gt;";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }
}