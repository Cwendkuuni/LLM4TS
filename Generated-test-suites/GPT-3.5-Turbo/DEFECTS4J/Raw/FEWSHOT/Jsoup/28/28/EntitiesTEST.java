package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;

import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt").charValue());
        assertEquals('>', Entities.getCharacterByName("gt").charValue());
        assertEquals('&', Entities.getCharacterByName("amp").charValue());
        assertEquals('\'', Entities.getCharacterByName("apos").charValue());
        assertEquals('"', Entities.getCharacterByName("quot").charValue());
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(EscapeMode.xhtml);
        CharsetEncoder encoder = CharsetEncoder.availableCharsets().get("UTF-8");

        assertEquals("&lt;", Entities.escape("<", outputSettings));
        assertEquals("&gt;", Entities.escape(">", outputSettings));
        assertEquals("&amp;", Entities.escape("&", outputSettings));
        assertEquals("&apos;", Entities.escape("'", outputSettings));
        assertEquals("&quot;", Entities.escape("\"", outputSettings));
        assertEquals("&#xa9;", Entities.escape("©", outputSettings));
        assertEquals("\u0000", Entities.escape("\u0000", outputSettings));

        assertEquals("&lt;", Entities.escape("<", encoder, EscapeMode.xhtml));
        assertEquals("&gt;", Entities.escape(">", encoder, EscapeMode.xhtml));
        assertEquals("&amp;", Entities.escape("&", encoder, EscapeMode.xhtml));
        assertEquals("&apos;", Entities.escape("'", encoder, EscapeMode.xhtml));
        assertEquals("&quot;", Entities.escape("\"", encoder, EscapeMode.xhtml));
        assertEquals("&#xa9;", Entities.escape("©", encoder, EscapeMode.xhtml));
        assertEquals("\u0000", Entities.escape("\u0000", encoder, EscapeMode.xhtml));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("'", Entities.unescape("&apos;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("©", Entities.unescape("&#xa9;"));
        assertEquals("A &amp; B", Entities.unescape("A &amp; B"));
        assertEquals("A&B", Entities.unescape("A&B"));
        assertEquals("<Emoji=😁>", Entities.unescape("&lt;Emoji=😁>"));
        assertEquals("\u0000", Entities.unescape("\u0000"));
        assertEquals("&unknown;", Entities.unescape("&unknown;"));
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inputMap = new HashMap<>();
        inputMap.put("lt", '<');
        inputMap.put("gt", '>');
        inputMap.put("amp", '&');
        inputMap.put("lt1", '<');
        inputMap.put("gt1", '>');
        inputMap.put("amp1", '&');

        Map<Character, String> expectedOutputMap = new HashMap<>();
        expectedOutputMap.put('<', "lt1");
        expectedOutputMap.put('>', "gt1");
        expectedOutputMap.put('&', "amp1");
        
        Map<Character, String> actualOutput = Entities.toCharacterKey(inputMap);
        
        assertNotNull(actualOutput);
        assertNotSame(inputMap, actualOutput);
        assertEquals(3, actualOutput.size());
        assertEquals(expectedOutputMap, actualOutput);
    }
}