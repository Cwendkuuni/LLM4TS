import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage_ReturnNull() {
        String filtered = HTMLFilter.filter(null);
        Assert.assertNull(filtered);
    }

    @Test
    public void testFilter_EmptyMessage_ReturnEmptyString() {
        String filtered = HTMLFilter.filter("");
        Assert.assertEquals("", filtered);
    }

    @Test
    public void testFilter_NoHTMLCharacters_ReturnOriginalString() {
        String message = "This is a test";
        String filtered = HTMLFilter.filter(message);
        Assert.assertEquals(message, filtered);
    }

    @Test
    public void testFilter_HTMLEncodeLessThanCharacter_ReturnEncodedResult() {
        String message = "5 < 10";
        String expected = "5 &lt; 10";
        String filtered = HTMLFilter.filter(message);
        Assert.assertEquals(expected, filtered);
    }

    @Test
    public void testFilter_HTMLEncodeGreaterThanCharacter_ReturnEncodedResult() {
        String message = "5 > 10";
        String expected = "5 &gt; 10";
        String filtered = HTMLFilter.filter(message);
        Assert.assertEquals(expected, filtered);
    }

    @Test
    public void testFilter_HTMLEncodeAmpersandCharacter_ReturnEncodedResult() {
        String message = "5 & 10";
        String expected = "5 &amp; 10";
        String filtered = HTMLFilter.filter(message);
        Assert.assertEquals(expected, filtered);
    }

    @Test
    public void testFilter_HTMLEncodeDoubleQuoteCharacter_ReturnEncodedResult() {
        String message = "5 \" 10";
        String expected = "5 &quot; 10";
        String filtered = HTMLFilter.filter(message);
        Assert.assertEquals(expected, filtered);
    }
}