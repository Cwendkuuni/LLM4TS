package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            Assert.assertTrue("Named entity " + name + " should be known", Entities.isNamedEntity(name));
            Assert.assertEquals("Named entity " + name + " should map to " + character, character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> base = Entities.base;
        for (Map.Entry<String, Character> entry : base.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            Assert.assertTrue("Named entity " + name + " in base set should be known", Entities.isBaseNamedEntity(name));
            Assert.assertEquals("Named entity " + name + " in base set should map to " + character, character, Entities.getCharacterByName(name));
        }

        Assert.assertFalse("Named entity that is not in base set should not be recognized as base entity", Entities.isBaseNamedEntity("amp-amp"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            Assert.assertEquals("Named entity " + name + " should map to " + character, character, Entities.getCharacterByName(name));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNamedEntityNullName() {
        String nullName = null;
        Entities.isNamedEntity(nullName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharacterByNameNullName() {
        String nullName = null;
        Entities.getCharacterByName(nullName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharacterByNameUnknownName() {
        String unknownName = "unknown";
        Entities.getCharacterByName(unknownName);
    }

    @Test
    public void testEscapeBasic() {
        String input = "& example & < > \"";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String expected = "&amp; example &amp; &amp; &gt; &lt; &amp;quot;";
        String actual = Entities.escape(input, outputSettings);
        Assert.assertEquals("Escaped output should match expected", expected, actual);
    }

    @Test
    public void testEscapeNormalizeWhite() {
        String input = "\t\n & example \n \t< > \"";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String expected = "&nbsp;&nbsp;&nbsp; &amp; example &nbsp;&#xA0;&nbsp;<>&lt; &amp;quot;&quot;";
        String actual = Entities.escape(input, outputSettings);
        Assert.assertEquals("Escaped output should match expected", expected, actual);
    }

    @Test
    public void testEscapeStripLeadingWhite() {
        String input = " \t\n & example \n \t< > \"";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String expected = "&nbsp;&#xA0;&nbsp; &amp; example &nbsp;&#xA0;&nbsp;<>&lt; &amp;quot;&quot;";
        String actual = Entities.escape(input, outputSettings);
        Assert.assertEquals("Escaped output should match expected", expected, actual);
    }

    @Test
    public void testEscapeUnsupportedCharacter() {
        String input = "😀";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String expected = "&#x1F600;";
        String actual = Entities.escape(input, outputSettings);
        Assert.assertEquals("Escaped output should match expected", expected, actual);
    }

    @Test
    public void testEscapeSupportedCharacter() {
        String input = "😺";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String expected = "😺";
        String actual = Entities.escape(input, outputSettings);
        Assert.assertEquals("Escaped output should match expected", expected, actual);
    }

    @Test
    public void testEscapeSurrogatePair() {
        String input = "\uD835\uDD1E \uD835\uDD1E";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String expected = "\uD835\uDD1E\uD835\uDD1E";
        String actual = Entities.escape(input, outputSettings);
        Assert.assertEquals("Escaped output should match expected", expected, actual);
    }

    @Test
    public void testUnescapeBasic() {
        String input = "&amp; example &amp; &amp; &gt; &lt; &amp;quot;";
        String expected = "& example & &gt; < &quot;";
        String actual = Entities.unescape(input);
        Assert.assertEquals("Unescaped output should match expected", expected, actual);
    }

    @Test
    public void testUnescapeStrict() {
        String input = "&amp;example;&quot;";
        String expected = "&amp;example;&quot;";
        String actual = Entities.unescape(input, true);
        Assert.assertEquals("Unescaped output should match expected", expected, actual);
    }

    @Test
    public void testUnescapeNonStrict() {
        String input = "&amp;example&quot;";
        String expected = "&example";
        String actual = Entities.unescape(input, false);
        Assert.assertEquals("Unescaped output should match expected", expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeNullString() {
        String nullString = null;
        Entities.unescape(nullString);
    }

}