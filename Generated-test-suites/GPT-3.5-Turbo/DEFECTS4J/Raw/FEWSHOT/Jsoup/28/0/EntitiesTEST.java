package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.junit.Test;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("foo"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;", Entities.escape("<", Document.OutputSettings.xhtml()));
        assertEquals("&gt;", Entities.escape(">", Document.OutputSettings.xhtml()));
        assertEquals("&amp;", Entities.escape("&", Document.OutputSettings.xhtml()));
        assertEquals("&apos;", Entities.escape("'", Document.OutputSettings.xhtml()));
        assertEquals("&quot;", Entities.escape("\"", Document.OutputSettings.xhtml()));

        assertEquals("&lt;", Entities.escape("<", Document.OutputSettings.base()));
        assertEquals("&gt;", Entities.escape(">", Document.OutputSettings.base()));
        assertEquals("&amp;", Entities.escape("&", Document.OutputSettings.base()));
        assertEquals("&apos;", Entities.escape("'", Document.OutputSettings.base()));
        assertEquals("&quot;", Entities.escape("\"", Document.OutputSettings.base()));

        assertEquals("&lt;", Entities.escape("<", Document.OutputSettings.extended()));
        assertEquals("&gt;", Entities.escape(">", Document.OutputSettings.extended()));
        assertEquals("&amp;", Entities.escape("&", Document.OutputSettings.extended()));
        assertEquals("&apos;", Entities.escape("'", Document.OutputSettings.extended()));
        assertEquals("&quot;", Entities.escape("\"", Document.OutputSettings.extended()));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("'", Entities.unescape("&apos;"));
        assertEquals("\"", Entities.unescape("&quot;"));
    }

    @Test
    public void testUnescapeStrict() {
        assertEquals("<", Entities.unescape("&lt;", true));
        assertEquals(">", Entities.unescape("&gt;", true));
        assertEquals("&", Entities.unescape("&amp;", true));
        assertEquals("'", Entities.unescape("&apos;", true));
        assertEquals("\"", Entities.unescape("&quot;", true));
        assertEquals("&lt;", Entities.unescape("&lt;", true));
        assertEquals("&gt;", Entities.unescape("&gt;", true));
        assertEquals("&amp;", Entities.unescape("&amp;", true));
        assertEquals("&apos;", Entities.unescape("&apos;", true));
        assertEquals("&quot;", Entities.unescape("&quot;", true));
    }
}