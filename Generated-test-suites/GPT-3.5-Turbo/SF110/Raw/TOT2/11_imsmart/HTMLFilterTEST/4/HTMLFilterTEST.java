import org.junit.*;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullInput_ReturnNull() {
        assertEquals(null, HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_NoSpecialCharacters_ReturnSameString() {
        String message = "This is a normal message";
        assertEquals(message, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsLessThanSymbol_FilteredCorrectly() {
        String message = "This contains < symbol";
        assertEquals("This contains &lt; symbol", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsGreaterThanSymbol_FilteredCorrectly() {
        String message = "This contains > symbol";
        assertEquals("This contains &gt; symbol", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsAmpersandSymbol_FilteredCorrectly() {
        String message = "This contains & symbol";
        assertEquals("This contains &amp; symbol", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsQuoteSymbol_FilteredCorrectly() {
        String message = "This contains \" symbol";
        assertEquals("This contains &quot; symbol", HTMLFilter.filter(message));
    }
    
    @Test
    public void testFilter_EmptyInput_ReturnEmptyString() {
        assertEquals("", HTMLFilter.filter(""));
    }
}