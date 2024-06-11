import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HTMLFilterTest {

    @Test 
    public void testFilter() {
        String message = "<p>This is a test message</p>";
        String expected = "&lt;p&gt;This is a test message&lt;/p&gt;";
        String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }

}