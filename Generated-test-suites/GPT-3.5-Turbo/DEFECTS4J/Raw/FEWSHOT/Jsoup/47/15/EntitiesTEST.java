package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertFalse(Entities.isNamedEntity("foo"));
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertFalse(Entities.isBaseNamedEntity("foo"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
    }

    @Test
    public void testGetCharacterByName() {
        assertNull(Entities.getCharacterByName("foo"));
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);

        assertEquals("&lt;", Entities.escape("<", outputSettings));
        assertEquals("&quot;", Entities.escape("\"", outputSettings));
        assertEquals("&#xa0;", Entities.escape("\u00a0", outputSettings));
    }

    @Test
    public void testEscape_stripLeadingWhite() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);

        assertEquals("Hello World", Entities.escape("\t   \nHello World", outputSettings));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("\u00a0", Entities.unescape("&#xa0;"));
    }

    @Test
    public void testUnescape_strict() {
        assertEquals("<", Entities.unescape("&lt;", true));
        assertEquals("\"", Entities.unescape("&quot;", true));
        assertEquals("\u00a0", Entities.unescape("&#xa0;", true));
    }

    @Test
    public void testUnescape_notStrict() {
        assertEquals("<", Entities.unescape("&lt;", false));
        assertEquals("\"", Entities.unescape("&quot;", false));
        assertEquals("\u00a0", Entities.unescape("&#xa0;", false));
    }
    
    @Test
    public void testUnescape_noSemicolon() {
        assertEquals("&lt", Entities.unescape("&lt"));
        assertEquals("&quot", Entities.unescape("&quot"));
        assertEquals("&#xa0", Entities.unescape("&#xa0"));
    }
    
    @Test
    public void testUnescape_invalidEntity() {
        assertEquals("foo&bar;", Entities.unescape("foo&bar;"));
        assertEquals("foo&ampbar;", Entities.unescape("foo&ampbar;"));
    }
    
    @Test
    public void testCoreCharset_byName() {
        assertEquals(Entities.CoreCharset.ascii, Entities.CoreCharset.byName("US-ASCII"));
        assertEquals(Entities.CoreCharset.utf, Entities.CoreCharset.byName("UTF-8"));
        assertEquals(Entities.CoreCharset.fallback, Entities.CoreCharset.byName("ISO-8859-1"));
    }
    
    @Test
    public void testEscapeMode_getMap() {
        Map<Character, String> xhtmlMap = Entities.EscapeMode.xhtml.getMap();
        Map<Character, String> baseMap = Entities.EscapeMode.base.getMap();
        Map<Character, String> extendedMap = Entities.EscapeMode.extended.getMap();

        assertEquals(4, xhtmlMap.size());
        assertEquals(2128, baseMap.size());
        assertEquals(2520, extendedMap.size());

        assertEquals("&lt;", xhtmlMap.get('<'));
        assertEquals("lt", baseMap.get('<'));
        assertEquals("lt", extendedMap.get('<'));
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> baseEntities = Entities.loadEntities("entities-base.properties");
        Map<String, Character> fullEntities = Entities.loadEntities("entities-full.properties");
        
        assertEquals(2128, baseEntities.size());
        assertEquals(2520, fullEntities.size());

        assertEquals(Character.valueOf('\u015d'), baseEntities.get("scedil"));
        assertEquals(Character.valueOf('\u015e'), fullEntities.get("Scedil"));
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = Entities.loadEntities("entities-full.properties");
        Map<Character, String> outMap = Entities.toCharacterKey(inMap);

        assertEquals(2520, outMap.size());
        assertTrue(outMap.containsKey('\u015e'));
        assertFalse(outMap.containsKey('ö'));
        assertEquals("Scedil", outMap.get('\u015e'));
        assertNull(outMap.get('ö'));
    }
}