import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HTMLFilterTest {
    
    @Test
    public void testFilter() {
        String input = "<script>alert('hello');</script>";
        String expected = "&lt;script&gt;alert(&apos;hello&apos;);&lt;/script&gt;";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }
}