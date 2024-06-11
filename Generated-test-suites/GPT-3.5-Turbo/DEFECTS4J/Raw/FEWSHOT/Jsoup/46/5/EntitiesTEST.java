package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Parser;
import org.junit.Test;

import java.nio.charset.CharsetEncoder;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("fakeEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("fakeEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(null, Entities.getCharacterByName("fakeEntity"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml);
        assertEquals("&amp;", Entities.escape("&", out));
        assertEquals("&gt;", Entities.escape(">", out));
        assertEquals("&lt;", Entities.escape("<", out));
        assertEquals("&quot;", Entities.escape("\"", out));
    }

    @Test
    public void testUnescape() {
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals("\"", Entities.unescape("&quot;"));
    }

    @Test
    public void testCanEncode() {
        assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, 'ñ', CharsetEncoder.fallback()));
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, 'A', CharsetEncoder.fallback()));
    }
}