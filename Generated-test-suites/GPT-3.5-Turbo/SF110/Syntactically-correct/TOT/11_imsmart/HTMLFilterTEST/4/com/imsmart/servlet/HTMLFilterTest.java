
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullInput() {
        assertNull(HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_EmptyString() {
        assertEquals("", HTMLFilter.filter(""));
    }

    @Test
    public void testFilter_OnlySpecialCharacters() {
        String input = "<>&\"";
        String expectedOutput = "&lt;&gt;&amp;&quot;";
        assertEquals(expectedOutput, HTMLFilter.filter(input));
    }

    @Test
    public void testFilter_OnlyAlphanumeric() {
        String input = "abcd1234";
        assertEquals(input, HTMLFilter.filter(input));
    }

    @Test
    public void testFilter_MixedInput() {
        String input = "Hello <b>World</b>!";
        String expectedOutput = "Hello &lt;b&gt;World&lt;/b&gt;!";
        assertEquals(expectedOutput, HTMLFilter.filter(input));
    }
}
