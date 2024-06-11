package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('\'', Entities.getCharacterByName("apos"));
        assertEquals('"', Entities.getCharacterByName("quot"));
    }

    @Test
    public void testXhtmlMap() {
        Map<Character, String> xhtmlMap = Entities.EscapeMode.xhtml.getMap();
        assertEquals(5, xhtmlMap.size());
        assertEquals("quot", xhtmlMap.get((char) 0x00022));
        assertEquals("amp", xhtmlMap.get((char) 0x00026));
        assertEquals("apos", xhtmlMap.get((char) 0x00027));
        assertEquals("lt", xhtmlMap.get((char) 0x0003C));
        assertEquals("gt", xhtmlMap.get((char) 0x0003E));
    }

    @Test
    public void testBaseMap() {
        Map<Character, String> baseMap = Entities.EscapeMode.base.getMap();
        assertEquals(5714, baseMap.size());
        // Some checks for known entities
        assertEquals("quot", baseMap.get((char) 0x00022));
        assertEquals("amp", baseMap.get((char) 0x00026));
        assertEquals("apos", baseMap.get((char) 0x00027));
        assertEquals("lt", baseMap.get((char) 0x0003C));
        assertEquals("gt", baseMap.get((char) 0x0003E));
    }

    @Test
    public void testExtendedMap() {
        Map<Character, String> extendedMap = Entities.EscapeMode.extended.getMap();
        assertEquals(10237, extendedMap.size());
        // Some checks for known entities
        assertEquals("quot", extendedMap.get((char) 0x00022));
        assertEquals("amp", extendedMap.get((char) 0x00026));
        assertEquals("apos", extendedMap.get((char) 0x00027));
        assertEquals("lt", extendedMap.get((char) 0x0003C));
        assertEquals("gt", extendedMap.get((char) 0x0003E));
    }

    @Test
    public void testEscape() {
        String str = "&lt; &gt; &amp; &apos; &quot; < > ' & \"";
        String expected = "<>&amp;'&quot; < > ' & \"";
        String result = Entities.escape(str, null, Entities.EscapeMode.base);
        assertEquals(expected, result);
    }

    @Test
    public void testUnescape() {
        String input = "&lt;>&amp;'&quot; < > ' & \"";
        String expected = "<>& amp;'\" < > ' & \"";
        String result = Entities.unescape(input);
        assertEquals(expected, result);
    }

    @Test
    public void testUnescapeStrict() {
        String input = "&lt;>&amp;'&quot; < > ' & \"";
        String expected = "<>&amp;";
        String result = Entities.unescape(input, true);
        assertEquals(expected, result);
    }
}