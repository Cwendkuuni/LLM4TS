import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter() {
        // Test case 1: null input
        Assert.assertNull(HTMLFilter.filter(null));

        // Test case 2: empty input
        Assert.assertEquals("", HTMLFilter.filter(""));

        // Test case 3: input with no special characters
        Assert.assertEquals("Hello", HTMLFilter.filter("Hello"));

        // Test case 4: input with special characters (<, >, &, ")
        Assert.assertEquals("&lt;Hello&gt;&amp;&quot;", HTMLFilter.filter("<Hello>&\""));

        // Test case 5: input with special characters in different order
        Assert.assertEquals("&amp;&quot;&lt;Hello&gt;", HTMLFilter.filter("&\"<Hello>"));
    }
}