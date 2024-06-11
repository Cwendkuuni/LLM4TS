package org.jsoup.nodes;
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Typical use case
        Assert.assertTrue(Entities.isNamedEntity("lt"));

        // Edge case
        Assert.assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Typical use case
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));

        // Edge case
        Assert.assertFalse(Entities.isBaseNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        // Typical use case
        Assert.assertEquals(Character.valueOf((char) 0x00022), Entities.getCharacterByName("quot"));

        // Edge case
        Assert.assertNull(Entities.getCharacterByName(""));
    }

    @Test
    public void testEscape() {
        // Typical use case
        String expected = "&amp;&lt;&gt;&quot;&#xa0;";
        String result = Entities.escape("&<>\" ", null);
        Assert.assertEquals(expected, result);

        // Edge case
        expected = "";
        result = Entities.escape("", null);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testUnescape() {
        // Typical use case
        String expected = "&<>";
        String result = Entities.unescape("&amp;&lt;&gt;");
        Assert.assertEquals(expected, result);

        // Edge case
        expected = "";
        result = Entities.unescape("");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testUnescapeStrict() {
        // Typical use case
        String expected = "&<>";
        String result = Entities.unescape("&amp;&lt;&gt;", true);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testEscapeModeXhtml() {
        // Typical use case
        Assert.assertEquals(Entities.EscapeMode.xhtml, Entities.EscapeMode.xhtml);

        // Edge case
        Assert.assertNotEquals(Entities.EscapeMode.xhtml, Entities.EscapeMode.base);
    }

    @Test
    public void testEscapeModeBase() {
        // Typical use case
        Assert.assertEquals(Entities.EscapeMode.base, Entities.EscapeMode.base);

        // Edge case
        Assert.assertNotEquals(Entities.EscapeMode.base, Entities.EscapeMode.xhtml);
    }

    @Test
    public void testEscapeModeExtended() {
        // Typical use case
        Assert.assertEquals(Entities.EscapeMode.extended, Entities.EscapeMode.extended);

        // Edge case
        Assert.assertNotEquals(Entities.EscapeMode.extended, Entities.EscapeMode.xhtml);
    }

}