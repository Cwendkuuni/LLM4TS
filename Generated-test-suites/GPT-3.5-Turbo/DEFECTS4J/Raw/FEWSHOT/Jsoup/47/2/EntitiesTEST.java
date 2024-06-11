package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("fakeEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("fakeEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertEquals(null, Entities.getCharacterByName("fakeEntity"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);

        assertEquals("&lt;", Entities.escape("<", out));
        assertEquals("&gt;", Entities.escape(">", out));
        assertEquals("&amp;", Entities.escape("&", out));
        assertEquals("&nbsp;", Entities.escape("\u00a0", out));
        assertEquals("&quot;", Entities.escape("\"", out));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\u00a0", Entities.unescape("&nbsp;"));
        assertEquals("\"", Entities.unescape("&quot;"));
    }

    @Test
    public void testEscapeStripLeadingWhite() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);

        StringBuilder accum = new StringBuilder();

        Entities.escape(accum, "  test", out, false, true, true);
        assertEquals("test", accum.toString());

        Entities.escape(accum, "  test", out, true, true, true);
        assertEquals(" test", accum.toString());

        Entities.escape(accum, "  test", out, false, false, true);
        assertEquals("  test", accum.toString());
    }

    @Test
    public void testEscapeNormaliseWhite() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);

        StringBuilder accum = new StringBuilder();

        Entities.escape(accum, "  te st  ", out, false, true, false);
        assertEquals(" te st ", accum.toString());

        Entities.escape(accum, "  te      st  ", out, false, true, false);
        assertEquals(" te st ", accum.toString());

        Entities.escape(accum, "te st", out, false, true, false);
        assertEquals("te st", accum.toString());
    }

    @Test
    public void testEscapeInAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);

        StringBuilder accum = new StringBuilder();

        Entities.escape(accum, "<test>", out, true, true, false);
        assertEquals("<test>", accum.toString());

        out.escapeMode(Entities.EscapeMode.base);

        accum = new StringBuilder();
        Entities.escape(accum, "<test>", out, true, true, false);
        assertEquals("&lt;test&gt;", accum.toString());
    }

    @Test
    public void testCanEncode() {
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, 'a', null));
        assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, '\u00a0', null));
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'a', null));
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");

        assertEquals(Character.valueOf('<'), entities.get("lt"));
        assertEquals(Character.valueOf('>'), entities.get("gt"));
        assertEquals(Character.valueOf('&'), entities.get("amp"));
        assertEquals(Character.valueOf('"'), entities.get("quot"));
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = new HashMap<>();
        inMap.put("lt", '<');
        inMap.put("gt", '>');

        Map<Character, String> outMap = Entities.toCharacterKey(inMap);

        assertEquals("lt", outMap.get('<'));
        assertEquals("gt", outMap.get('>'));
    }
}