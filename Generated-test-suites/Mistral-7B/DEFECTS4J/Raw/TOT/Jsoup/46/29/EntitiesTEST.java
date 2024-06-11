package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<Character, String> fullMap = Entities.full;
        for (Map.Entry<Character, String> entry : fullMap.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            boolean expected = true;
            boolean actual = Entities.isNamedEntity(value);
            Assert.assertEquals("isNamedEntity test failed for entity: " + value, expected, actual);
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> baseMap = Entities.base;
        for (Map.Entry<Character, String> entry : baseMap.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            boolean expected = true;
            boolean actual = Entities.isBaseNamedEntity(value);
            Assert.assertEquals("isBaseNamedEntity test failed for entity: " + value, expected, actual);
        }

        for (Character key : Entities.xhtmlByVal.keySet()) {
            String value = Entities.xhtmlByVal.get(key);
            boolean expected = false;
            boolean actual = Entities.isBaseNamedEntity(value);
            Assert.assertEquals("isBaseNamedEntity test failed for entity: " + value, expected, actual);
        }
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, String> fullMap = Entities.full;
        for (Map.Entry<Character, String> entry : fullMap.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            Character expected = key;
            Character actual = Entities.getCharacterByName(value);
            Assert.assertEquals("getCharacterByName test failed for entity: " + value, expected, actual);
        }
    }

    @Test
    public void testEscape() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setEscapeMode(Entities.EscapeMode.xhtml);
        StringBuilder accum = new StringBuilder();

        String input = "This is a &lt;test&gt; for &amp;Entities&apos; class &quot;with some &quot;quoted text&quot; and special characters: &nbsp; &copy; &eacute; &ldquo; &rdquo; &ndash; &mdash; &ensp; &emsp; &lsquo; &rsquo;";
        Entities.escape(accum, input, outputSettings, false, false, false);
        String expected = "This is a &lt;test&gt; for &amp;amp;Entities&apos; class &quot;with some &quot;quoted text&quot; and special characters: &nbsp; &copy; &eacute; &ldquo; &rdquo; &ndash; &mdash; &ensp; &emsp; &lsquo; &rsquo;";
        String actual = accum.toString();
        Assert.assertEquals("escape test failed", expected, actual);

        outputSettings.setEscapeMode(Entities.EscapeMode.base);
        accum.setLength(0);
        Entities.escape(accum, input, outputSettings, false, false, false);
        actual = accum.toString();
        expected = "This is a &amp;lt;test&amp;gt; for &amp;amp;Entities&amp;amp;#0146;amp;#0154;amp;#0148;amp;#0147;amp;#0145;amp;#0146;amp;#0155;amp;#0151;amp;#0154;amp;#0154;amp;#0155;amp;#0151;amp;#0145;amp;#0145;";
        Assert.assertEquals("escape test failed with base mode", expected, actual);
    }

    @Test
    public void testUnescape() {
        String input = "This is a &lt;test&gt; for &amp;Entities&apos; class &quot;with some &quot;quoted text&quot; and special characters: &nbsp; &copy; &eacute; &ldquo; &rdquo; &ndash; &mdash; &ensp; &emsp; &lsquo; &rsquo;";
        String expected = "This is a <test> for &Entities; class \"with some \\\"quoted text\\\" and special characters: \u00A0 \u00A9 \u00EA \u201C \u201D \u2013 \u2014 \u2002 \u2003";
        String actual = Entities.unescape(input, false);
        Assert.assertEquals("unescape test failed", expected, actual);

        input = "This is a &lt;test&gt; for &amp;Entities&apos; class &quot;with some &quot;quoted text&quot; and special characters: &nbsp; &copy; &eacute; &ldquo; &rdquo; &ndash; &mdash; &ensp; &emsp; &lsquo; &rsquo;";
        expected = "This is a <test> for &Entities; class \"with some \\\"quoted text\\\" and special characters: \u00A0 \u00A9 \u00EA \u201C \u201D \u2013 \u2014 \u2002 \u2003";
        actual = Entities.unescape(input, true); // strict
        Assert.assertEquals("unescape test failed with strict mode", expected, actual);
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingEntitiesResource() {
        Entities.loadEntities("nonexistent.properties");
    }
}