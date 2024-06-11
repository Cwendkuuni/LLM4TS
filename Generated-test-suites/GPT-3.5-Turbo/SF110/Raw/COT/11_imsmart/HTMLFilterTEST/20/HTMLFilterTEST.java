import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_WithNullMessage_ReturnsNull() {
        String message = null;
        String expected = null;
        String actual = HTMLFilter.filter(message);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFilter_WithEmptyMessage_ReturnsEmptyString() {
        String message = "";
        String expected = "";
        String actual = HTMLFilter.filter(message);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFilter_WithNoHTMLTags_ReturnsSameMessage() {
        String message = "This is a normal message without any HTML tags.";
        String expected = "This is a normal message without any HTML tags.";
        String actual = HTMLFilter.filter(message);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFilter_WithHTMLEncodedTags_ReturnsDecodedString() {
        String message = "&lt;p&gt;This is a &quot;para&quot; tag.&lt;/p&gt;";
        String expected = "<p>This is a \"para\" tag.</p>";
        String actual = HTMLFilter.filter(message);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFilter_WithMixOfTagsAndText_ReturnsFilteredString() {
        String message = "This is a &lt;b&gt;bold&lt;/b&gt; and &lt;i&gt;italic&lt;/i&gt; text.";
        String expected = "This is a <b>bold</b> and <i>italic</i> text.";
        String actual = HTMLFilter.filter(message);
        Assert.assertEquals(expected, actual);
    }

}