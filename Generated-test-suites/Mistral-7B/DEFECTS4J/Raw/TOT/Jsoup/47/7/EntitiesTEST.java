import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test for known named entities
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertFalse(Entities.isNamedEntity("unknown"));

        // Test for empty and null input
        Assert.assertFalse(Entities.isNamedEntity(null));
        Assert.assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test for known base named entities
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(Entities.isBaseNamedEntity("unknown"));

        // Test for empty and null input
        Assert.assertFalse(Entities.isBaseNamedEntity(null));
        Assert.assertFalse(Entities.isBaseNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        Character expected = Entities.getCharacterByName("quot");
        Assert.assertEquals(expected, '\u00022');

        expected = Entities.getCharacterByName("amp");
        Assert.assertEquals(expected, '\u00026');

        expected = Entities.getCharacterByName("lt");
        Assert.assertEquals(expected, '\u0003C');

        expected = Entities.getCharacterByName("gt");
        Assert.assertEquals(expected, '\u0003E');

        expected = Entities.getCharacterByName("unknown");
        Assert.assertNull(expected);
    }

    @Test
    public void testEscapeMethod() {
        String input = "This is a test for the &lt;Entities&gt; class.";
        String expected = "This is a test for the &amp;lt;Entities&gt; class.";

        StringBuilder accumulator = new StringBuilder();
        Entities.escape(accumulator, input, Document.OutputSettings.base(), false, false, false);

        Assert.assertEquals(expected, accumulator.toString());

        // Test with XHTML output
        Document.OutputSettings xhtmlOutput = new Document.OutputSettings();
        xhtmlOutput.escapeMode(Entities.EscapeMode.xhtml());

        accumulator = new StringBuilder();
        Entities.escape(accumulator, input, xhtmlOutput, false, false, false);

        expected = "This is a test for the &amp;lt;Entities&gt;&#x26;amp;#x3B;Entities&gt;";
        Assert.assertEquals(expected, accumulator.toString());
    }

    @Test
    public void testUnescapeMethod() {
        String input = "This is a test for the &amp;lt;Entities&gt;&#x26;amp;#x3B;Entities&gt; class.";
        String expected = "This is a test for the <Entities> class.";

        String result = Entities.unescape(input, false);
        Assert.assertEquals(expected, result);

        expected = "This is a test for the <Entities> class.";
        result = Entities.unescape(Entities.escape(expected, Document.OutputSettings.base()), false);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanEncodeWithInvalidCharacterSet() {
        Entities.canEncode(null, '\u00001', null);
    }
}