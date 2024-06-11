import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("quot"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertFalse(Entities.isNamedEntity("invalidEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(Entities.isBaseNamedEntity("quot"));
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(Entities.isBaseNamedEntity("gt"));
    }

    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals('<', Entities.getCharacterByName("lt"));
        Assert.assertEquals('&', Entities.getCharacterByName("amp"));
        Assert.assertEquals('"', Entities.getCharacterByName("quot"));
        Assert.assertNull(Entities.getCharacterByName("invalidEntity"));
    }

    @Test
    public void testEscapeMethod() {
        StringBuilder accum = new StringBuilder();
        String input = "&lt;<title&gt;Test&amp;amp;&quot; title&gt;";
        Entities.escape(accum, input, null, false, false, false);
        String expected = "&amp;lt;&lt;title&amp;amp;amp;amp;&amp;amp;amp;quot;&amp;quot; title&amp;gt;";
        Assert.assertEquals(expected, accum.toString());
    }

    @Test
    public void testUnescapeMethod() {
        String input = "&amp;lt;&lt;title&amp;amp;amp;amp;&amp;amp;amp;quot;&amp;quot; title&amp;gt;";
        String expected = "&lt;<title&gt;Test&amp;amp; &quot; title&gt;";
        String result = Entities.unescape(input, false);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeMethodWithInvalidInput() {
        String input = "&amp;lt;&lt;title&amp;amp;amp;amp;&amp;amp;amp;quot;&amp;quot; title&amp;gt;&amper;"; // invalid character "&amp;per;" is added intentionally
        Entities.unescape(input, false); // this will throw an IllegalArgumentException due to the invalid character
    }

    @Test
    public void testCanEncodeMethod() {
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, 'A'));
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, 'a'));
        Assert.assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, 'ñ'));
        Assert.assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'ñ'));
    }
}