import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullInput_ReturnsNull() {
        String filteredMessage = HTMLFilter.filter(null);
        assertEquals(null, filteredMessage);
    }

    @Test
    public void testFilter_EmptyString_ReturnsEmptyString() {
        String filteredMessage = HTMLFilter.filter("");
        assertEquals("", filteredMessage);
    }

    @Test
    public void testFilter_NoSpecialCharacters_ReturnsSameString() {
        String message = "No special characters";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals(message, filteredMessage);
    }

    @Test
    public void testFilter_ContainsTag() {
        String message = "Hello <b>World</b>";
        String filteredMessage = HTMLFilter.filter(message);
        String expectedResult = "Hello &lt;b&gt;World&lt;/b&gt;";
        assertEquals(expectedResult, filteredMessage);
    }

    @Test
    public void testFilter_ContainsQuotationMarks() {
        String message = "Quotation \"marks\"";
        String filteredMessage = HTMLFilter.filter(message);
        String expectedResult = "Quotation &quot;marks&quot;";
        assertEquals(expectedResult, filteredMessage);
    }

    @Test
    public void testFilter_ContainsAmpersand() {
        String message = "Ampersand & test";
        String filteredMessage = HTMLFilter.filter(message);
        String expectedResult = "Ampersand &amp; test";
        assertEquals(expectedResult, filteredMessage);
    }

    @Test
    public void testFilter_ContainsLessThanAndGreaterThan() {
        String message = "4 < 5 > 3";
        String filteredMessage = HTMLFilter.filter(message);
        String expectedResult = "4 &lt; 5 &gt; 3";
        assertEquals(expectedResult, filteredMessage);
    }

    @Test
    public void testFilter_ContainsAllSpecialCharacters() {
        String message = "<>&\"";
        String filteredMessage = HTMLFilter.filter(message);
        String expectedResult = "&lt;&gt;&amp;&quot;";
        assertEquals(expectedResult, filteredMessage);
    }

    // Edge Cases

    @Test
    public void testFilter_LargeInput() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("<tag>");
        }
        String message = sb.toString();
        String filteredMessage = HTMLFilter.filter(message);
        StringBuilder expectedSb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            expectedSb.append("&lt;tag&gt;");
        }
        String expectedResult = expectedSb.toString();
        assertEquals(expectedResult, filteredMessage);
    }

    @Test
    public void testFilter_ContainsNullCharacter() {
        String message = "Null \0 character";
        String filteredMessage = HTMLFilter.filter(message);
        String expectedResult = "Null \0 character";
        assertEquals(expectedResult, filteredMessage);
    }

    // Error Scenarios

    @Test(expected = NullPointerException.class)
    public void testFilter_ThrowsNullPointerException() {
        HTMLFilter.filter(message);
    }
    
}