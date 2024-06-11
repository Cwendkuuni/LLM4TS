import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullInput_ReturnsNull() {
        String result = HTMLFilter.filter(null);
        Assert.assertNull(result);
    }

    @Test
    public void testFilter_EmptyString_ReturnsEmptyString() {
        String result = HTMLFilter.filter("");
        Assert.assertEquals("", result);
    }

    @Test
    public void testFilter_NoHTMLTags_ReturnsSameString() {
        String input = "This is a test string";
        String result = HTMLFilter.filter(input);
        Assert.assertEquals(input, result);
    }

    @Test
    public void testFilter_StringWithHTMLTags_ReturnsFilteredString() {
        String input = "<p>This is a <b>test</b> string</p>";
        String expectedOutput = "&lt;p&gt;This is a &lt;b&gt;test&lt;/b&gt; string&lt;/p&gt;";
        String result = HTMLFilter.filter(input);
        Assert.assertEquals(expectedOutput, result);
    }

    // Add more test methods to cover all possible scenarios

}