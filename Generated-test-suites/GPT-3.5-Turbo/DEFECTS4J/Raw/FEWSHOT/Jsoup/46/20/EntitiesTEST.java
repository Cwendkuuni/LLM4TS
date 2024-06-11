package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharsetEncoder;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class EntitiesTest {

    private static Map<String, Character> full;
    private static Map<Character, String> xhtmlByVal;
    private static Map<String, Character> base;
    private static Map<Character, String> baseByVal;
    private static Map<Character, String> fullByVal;

    @BeforeClass
    public static void loadEntities() {
        full = Entities.loadEntities("entities-full.properties");
        xhtmlByVal = Entities.xhtmlByVal;
        base = Entities.loadEntities("entities-base.properties");
        baseByVal = Entities.baseByVal;
        fullByVal = Entities.fullByVal;
    }

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("foo"));
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("foo"));
        assertFalse(Entities.isBaseNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("foo"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);

        assertEquals("&quot;", Entities.escape("\"", out));
        assertEquals("&amp;", Entities.escape("&", out));
        assertEquals("&lt;", Entities.escape("<", out));
        assertEquals("&gt;", Entities.escape(">", out));
    }

    @Test
    public void testEscapeInAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);

        assertEquals("\"", Entities.escape("\"", out));
        assertEquals("&amp;", Entities.escape("&", out));
        assertEquals("&lt;", Entities.escape("<", out));
        assertEquals("&gt;", Entities.escape(">", out));
    }

    @Test
    public void testEscapeNormaliseWhite() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);

        assertEquals("foo bar", Entities.escape("foo bar", out));
        assertEquals(" foo bar ", Entities.escape(" foo bar ", out));
    }

    @Test
    public void testEscapeStripLeadingWhite() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);

        assertEquals("foo", Entities.escape(" foo", out));
        assertEquals("foo", Entities.escape("  foo", out));
        assertEquals("foo bar", Entities.escape("  foo bar", out));
    }

    @Test
    public void testUnescape() {
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
    }

    @Test
    public void testUnescapeStrict() {
        // No changes needed for strict unescaping
        assertEquals("\"", Entities.unescape("&quot;", true));
        assertEquals("&", Entities.unescape("&amp;", true));
        assertEquals("<", Entities.unescape("&lt;", true));
        assertEquals(">", Entities.unescape("&gt;", true));
    }

    @Test
    public void testUnescapeNonStrict() {
        assertEquals("foo\"", Entities.unescape("foo&quot;", false));
        assertEquals("foo&", Entities.unescape("foo&amp;", false));
        assertEquals("foo<", Entities.unescape("foo&lt;", false));
        assertEquals("foo>", Entities.unescape("foo&gt;", false));
    }

    @Test
    public void testUnescapeInAttribute() {
        assertEquals("\"", Entities.unescape("\""));
        assertEquals("&", Entities.unescape("&"));
        assertEquals("<", Entities.unescape("<"));
        assertEquals(">", Entities.unescape(">"));
    }

    @Test
    public void testUnescapeInAttributeNonStrict() {
        assertEquals("foo\"", Entities.unescape("foo\""));
        assertEquals("foo&", Entities.unescape("foo&"));
        assertEquals("foo<", Entities.unescape("foo<"));
        assertEquals("foo>", Entities.unescape("foo>"));
    }

    @Test
    public void testCanEncodeCharsetAscii() {
        CharsetEncoder encoder = null;
        try {
            encoder = entities.createEncoder("US-ASCII");
        } catch (IOException e) {
            fail();
        }

        assertTrue(entities.canEncode(entities.CoreCharset.ascii, '<', encoder));
        assertTrue(entities.canEncode(entities.CoreCharset.ascii, '>', encoder));
        assertTrue(entities.canEncode(entities.CoreCharset.ascii, '&', encoder));
        assertTrue(entities.canEncode(entities.CoreCharset.ascii, 'A', encoder));

        assertFalse(entities.canEncode(entities.CoreCharset.ascii, '\u20AC', encoder));  // €
        assertFalse(entities.canEncode(entities.CoreCharset.ascii, '中', encoder)); // 中
    }

    @Test
    public void testCanEncodeCharsetUtf() {
        CharsetEncoder encoder = null;
        try {
            encoder = entities.createEncoder("UTF-8");
        } catch (IOException e) {
            fail();
        }

        assertTrue(entities.canEncode(entities.CoreCharset.utf, '<', encoder));
        assertTrue(entities.canEncode(entities.CoreCharset.utf, '>', encoder));
        assertTrue(entities.canEncode(entities.CoreCharset.utf, '&', encoder));
        assertTrue(entities.canEncode(entities.CoreCharset.utf, 'A', encoder));
        assertTrue(entities.canEncode(entities.CoreCharset.utf, '\u20AC', encoder));  // €
        assertTrue(entities.canEncode(entities.CoreCharset.utf, '中', encoder)); // 中
    }

    @Test
    public void testCanEncodeCharsetFallback() {
        CharsetEncoder encoder = null;
        try {
            encoder = entities.createEncoder("ISO-8859-1"); // Not covered by CoreCharset
        } catch (IOException e) {
            fail();
        }

        assertTrue(entities.canEncode(entities.CoreCharset.fallback, '<', encoder));
        assertTrue(entities.canEncode(entities.CoreCharset.fallback, '>', encoder));
        assertTrue(entities.canEncode(entities.CoreCharset.fallback, '&', encoder));
        assertTrue(entities.canEncode(entities.CoreCharset.fallback, 'A', encoder));
        assertTrue(entities.canEncode(entities.CoreCharset.fallback, '\u20AC', encoder));  // €
        assertTrue(entities.canEncode(entities.CoreCharset.fallback, '中', encoder)); // 中
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-full.properties");
        assertNotNull(entities);
        assertFalse(entities.isEmpty());
    }

    @Test
    public void testToCharacterKey() {
        Properties properties = new Properties();
        properties.setProperty("lt", "&#60;");
        properties.setProperty("gt", "&#62;");

        Map<String, Character> inMap = new HashMap<>();
        inMap.put("lt", '<');
        inMap.put("gt", '>');

        Map<Character, String> outMap = Entities.toCharacterKey(inMap);

        assertEquals("lt", outMap.get('<'));
        assertEquals("gt", outMap.get('>'));
    }
}