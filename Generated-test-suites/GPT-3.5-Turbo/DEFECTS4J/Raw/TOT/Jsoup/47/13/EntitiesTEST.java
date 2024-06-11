package org.jsoup.nodes;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;hello&gt;", Entities.escape("<hello>", new Document.OutputSettings()));
        assertEquals("test &amp; test", Entities.escape("test & test", new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        assertEquals("<hello>", Entities.unescape("&lt;hello&gt;"));
        assertEquals("test & test", Entities.unescape("test &amp; test"));
    }

    @Test
    public void testEscapeWithSettings() {
        Document.OutputSettings settings = new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml);

        assertEquals("&lt;hello&gt;", Entities.escape("<hello>", settings));
        assertEquals("test &amp; test", Entities.escape("test & test", settings));
        assertEquals("&#xa0;", Entities.escape("\u00a0", settings));
    }

    @Test
    public void testEscapeModeGetMap() {
        Map<Character, String> expectedMap = new HashMap<>();
        expectedMap.put('<', "lt");
        expectedMap.put('>', "gt");
        expectedMap.put('&', "amp");
        expectedMap.put('"', "quot");

        Entities.EscapeMode escapeMode = Entities.EscapeMode.xhtml;
        assertEquals(expectedMap, escapeMode.getMap());
    }

    @Test
    public void testEscapeWithAttribute() {
        Document.OutputSettings settings = new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml);

        assertEquals("<hello>", Entities.escape("<hello>", settings, true, false, false));
        assertEquals("test & test", Entities.escape("test & test", settings, true, false, false));
        assertEquals("<a href=&quot;http://example.com&quot;>", Entities.escape("<a href=\"http://example.com\">", settings, true, false, false));
    }

    @Test
    public void testEscapeWithAttributeAndWhitespaceNormalization() {
        Document.OutputSettings settings = new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml);

        assertEquals("<hello>", Entities.escape("<hello>", settings, true, true, false));
        assertEquals("test &amp; test", Entities.escape("test & test", settings, true, true, false));
        assertEquals("<a href=\"http://example.com\">", Entities.escape("<a href=\"http://example.com\">", settings, true, true, false));
    }

    @Test
    public void testEscapeWithAttributeAndWhitespaceNormalizationAndLeadingWhitespaceStripping() {
        Document.OutputSettings settings = new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml);

        assertEquals("<hello>", Entities.escape("<hello>", settings, true, true, true));
        assertEquals("test & test", Entities.escape("test & test", settings, true, true, true));
        assertEquals("<a href=\"http://example.com\">", Entities.escape("<a href=\"http://example.com\">", settings, true, true, true));
    }

    @Test
    public void testUnescapeWithStrict() {
        assertEquals("<hello>", Entities.unescape("&lt;hello&gt;", true));
        assertEquals("test &amp; test", Entities.unescape("test &amp; test", true));
    }

    @Test
    public void testUnescapeWithoutStrict() {
        assertEquals("<hello>", Entities.unescape("&lt;hello&gt;", false));
        assertEquals("test & test", Entities.unescape("test &amp; test", false));
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assertEquals(entities.get("lt"), Character.valueOf('<'));
        assertEquals(entities.get("gt"), Character.valueOf('>'));
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = new HashMap<>();
        inMap.put("lt", '<');
        inMap.put("LT", '<');
        inMap.put("gt", '>');
        inMap.put("GT", '>');

        Map<Character, String> outMap = Entities.toCharacterKey(inMap);

        assertEquals(2, outMap.size());
        assertEquals("lt", outMap.get('<'));
        assertEquals("gt", outMap.get('>'));
    }
}