import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter() {
        // Test with null input
        Assert.assertNull(HTMLFilter.filter(null));

        // Test with empty input
        Assert.assertEquals("", HTMLFilter.filter(""));

        // Test with HTML special characters
        Assert.assertEquals("&lt;", HTMLFilter.filter("<"));
        Assert.assertEquals("&gt;", HTMLFilter.filter(">"));
        Assert.assertEquals("&amp;", HTMLFilter.filter("&"));
        Assert.assertEquals("&quot;", HTMLFilter.filter("\""));

        // Test with a combination of HTML special characters
        Assert.assertEquals("&lt;&gt;&amp;&quot;", HTMLFilter.filter("<>&\""));

        // Test with normal text
        Assert.assertEquals("Hello World", HTMLFilter.filter("Hello World"));
    }
}