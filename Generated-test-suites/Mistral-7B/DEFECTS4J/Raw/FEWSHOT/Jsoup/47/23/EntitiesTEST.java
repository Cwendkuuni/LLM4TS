package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Entities;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        for (Map.Entry<Character, String> entry : Entities.full.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue().toString();
            assertTrue(Entities.isNamedEntity(value));
            assertTrue(Entities.isNamedEntity(String.valueOf(key)));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        for (Map.Entry<Character, String> entry : Entities.base.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue().toString();
            assertTrue(Entities.isBaseNamedEntity(value));
            assertTrue(Entities.isBaseNamedEntity(String.valueOf(key)));
        }
    }

    @Test
    public void testGetCharacterByName() {
        for (Map.Entry<Character, String> entry : Entities.full.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue().toString();
            assertEquals(key, Entities.getCharacterByName(value));
            assertEquals(key, Entities.getCharacterByName(String.valueOf(value[0])));
        }
    }

    @Test
    public void testEscape() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String input = "This is & a & test & with & quotes: \"single\" & 'double' & < > & & & &nbsp; & &#160; & &amp; & &#x26;";

        String escaped = Entities.escape(input, outputSettings);
        assertEquals("This is &amp; a &amp; &test;&amp; with &amp; quotes: &quot;single&quot; &amp; &amp; &amp;nbsp; &amp;#160; &amp; &amp;amp; &amp;#x26;", escaped);

        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        escaped = Entities.escape(input, outputSettings);
        assertEquals("This is &amp; a &amp; test &amp; with &quot;single&quot; &amp; 'double' &amp; < &amp; > &amp; &amp; &nbsp; &amp;#xA0; &amp; &amp;amp; &amp;#x26;", escaped);
    }

    @Test
    public void testUnescape() {
        String input = "This is &amp; a &quot;test&quot; &apos;apostrophe&apos; &lt; &gt; &amp; &amp; &nbsp; &amp;#x26; &amp;#x2E; &amp;#xA0;";

        String unescaped = Entities.unescape(input);
        assertEquals("This is an a test apostrophe < > & & &nbsp; &amp; &amp; &nbsp;", unescaped);
    }

    @Test
    public void testCanEncode() {
        for (char c = 0; c < 0xFFFF; c++) {
            CoreCharset coreCharset = Entities.CoreCharset.utf;
            CharsetEncoder encoder = java.nio.charset.StandardCharsets.UTF_8.newEncoder();
            assertTrue(Entities.canEncode(coreCharset, c, encoder));
        }

        for (char c = 0; c < 0x10000; c++) {
            CoreCharset coreCharset = Entities.CoreCharset.utf;
            CharsetEncoder encoder = java.nio.charset.StandardCharsets.UTF_8.newEncoder();
            assertFalse(Entities.canEncode(coreCharset, c, encoder));
        }

        for (char c = 0xD800; c <= 0xDFFF; c++) {
            CoreCharset coreCharset = Entities.CoreCharset.utf;
            CharsetEncoder encoder = java.nio.charset.StandardCharsets.UTF_8.newEncoder();
            assertFalse(Entities.canEncode(coreCharset, c, encoder));
        }

        for (char c = 0; c < 0x10FFFF; c++) {
            CoreCharset coreCharset = Entities.CoreCharset.fallback;
            CharsetEncoder encoder = java.nio.charset.StandardCharsets.UTF_8.newEncoder();
            assertTrue(Entities.canEncode(coreCharset, c, encoder));
        }
    }
}