import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_withNullMessage_returnsNull() {
        String input = null;
        String expectedResult = null;

        String result = HTMLFilter.filter(input);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFilter_withEmptyMessage_returnsEmptyString() {
        String input = "";
        String expectedResult = "";

        String result = HTMLFilter.filter(input);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFilter_withNormalMessage_returnsFilteredResult() {
        String input = "<script>alert('Hello!');</script>";
        String expectedResult = "&lt;script&gt;alert(&#039;Hello!&#039;);&lt;/script&gt;";

        String result = HTMLFilter.filter(input);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFilter_withMessageContainingAllSymbolsToFilter_returnsFilteredResult() {
        String input = "<>&\"'";
        String expectedResult = "&lt;&gt;&amp;&quot;&#039;";

        String result = HTMLFilter.filter(input);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFilter_withMessageContainingNoSymbolsToFilter_returnsUnchangedMessage() {
        String input = "Hello World";
        String expectedResult = "Hello World";

        String result = HTMLFilter.filter(input);

        assertEquals(expectedResult, result);
    }
}