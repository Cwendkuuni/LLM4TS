import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter() {
        // Test case 1: message is null
        String message1 = null;
        Assert.assertNull(HTMLFilter.filter(message1));

        // Test case 2: message contains no special characters
        String message2 = "Hello World";
        Assert.assertEquals("Hello World", HTMLFilter.filter(message2));

        // Test case 3: message contains '<' character
        String message3 = "Hello < World";
        Assert.assertEquals("Hello &lt; World", HTMLFilter.filter(message3));

        // Test case 4: message contains '>' character
        String message4 = "Hello > World";
        Assert.assertEquals("Hello &gt; World", HTMLFilter.filter(message4));

        // Test case 5: message contains '&' character
        String message5 = "Hello & World";
        Assert.assertEquals("Hello &amp; World", HTMLFilter.filter(message5));

        // Test case 6: message contains '\"' character
        String message6 = "Hello \" World";
        Assert.assertEquals("Hello &quot; World", HTMLFilter.filter(message6));
    }
}