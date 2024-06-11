package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        Assert.assertTrue(entities.isNamedEntity("lt"));
        Assert.assertTrue(entities.isNamedEntity("amp"));
        Assert.assertFalse(entities.isNamedEntity("invalidEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        Assert.assertTrue(entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(entities.isBaseNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        Assert.assertEquals('<', entities.getCharacterByName("lt"));
        Assert.assertEquals('&', entities.getCharacterByName("amp"));
        Assert.assertNull(entities.getCharacterByName("invalidEntity"));
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String originalString = "&lt;test&gt; &amp; &apos; &quot; &xA0; &lt;xml&gt;";
        String escapedString = entities.escape(originalString, outputSettings);

        String expectedEscapedString = "&amp;lt;test&amp;gt; &amp;amp; &amp;apos; &amp;quot; &amp;#xa0; &amp;lt;xml&amp;gt;";
        Assert.assertEquals(expectedEscapedString, escapedString);
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String escapedString = "&amp;lt;test&amp;gt; &amp;amp; &amp;apos; &amp;quot; &amp;#xa0; &amp;lt;xml&amp;gt;";
        String unescapedString = entities.unescape(escapedString);

        String expectedUnescapedString = "&lt;test&gt; &amp; &apos; &quot; " + (char) 160 + " &lt;xml&gt;";
        Assert.assertEquals(expectedUnescapedString, unescapedString);
    }
}