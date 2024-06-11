package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.loadEntities("entities-full.properties");
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String name = entry.getKey();
            Character charValue = entry.getValue();
            assertTrue(Entities.isNamedEntity(name));
            assertEquals(charValue, Entities.getCharacterByName(name));
        }

        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> fullEntities = Entities.loadEntities("entities-full.properties");
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String name = entry.getKey();
            Character charValue = entry.getValue();
            assertEquals(charValue, Entities.getCharacterByName(name));
        }

        assertNull(Entities.getCharacterByName(null));
        assertNull(Entities.getCharacterByName(""));
    }

    @Test
    public void testEscape() {
        String input = "&lt; &gt; &amp; &apos; &quot; &eacute; &nbsp; &iexcl; &cent; &pound;";
        EscapeMode escapeMode = Entities.EscapeMode.base;
        String expectedOutput = "&amp;lt;&amp;gt;&amp;amp;&amp;apos;&amp;quot;&amp;#233;&amp;#160;&amp;#191;&amp;#163;&#163;";
        String output = Entities.escape(input, null, escapeMode);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;&amp;gt;&amp;amp;&amp;apos;&amp;quot;&amp;#233;&amp;#160;&amp;#191;&amp;#163;&#163;";
        String output = Entities.unescape(input);
        String expectedOutput = "&lt;>&gt;&amp;&apos;&quot;&eacute;&nbsp;&iexcl;&cent;&#163;&#163;";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescapeWithStrict() {
        String input = "&amp;lt;&amp;gt;&amp;amp;&amp;apos;&amp;quot;&amp;#233;&amp;#160;&amp;#191;&amp;#163;&#163;";
        String output = Entities.unescape(input, true);
        String expectedOutput = "&lt;>&gt;&amp;&apos;&quot;&eacute;&nbsp;&iexcl;&cent;&#163;&#163;";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testIsNamedEntity_XHTML_Entities() {
        Map<Character, String> xhtmlEntities = Entities.EscapeMode.xhtml.getMap();
        for (Map.Entry<Character, String> entry : xhtmlEntities.entrySet()) {
            Character charValue = entry.getKey();
            String name = entry.getValue();
            assertTrue(Entities.isNamedEntity(name));
            assertEquals(charValue, Entities.getCharacterByName(name));
        }

        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName_XHTML_Entities() {
        Map<Character, String> xhtmlEntities = Entities.EscapeMode.xhtml.getMap();
        for (Map.Entry<Character, String> entry : xhtmlEntities.entrySet()) {
            Character charValue = entry.getKey();
            String name = entry.getValue();
            assertEquals(charValue, Entities.getCharacterByName(name));
        }

        assertNull(Entities.getCharacterByName(null));
        assertNull(Entities.getCharacterByName(""));
    }
}