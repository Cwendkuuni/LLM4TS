
import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_EmptyString() {
        String message = "";
        Assert.assertEquals("", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_NoSpecialCharacters() {
        String message = "This message contains no special characters.";
        Assert.assertEquals(message, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_LessThanCharacter() {
        String message = "This message contains a < character.";
        Assert.assertEquals("This message contains a &lt; character.", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_GreaterThanCharacter() {
        String message = "This message contains a > character.";
        Assert.assertEquals("This message contains a &gt; character.", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_AmpersandCharacter() {
        String message = "This message contains an & character.";
        Assert.assertEquals("This message contains an &amp; character.", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_QuoteCharacter() {
        String message = "This message contains a \" character.";
        Assert.assertEquals("This message contains a &quot; character.", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_CombinationOfSpecialCharacters() {
        String message = "This message contains <, >, &, and \" characters.";
        Assert.assertEquals("This message contains &lt;, &gt;, &amp;, and &quot; characters.",
                HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_NullMessage() {
        String message = null;
        Assert.assertNull(HTMLFilter.filter(message));
    }
}
