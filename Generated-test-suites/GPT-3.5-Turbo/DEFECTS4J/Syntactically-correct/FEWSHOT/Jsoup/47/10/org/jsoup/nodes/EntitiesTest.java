package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("apple"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("apple"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("apple"));
    }

    @Test
    public void testEscape() {
        assertEquals("S", Entities.escape("S", Document.OutputSettings.html()));
        assertEquals("&amp;", Entities.escape("&", Document.OutputSettings.html()));
        assertEquals("&lt;", Entities.escape("<", Document.OutputSettings.html()));
        assertEquals("&gt;", Entities.escape(">", Document.OutputSettings.html()));
        assertEquals("&quot;", Entities.escape("\"", Document.OutputSettings.html()));
        assertEquals("&nbsp;", Entities.escape("\u00A0", Document.OutputSettings.html()));
        assertEquals("&gt;&#x2E;", Entities.escape(">.", Document.OutputSettings.html()));
        assertEquals("&#x1D306;", Entities.escape("\uD834\uDF06", Document.OutputSettings.html()));
    }

    @Test
    public void testUnescape() {
        assertEquals("S", Entities.unescape("S"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("\u00A0", Entities.unescape("&nbsp;"));
        assertEquals(">.", Entities.unescape("&gt;&#x2E;"));
        assertEquals("\uD834\uDF06", Entities.unescape("&#x1D306;"));
    }
}