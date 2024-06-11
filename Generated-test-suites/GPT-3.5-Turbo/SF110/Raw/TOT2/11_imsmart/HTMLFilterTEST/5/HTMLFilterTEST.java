import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullInput_ReturnNull() {
        String result = HTMLFilter.filter(null);
        assertEquals(null, result);
    }

    @Test
    public void testFilter_NoSpecialCharacters_ReturnInput() {
        String input = "Hello World";
        String result = HTMLFilter.filter(input);
        assertEquals(input, result);
    }

    @Test
    public void testFilter_ContainsSpecialCharacters_ReturnFilteredString() {
        String input = "<p>Hello & Welcome</p>";
        String expected = "&lt;p&gt;Hello &amp; Welcome&lt;/p&gt;";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }

    @Test
    public void testFilter_EmptyInput_ReturnEmptyString() {
        String input = "";
        String result = HTMLFilter.filter(input);
        assertEquals("", result);
    }

}