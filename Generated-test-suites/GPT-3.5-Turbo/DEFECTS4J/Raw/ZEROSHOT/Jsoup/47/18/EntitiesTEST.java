import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertFalse(Entities.isBaseNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        Assert.assertNull(Entities.getCharacterByName("foo"));
    }

    @Test
    public void testEscape() {
        String string = "This is a test <foo> bar";
        String escapedString = Entities.escape(string, new Document.OutputSettings());
        Assert.assertEquals("This is a test &lt;foo&gt; bar", escapedString);
    }

    @Test
    public void testUnescape() {
        String string = "This is a test &lt;foo&gt; bar";
        String unescapedString = Entities.unescape(string);
        Assert.assertEquals("This is a test <foo> bar", unescapedString);
    }

    @Test
    public void testEscapeWithOutputSettings() {
        String string = "This is a test <foo> bar";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        String escapedString = Entities.escape(string, outputSettings);
        Assert.assertEquals("This is a test &lt;foo&gt; bar", escapedString);
    }

    @Test
    public void testEscapeWithNormaliseWhite() {
        String string = "This   is   a   test";
        String escapedString = Entities.escape(string, new Document.OutputSettings(), false, true, false);
        Assert.assertEquals("This is a test", escapedString);
    }

    @Test
    public void testEscapeWithStripLeadingWhite() {
        String string = "   This is a test";
        String escapedString = Entities.escape(string, new Document.OutputSettings(), false, false, true);
        Assert.assertEquals("This is a test", escapedString);
    }

    @Test
    public void testEscapeWithInAttribute() {
        String string = "This <a> is an attribute";
        String escapedString = Entities.escape(string, new Document.OutputSettings(), true, false, false);
        Assert.assertEquals("This <a> is an attribute", escapedString);
    }

    @Test
    public void testUnescapeWithStrict() {
        String string = "This is a test &lt;foo&gt; bar;";
        String unescapedString = Entities.unescape(string, true);
        Assert.assertEquals("This is a test <foo> bar", unescapedString);
    }

    @Test
    public void testUnescapeWithoutStrict() {
        String string = "This is a test &lt;foo&gt; bar";
        String unescapedString = Entities.unescape(string, false);
        Assert.assertEquals("This is a test <foo> bar", unescapedString);
    }

    @Test
    public void testCanEncodeWithAsciiCharset() {
        boolean canEncode = Entities.canEncode(Entities.CoreCharset.ascii, 'a', null);
        Assert.assertTrue(canEncode);
    }

    @Test
    public void testCanEncodeWithUtfCharset() {
        boolean canEncode = Entities.canEncode(Entities.CoreCharset.utf, 'a', null);
        Assert.assertTrue(canEncode);
    }

    @Test
    public void testCanEncodeWithFallbackCharset() {
        boolean canEncode = Entities.canEncode(Entities.CoreCharset.fallback, 'a', null);
        Assert.assertTrue(canEncode);
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        Assert.assertEquals(252, entities.size());
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = Entities.loadEntities("entities-base.properties");
        Map<Character, String> outMap = Entities.toCharacterKey(inMap);
        Assert.assertEquals(252, outMap.size());
    }
}