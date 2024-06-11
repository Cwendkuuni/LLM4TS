import static org.junit.Assert.*;
import org.junit.Test;

public class HTMLFilterTest {
    
    @Test
    public void testFilter_NullInput() {
        assertNull(HTMLFilter.filter(null));
    }
    
    @Test
    public void testFilter_EmptyInput() {
        assertEquals("", HTMLFilter.filter(""));
    }

    @Test
    public void testFilter_NoSpecialCharacters() {
        assertEquals("Hello World", HTMLFilter.filter("Hello World"));
    }

    @Test
    public void testFilter_WithSpecialCharacters() {
        assertEquals("&lt;&gt;&amp;&quot;", HTMLFilter.filter("<>&\""));
    }

    @Test
    public void testFilter_LongString() {
        String longString = "This is a long string with some special characters !@#$%^&*";
        assertEquals("This is a long string with some special characters !@#$%^&*", HTMLFilter.filter(longString));
    }

    @Test
    public void testFilter_LongStringWithSpecialCharacters() {
        String longString = "<>&\"!@#$%^&*()";
        assertEquals("&lt;&gt;&amp;&quot;!@#$%^&*()", HTMLFilter.filter(longString));
    }

    @Test
    public void testFilter_LargeInput() {
        String largeInput = "";
        for(int i = 0; i < 10000; i++) {
            largeInput += "a";
        }
        assertEquals(largeInput, HTMLFilter.filter(largeInput));
    }

    @Test
    public void testFilter_ExceptionHandling() {
        String message = null;
        try {
            HTMLFilter.filter(message);
            fail("Expected NullPointerException not thrown");
        } catch (NullPointerException e) {
            // Exception caught, test passed
        }
    }
}