import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullInput() {
        String result = HTMLFilter.filter(null);
        assertEquals("Null input should return null",null,result);
    }

    @Test
    public void testFilter_EmptyInput() {
        String result = HTMLFilter.filter("");
        assertEquals("Empty input should return empty string","",result);
    }

    @Test
    public void testFilter_NoSpecialCharacters() {
        String input = "This is a test message";
        String result = HTMLFilter.filter(input);
        assertEquals("No special characters should be modified",input,result);
    }

    @Test
    public void testFilter_LessThanSymbol() {
        String input = "This is less than < symbol";
        String expected = "This is less than &lt; symbol";
        String result = HTMLFilter.filter(input);
        assertEquals("Less than symbol should be converted correctly",expected,result);
    }

    @Test
    public void testFilter_GreaterThanSymbol() {
        String input = "This is greater than > symbol";
        String expected = "This is greater than &gt; symbol";
        String result = HTMLFilter.filter(input);
        assertEquals("Greater than symbol should be converted correctly",expected,result);
    }

    @Test
    public void testFilter_AmpersandSymbol() {
        String input = "This is an ampersand & symbol";
        String expected = "This is an ampersand &amp; symbol";
        String result = HTMLFilter.filter(input);
        assertEquals("Ampersand symbol should be converted correctly",expected,result);
    }

    @Test
    public void testFilter_QuoteSymbol() {
        String input = "This is a quote \" symbol";
        String expected = "This is a quote &quot; symbol";
        String result = HTMLFilter.filter(input);
        assertEquals("Quote symbol should be converted correctly",expected,result);
    }

    @Test
    public void testFilter_AllSpecialCharacters() {
        String input = "<>&\"";
        String expected = "&lt;&gt;&amp;&quot;";
        String result = HTMLFilter.filter(input);
        assertEquals("All special characters should be converted correctly",expected,result);
    }
}