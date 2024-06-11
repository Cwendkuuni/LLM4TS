package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.Properties;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("foo"));
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("foo"));
        assertFalse(Entities.isBaseNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("foo"));
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscape() {
        assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml)));
        assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml)));
        assertEquals("&gt;", Entities.escape(">", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml)));
        assertEquals("&quot;", Entities.escape("\"", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml)));
        assertEquals("&#xA0;", Entities.escape("\u00A0", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml)));
        assertEquals("\u00A0", Entities.escape("\u00A0", new Document.OutputSettings().escapeMode(Entities.EscapeMode.base)));
        assertEquals("&#x26;", Entities.escape("&", new Document.OutputSettings().escapeMode(Entities.EscapeMode.extended)));
    }

    @Test
    public void testUnescape() {
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("\"", Entities.unescape("&quot;"));
    }

    @Test
    public void testUnescapeStrict() {
        assertEquals("&amp;", Entities.unescape("&amp"));
        assertEquals("&lt;", Entities.unescape("&lt"));
        assertEquals("&gt;", Entities.unescape("&gt"));
        assertEquals("&quot;", Entities.unescape("&quot"));
    }

    @Test
    public void testUnescapeNull() {
        assertEquals("", Entities.unescape(null));
    }

    @Test
    public void testEscapeAndUnescape() {
        String original = "<>&\"hello\u00A0world";
        String escaped = Entities.escape(original, new Document.OutputSettings().escapeMode(Entities.EscapeMode.extended));
        String unescaped = Entities.unescape(escaped);
        assertEquals(original, unescaped);
    }

    @Test
    public void testEscapeAndUnescapeStrict() {
        String original = "<>&\"hello\u00A0world";
        String escaped = Entities.escape(original, new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        String unescaped = Entities.unescape(escaped, true);
        assertEquals(original, unescaped);
    }

    @Test
    public void testCoreCharsetByName() {
        assertEquals(Entities.CoreCharset.ascii, Entities.CoreCharset.byName("US-ASCII"));
        assertEquals(Entities.CoreCharset.utf, Entities.CoreCharset.byName("UTF-8"));
        assertEquals(Entities.CoreCharset.fallback, Entities.CoreCharset.byName("ISO-8859-1"));
        assertEquals(Entities.CoreCharset.fallback, Entities.CoreCharset.byName("ISO-LATIN-1"));
    }

    @Test
    public void testXhtmlEscapeModeMap() {
        assertEquals("&quot;", Entities.EscapeMode.xhtml.getMap().get(Character.valueOf('\"')));
        assertEquals("&amp;", Entities.EscapeMode.xhtml.getMap().get(Character.valueOf('&')));
        assertEquals("&lt;", Entities.EscapeMode.xhtml.getMap().get(Character.valueOf('<')));
        assertEquals("&gt;", Entities.EscapeMode.xhtml.getMap().get(Character.valueOf('>')));
    }

    @Test
    public void testBaseEscapeModeMap() {
        assertEquals("&quot;", Entities.EscapeMode.base.getMap().get(Character.valueOf('\"')));
        assertEquals("&amp;", Entities.EscapeMode.base.getMap().get(Character.valueOf('&')));
        assertEquals("&lt;", Entities.EscapeMode.base.getMap().get(Character.valueOf('<')));
        assertEquals("&gt;", Entities.EscapeMode.base.getMap().get(Character.valueOf('>')));
    }

    @Test
    public void testExtendedEscapeModeMap() {
        assertEquals("&quot;", Entities.EscapeMode.extended.getMap().get(Character.valueOf('\"')));
        assertEquals("&amp;", Entities.EscapeMode.extended.getMap().get(Character.valueOf('&')));
        assertEquals("&lt;", Entities.EscapeMode.extended.getMap().get(Character.valueOf('<')));
        assertEquals("&gt;", Entities.EscapeMode.extended.getMap().get(Character.valueOf('>')));
    }

    @Test
    public void testLoadEntities() {
        Properties properties = Entities.loadEntities("entities-base.properties");
        assertNotNull(properties);
        assertEquals(252, properties.size());
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = new HashMap<>();
        inMap.put("amp", Character.valueOf('&'));
        inMap.put("lt", Character.valueOf('<'));
        inMap.put("gt", Character.valueOf('>'));
        inMap.put("quot", Character.valueOf('"'));

        Map<Character, String> outMap = Entities.toCharacterKey(inMap);
        assertEquals("amp", outMap.get(Character.valueOf('&')));
        assertEquals("lt", outMap.get(Character.valueOf('<')));
        assertEquals("gt", outMap.get(Character.valueOf('>')));
        assertEquals("quot", outMap.get(Character.valueOf('"')));
    }
}