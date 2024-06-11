
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("notanentity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("notanentity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(null, Entities.getCharacterByName("notanentity"));
    }

    @Test
    public void testEscapeXhtml() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);

        assertEquals("&amp;", Entities.escape("&", out));
        assertEquals("&nbsp;", Entities.escape("\u00A0", out));
        assertEquals("&lt;", Entities.escape("<", out));
        assertEquals("&gt;", Entities.escape(">", out));
        assertEquals("\"", Entities.escape("\"", out));
        assertEquals("&#x4E2D;", Entities.escape("中", out));
    }

    @Test
    public void testEscapeBase() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.base);

        assertEquals("&amp;", Entities.escape("&", out));
        assertEquals(" ", Entities.escape("\u00A0", out));
        assertEquals("&lt;", Entities.escape("<", out));
        assertEquals("&gt;", Entities.escape(">", out));
        assertEquals("\"", Entities.escape("\"", out));
        assertEquals("中", Entities.escape("中", out));
    }

    @Test
    public void testEscapeExtended() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.extended);

        assertEquals("&amp;", Entities.escape("&", out));
        assertEquals("&nbsp;", Entities.escape("\u00A0", out));
        assertEquals("&lt;", Entities.escape("<", out));
        assertEquals("&gt;", Entities.escape(">", out));
        assertEquals("\"", Entities.escape("\"", out));
        assertEquals("&#x4E2D;", Entities.escape("中", out));
    }

    @Test
    public void testUnescape() {
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("中", Entities.unescape("&#x4E2D;"));
    }

    @Test
    public void testUnescapeStrict() {
        assertEquals("&", Entities.unescape("&amp;", true));
        assertEquals("<", Entities.unescape("&lt;", true));
        assertEquals(">", Entities.unescape("&gt;", true));
        assertEquals("\"", Entities.unescape("&quot;", true));
        assertEquals("&#x4E2D;", Entities.unescape("&#x4E2D;", true));
    }

    @Test
    public void testCanEncode() {
        CoreCharset asciiCharset = CoreCharset.byName("US-ASCII");
        CoreCharset utfCharset = CoreCharset.byName("UTF-8");
        CoreCharset fallbackCharset = CoreCharset.byName("ISO-8859-1");

        assertTrue(Entities.canEncode(asciiCharset, 'a', StringUtil.getDefaultCharsetEncoder()));
        assertFalse(Entities.canEncode(asciiCharset, '\u4E2D', StringUtil.getDefaultCharsetEncoder()));

        assertTrue(Entities.canEncode(utfCharset, 'a', StringUtil.getDefaultCharsetEncoder()));
        assertTrue(Entities.canEncode(utfCharset, '\u4E2D', StringUtil.getDefaultCharsetEncoder()));

        assertTrue(Entities.canEncode(fallbackCharset, 'a', StringUtil.getDefaultCharsetEncoder()));
        assertFalse(Entities.canEncode(fallbackCharset, '\u4E2D', StringUtil.getDefaultCharsetEncoder()));
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assertEquals((char) 0x00A0, entities.get("nbsp").charValue());
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = new HashMap<>();
        inMap.put("entity1", Character.valueOf('a'));
        inMap.put("entity2", Character.valueOf('b'));
        inMap.put("entity3", Character.valueOf('A'));
        inMap.put("entity4", Character.valueOf('B'));
        inMap.put("entity5", Character.valueOf('A'));

        Map<Character, String> outMap = Entities.toCharacterKey(inMap);

        assertEquals("entity3", outMap.get(Character.valueOf('A')));
        assertEquals("entity4", outMap.get(Character.valueOf('B')));
        assertEquals("entity1", outMap.get(Character.valueOf('a')));
        assertEquals("entity2", outMap.get(Character.valueOf('b')));
    }
}
