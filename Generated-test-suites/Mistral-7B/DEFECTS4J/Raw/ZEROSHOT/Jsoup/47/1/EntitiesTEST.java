package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test the isNamedEntity method for various named entities
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("gt"));
        Assert.assertTrue(Entities.isNamedEntity("quot"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertTrue(Entities.isNamedEntity("nbsp"));
        Assert.assertFalse(Entities.isNamedEntity("unkownEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test the isBaseNamedEntity method for various named entities
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(Entities.isBaseNamedEntity("gt"));
        Assert.assertFalse(Entities.isBaseNamedEntity("quot"));
        Assert.assertFalse(Entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(Entities.isBaseNamedEntity("nbsp"));
    }

    @Test
    public void testGetCharacterByName() {
        // Test the getCharacterByName method for various named entities
        Assert.assertEquals('<', Entities.getCharacterByName("lt"));
        Assert.assertEquals('>', Entities.getCharacterByName("gt"));
        Assert.assertEquals('"', Entities.getCharacterByName("quot"));
        Assert.assertEquals('&', Entities.getCharacterByName("amp"));
        Assert.assertNull(Entities.getCharacterByName("unkownEntity"));
    }

    @Test
    public void testEscape() {
        // Test the escape method with different inputs
        String string = "&lt;example&gt; <span style=\"color: red;\">hello&quot; world&amp;</span>";
        Document.OutputSettings out = new Document.OutputSettings();
        StringBuilder expected = new StringBuilder();
        expected.append("&amp;lt;example&amp;gt; &lt;span style=\"color: red;\">&amp;quot; &amp;hello&amp;quot; &amp;world&amp;amp;;</span>");

        StringBuilder actual = new StringBuilder(Entities.escape(string, out));

        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testUnescape() {
        // Test the unescape method with different inputs
        String string = "&amp;lt;example&amp;gt; &lt;span style=\"color: red;\">&amp;quot; &amp;hello&amp;quot; &amp;world&amp;amp;;</span>";

        String actual = Entities.unescape(string);

        String expected = "&lt;example&gt; <span style=\"color: red;\">hello\" world</span>";

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCanEncode() {
        // Test the canEncode method with various characters
        CoreCharset charset = CoreCharset.utf;
        CharsetEncoder encoder = null; // not used in this test

        for (char character = 0; character <= 127; character++) {
            boolean result = Entities.canEncode(charset, character, encoder);
            Assert.assertTrue(result);
        }

        char surrogate1 = 0xD800;
        char surrogate2 = 0xDC00;
        char character = Character.toCodePoint(surrogate1, surrogate2);

        boolean result = Entities.canEncode(charset, character, encoder);
        Assert.assertFalse(result);
    }
}