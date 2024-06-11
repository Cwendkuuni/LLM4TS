package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Entities;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("zzz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("zzz"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('>', Entities.getCharacterByName("gt"));
        assertEquals('"', Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("zzz"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml);
        assertEquals("&lt;foo&gt;", Entities.escape("<foo>", out));
        assertEquals("&amp;&quot;bar&quot;", Entities.escape("&\"bar", out));
        assertEquals("&nbsp;", Entities.escape("\u00A0", out));
        assertEquals("&QuOTe;", Entities.escape("QuOTe", out));
        Document.OutputSettings out2 = new Document.OutputSettings().escapeMode(Entities.EscapeMode.extended);
        assertEquals("&#x304;a", Entities.escape("ä", out2));
        assertEquals("&#x20AC;", Entities.escape("€", out2));
        assertEquals("&#x1F603;", Entities.escape("💩", out2));
    }

    @Test
    public void testUnescape() {
        String str = "&lt;foo&gt;&#x20AC;&amp;&#x304;bar&quot;";
        assertEquals("<foo>\u00A0&amp;äbar\"", Entities.unescape(str));
        assertEquals("&lt;foo&gt;&#xA0;&amp;&#228;bar&quot;", Entities.unescape(str, true)); // strict
    }

    @Test
    public void testCanEncode() {
        assertTrue(Entities.canEncode(CoreCharset.ascii, 'a'));
        assertTrue(Entities.canEncode(CoreCharset.ascii, ' '));
        assertTrue(Entities.canEncode(CoreCharset.utf, 'ö'));
        assertFalse(Entities.canEncode(CoreCharset.ascii, 'ö'));
        assertFalse(Entities.canEncode(CoreCharset.ascii, 'ö'.toString().getBytes().length > 1 ? "ö" : "\u00F6")); // surrogate pair
    }
}