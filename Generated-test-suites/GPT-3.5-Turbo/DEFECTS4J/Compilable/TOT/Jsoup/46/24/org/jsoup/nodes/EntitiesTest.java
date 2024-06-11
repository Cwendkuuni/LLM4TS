package org.jsoup.nodes;
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Entities.EscapeMode;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Typical use case
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        // Edge case
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        // Error scenario
        Assert.assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Typical use case
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        // Edge case
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        // Error scenario
        Assert.assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        // Typical use case
        Assert.assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        // Edge case
        Assert.assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        // Error scenario
        Assert.assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        // Typical use case
        String escapedString = Entities.escape("<strong>HTML</strong>", new Document.OutputSettings());
        Assert.assertEquals("&lt;strong&gt;HTML&lt;/strong&gt;", escapedString);
        // Edge case
        escapedString = Entities.escape("€", new Document.OutputSettings());
        Assert.assertEquals("€", escapedString);
        // Error scenario
        escapedString = Entities.escape(null, new Document.OutputSettings());
        Assert.assertNotNull(escapedString);
    }

    @Test
    public void testUnescape() {
        // Typical use case
        String unescapedString = Entities.unescape("&lt;strong&gt;HTML&lt;/strong&gt;");
        Assert.assertEquals("<strong>HTML</strong>", unescapedString);
        // Edge case
        unescapedString = Entities.unescape("€");
        Assert.assertEquals("€", unescapedString);
        // Error scenario
        unescapedString = Entities.unescape(null);
        Assert.assertNotNull(unescapedString);
    }

    @Test
    public void testUnescapeStrict() {
        // Typical use case
        String unescapedString = Entities.unescape("&lt;strong&gt;HTML&lt;/strong&gt;", true);
        Assert.assertEquals("<strong>HTML</strong>", unescapedString);
        // Edge case
        unescapedString = Entities.unescape("€", true);
        Assert.assertEquals("€", unescapedString);
        // Error scenario
        unescapedString = Entities.unescape(null, true);
        Assert.assertNotNull(unescapedString);
    }

    @Test
    public void testEscapeModeGetXhtmlMap() {
        // Typical use case
        Assert.assertEquals(EscapeMode.xhtml.getMap(), Entities.EscapeMode.xhtml.getMap());
        // Edge case
        Assert.assertNotNull(EscapeMode.xhtml.getMap().get('<'));
        // Error scenario
        Assert.assertNull(EscapeMode.xhtml.getMap().get('x'));
    }
}