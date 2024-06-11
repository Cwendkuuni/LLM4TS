package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharsetEncoder;
import java.util.*;

import org.junit.Test; 

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("copy"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("foo"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);
        assertEquals("&lt;", Entities.escape("<", out));
        assertEquals("&gt;", Entities.escape(">", out));
        assertEquals("&amp;", Entities.escape("&", out));
        assertEquals("&quot;", Entities.escape("\"", out));
        assertEquals("&#xa0;", Entities.escape("\u00a0", out));
        assertEquals("a &lt; b", Entities.escape("a < b", out));
    }

    @Test
    public void testEscapeWithInAttributeTrue() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);
        assertEquals("<", Entities.escape("<", out, true, false, false));
        assertEquals("&gt;", Entities.escape(">", out, true, false, false));
        assertEquals("&amp;", Entities.escape("&", out, true, false, false));
        assertEquals("\"", Entities.escape("\"", out, true, false, false));
        assertEquals("\u00a0", Entities.escape("\u00a0", out, true, false, false));
        assertEquals("a < b", Entities.escape("a < b", out, true, false, false));
    }

    @Test
    public void testEscapeWithNormaliseWhiteTrue() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);
        assertEquals("&lt;", Entities.escape("<", out, false, true, false));
        assertEquals("&gt;", Entities.escape(">", out, false, true, false));
        assertEquals("&amp;", Entities.escape("&", out, false, true, false));
        assertEquals("&quot;", Entities.escape("\"", out, false, true, false));
        assertEquals("\u00a0", Entities.escape("\u00a0", out, false, true, false));
        assertEquals("a   b", Entities.escape("a \t \n b", out, false, true, false));
    }

    @Test
    public void testEscapeWithStripLeadingWhiteTrue() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);
        assertEquals("<", Entities.escape("<", out, false, false, true));
        assertEquals("&gt;", Entities.escape(">", out, false, false, true));
        assertEquals("&amp;", Entities.escape("&", out, false, false, true));
        assertEquals("\"", Entities.escape("\"", out, false, false, true));
        assertEquals("\u00a0", Entities.escape("\u00a0", out, false, false, true));
        assertEquals("a < b", Entities.escape(" \n  a < b", out, false, false, true));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("\u00a0", Entities.unescape("&#xa0;"));
    }

    @Test
    public void testUnescapeWithStrictTrue() {
        assertEquals("<", Entities.unescape("&lt;", true));
        assertEquals(">", Entities.unescape("&gt;", true));
        assertEquals("&amp;", Entities.unescape("&amp;", true));
        assertEquals("\"", Entities.unescape("&quot;", true));
        assertEquals("\u00a0", Entities.unescape("&#xa0;", true));
    }

    @Test
    public void testUnescapeWithStrictFalse() {
        assertEquals("<", Entities.unescape("&lt;", false));
        assertEquals(">", Entities.unescape("&gt;", false));
        assertEquals("&", Entities.unescape("&amp;", false));
        assertEquals("\"", Entities.unescape("&quot;", false));
        assertEquals("\u00a0", Entities.unescape("&#xa0;", false));
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-full.properties");
        assertEquals(Character.valueOf('\u00A0'), entities.get("nbsp"));
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = new HashMap<String, Character>();
        inMap.put("lt", '<');
        inMap.put("gt", '>');
        inMap.put("amp", '&');
        inMap.put("quot", '"');
        inMap.put("copy", '\u00A9');
        Map<Character, String> expectedMap = new HashMap<Character, String>();
        expectedMap.put('<', "lt");
        expectedMap.put('>', "gt");
        expectedMap.put('&', "amp");
        expectedMap.put('"', "quot");
        expectedMap.put('\u00A9', "copy");
        Map<Character, String> outMap = Entities.toCharacterKey(inMap);
        assertEquals(expectedMap, outMap);
    }
}