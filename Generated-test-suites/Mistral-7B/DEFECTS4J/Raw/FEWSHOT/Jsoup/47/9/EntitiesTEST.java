package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jsoup.nodes.Entities.CoreCharset;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.nodes.Entities.Entities;
import org.jsoup.nodes.Entities.Entities.EscapeMode.xhtmlByVal;
import org.jsoup.nodes.Entities.Entities.EscapeMode.baseByVal;
import org.jsoup.nodes.Entities.Entities.EscapeMode.fullByVal;
import org.jsoup.nodes.Entities.Entities.EscapeMode.xhtml;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> map = Entities.full;
        for (Map.Entry<String, Character> entry : map.entrySet()) {
            String key = entry.getKey();
            Character value = entry.getValue();
            testIsNamedEntity(key, value);
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> map = Entities.base;
        for (Map.Entry<String, Character> entry : map.entrySet()) {
            String key = entry.getKey();
            Character value = entry.getValue();
            testIsBaseNamedEntity(key, value);
        }
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> map = Entities.full;
        for (Map.Entry<String, Character> entry : map.entrySet()) {
            String key = entry.getKey();
            Character value = entry.getValue();
            testGetCharacterByName(key, value);
        }
    }

    @Test
    public void testEscapeModeXhtml() {
        Map<Character, String> map = xhtml.getMap();
        for (Map.Entry<Character, String> entry : map.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            testEscapeModeXhtml(key, value);
        }
    }

    @Test
    public void testEscapeModeBase() {
        Map<Character, String> map = base.getMap();
        for (Map.Entry<Character, String> entry : map.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            testEscapeModeBase(key, value);
        }
    }

    @Test
    public void testEscapeModeExtended() {
        Map<Character, String> map = extended.getMap();
        for (Map.Entry<Character, String> entry : map.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            testEscapeModeExtended(key, value);
        }
    }

    @Test
    public void testIsNamedEntityWithNull() {
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testIsBaseNamedEntityWithNull() {
        assertFalse(Entities.isBaseNamedEntity(null));
    }

    @Test
    public void testGetCharacterByNameWithNull() {
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        String input = "&lt;foo&gt; &amp; quot &quot; &apos; &quot; &apos; &quot; &amp; &lt; &gt; &quot; &apos; &quot;\n&nbsp; &nbsp; &nbsp;";
        String expected = "&amp;lt;foo&gt;&amp;amp; quot&#x22; &quot;&apos; &quot;&apos; &quot;&apos; &quot;&apos; &quot;&amp;amp; &amp;lt; &amp;gt; &quot;&apos; &quot;&quot; &amp;#x2002; &amp;#x2002; &amp;#x2002;";
        String result = Entities.escape(input, out);
        assertEquals(expected, result);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;foo&gt;&amp;amp; quot&#x22; &quot;&apos; &quot;&apos; &quot;&apos; &quot;&apos; &quot;&amp;amp; &amp;lt; &amp;gt; &quot;&apos; &quot;&quot; &amp;#x2002; &amp;#x2002; &amp;#x2002;";
        String expected = "&lt;foo&gt; &amp; quot &quot; &apos; &quot; &apos; &quot; &apos; &quot; &amp; &lt; &gt; &quot; &apos; &quot;";
        String result = Entities.unescape(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCanEncode() {
        Map<Character, String> map = Entities.full;
        for (Map.Entry<Character, String> entry : map.entrySet()) {
            Character key = entry.getKey();
            Character character = key;
            String codePoint = Integer.toString(Character.codePointAt(character), 16);
            testCanEncode(character, codePoint);
        }
    }

    private void testIsNamedEntity(String key, Character value) {
        assertTrue(Entities.isNamedEntity(key));
        assertSame(value, Entities.getCharacterByName(key));
    }

    private void testIsBaseNamedEntity(String key, Character value) {
        assertTrue(Entities.isBaseNamedEntity(key));
        assertSame(value, Entities.getCharacterByName(key));
    }

    private void testGetCharacterByName(String key, Character value) {
        assertEquals(value, Entities.getCharacterByName(key));
    }

    private void testEscapeModeXhtml(Character key, String value) {
        Map<Character, String> map = xhtml.getMap();
        assertEquals(value, map.get(key));
    }

    private void testEscapeModeBase(Character key, String value) {
        Map<Character, String> map = base.getMap();
        assertEquals(value, map.get(key));
    }

    private void testEscapeModeExtended(Character key, String value) {
        Map<Character, String> map = extended.getMap();
        assertEquals(value, map.get(key));
    }

    private void testCanEncode(Character character, String codePoint) {
        CoreCharset charset = CoreCharset.utf;
        CharsetEncoder encoder = Character.encodingFor(character).newEncoder();
        assertTrue(Entities.canEncode(charset, character, encoder));
        String expected = codePoint.substring(1, codePoint.length());
        assertEquals(expected, Character.toString(character));
    }

}