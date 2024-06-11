import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertTrue(Entities.isNamedEntity("quot"));
        Assert.assertFalse(Entities.isNamedEntity("invalidEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertFalse(Entities.isBaseNamedEntity("quot")); // not in base set
        Assert.assertFalse(Entities.isBaseNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        Character expected = '<';
        Character actual = Entities.getCharacterByName("lt");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEscape() {
        String input = "& < > &#x61; &amp; &quot; '&gt;";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String expected = "&amp; &lt; &gt; &#x61; &amp; &quot; &amp;gt;";
        String actual = Entities.escape(input, outputSettings);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEscape_xhtmlMode() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setEscapeMode(Entities.EscapeMode.xhtml);
        String input = "& < > &#x61; &amp; &quot; '&gt;";
        String expected = "&amp; &lt; &gt; &#x61; &amp; &quot;";
        String actual = Entities.escape(input, outputSettings);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEscape_nonAsciiCharacter() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String input = " TESTère ";
        String expected = " TEST&#xc3;ere ";
        String actual = Entities.escape(input, outputSettings);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscape_nullInput() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String input = null;
        Entities.escape(input, outputSettings);
    }

    @Test
    public void testUnescape() {
        String input = "&amp; &lt; &gt; &#x61; &amp; &quot; &amp;gt;";
        String expected = "& < > a &";
        String actual = Entities.unescape(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testUnescape_strictMode() {
        String input = "&amp; &lt; &gt; &#x61; &amp; &quot; &amp;gt; ";
        String expected = "&amp;lt; &amp;gt; a &amp;quot;";
        String actual = Entities.unescape(input, true);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescape_nullInput() {
        String input = null;
        Entities.unescape(input, false);
    }
}