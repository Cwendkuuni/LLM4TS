package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities.isNamedEntity("lt");
        Entities.isNamedEntity("amp");
        Entities.isNamedEntity("quot");
        // Check for unknown named entity
        Assert.assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities.isBaseNamedEntity("lt");
        Entities.isBaseNamedEntity("amp");
        Entities.isBaseNamedEntity("quot");
        // Check for unknown named entity in base set
        Assert.assertFalse(Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        Character character = Entities.getCharacterByName("lt");
        Character expectedCharacter = '<';
        Assert.assertEquals(expectedCharacter, character);

        character = Entities.getCharacterByName("amp");
        expectedCharacter = '&';
        Assert.assertEquals(expectedCharacter, character);

        character = Entities.getCharacterByName("quot");
        expectedCharacter = '\"';
        Assert.assertEquals(expectedCharacter, character);

        // Check for unknown named entity
        Assert.assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape_normalCase() {
        String input = "&amp;<lt>xml&gt;";
        String output = Entities.escape(input, null);
        String expectedOutput = "&amp;amp; &lt;lt&gt;xml&gt;";
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscape_attributeCase() {
        String input = "<xml attr='&amp;<lt>'>";
        String output = Entities.escape(input, null, true, false, false);
        String expectedOutput = "&amp;amp; &lt;lt&gt; xml attr='&amp;amp; &lt;lt&gt;'";
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscape_stripLeadingWhite() {
        String input = " xml &amp;&#xA0;<lt> &gt; ";
        String output = Entities.escape(input, null, false, false, true);
        String expectedOutput = " xml &amp;&#xA0; ";
        Assert.assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscape_nullInput() {
        String input = null;
        Entities.escape(input, null);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;amp; &lt;lt&gt; xml &amp;amp; &lt;lt&gt;";
        String output = Entities.unescape(input, false);
        String expectedOutput = "&amp;<lt>xml&gt;&amp;<lt>";
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testCanEncode() {
        Entities.canEncode(CoreCharset.ascii, 'a', null); // true
        Entities.canEncode(CoreCharset.ascii, 'ü', null); // false
        Entities.canEncode(CoreCharset.utf, 'ü', null); // true
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-full.properties");
        Character value = entities.get("quot");
        Assert.assertEquals('\"', value);
    }

    @Test(expected = MissingResourceException.class)
    public void testLoadEntities_missingFile() {
        Entities.loadEntities("missing-file.properties");
    }
}