
import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter() {
        // Test null input
        String result = HTMLFilter.filter(null);
        Assert.assertNull(result);

        // Test empty input
        result = HTMLFilter.filter("");
        Assert.assertEquals("", result);

        // Test input without special characters
        result = HTMLFilter.filter("Hello World");
        Assert.assertEquals("Hello World", result);

        // Test input with '<' character
        result = HTMLFilter.filter("<p>");
        Assert.assertEquals("&lt;p&gt;", result);

        // Test input with '>' character
        result = HTMLFilter.filter(">");
        Assert.assertEquals("&gt;", result);

        // Test input with '&' character
        result = HTMLFilter.filter("AT&T");
        Assert.assertEquals("AT&amp;T", result);

        // Test input with '\"' character
        result = HTMLFilter.filter("I said \"Hello World\"");
        Assert.assertEquals("I said &quot;Hello World&quot;", result);

        // Test input with multiple special characters
        result = HTMLFilter.filter("<p>AT&T</p>");
        Assert.assertEquals("&lt;p&gt;AT&amp;T&lt;/p&gt;", result);
    }

}
