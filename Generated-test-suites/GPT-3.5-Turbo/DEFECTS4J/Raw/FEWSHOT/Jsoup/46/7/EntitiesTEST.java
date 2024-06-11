package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("cent"));
        assertFalse(Entities.isNamedEntity("abc"));
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("xyz"));
        assertFalse(Entities.isBaseNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf((char) 60), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("xyz"));
        assertNull(Entities.getCharacterByName(""));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);
        assertEquals("&amp;", Entities.escape("&", out));
        assertEquals("&nbsp;", Entities.escape("\u00A0", out));
        assertEquals("&lt;", Entities.escape("<", out));
        assertEquals("&gt;", Entities.escape(">", out));
        assertEquals("&quot;", Entities.escape("\"", out));
        assertEquals("&#x20AC;", Entities.escape("\u20AC", out));
    }

    @Test
    public void testEscape_inAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);
        out.inAttribute(true);
        assertEquals("&", Entities.escape("&", out));
        assertEquals("\u00A0", Entities.escape("\u00A0", out));
        assertEquals("&lt;", Entities.escape("<", out));
        assertEquals("&gt;", Entities.escape(">", out));
        assertEquals("&quot;", Entities.escape("\"", out));
        assertEquals("\u20AC", Entities.escape("\u20AC", out));
    }

    @Test
    public void testUnescape_strict() {
        assertEquals("LT;", Entities.unescape("LT;", true));
        assertEquals("XYZ;", Entities.unescape("XYZ;", true));
        assertEquals("", Entities.unescape("", true));
    }

    @Test
    public void testUnescape_nonStrict() {
        assertEquals("LT;", Entities.unescape("LT;", false));
        assertEquals("XYZ;", Entities.unescape("XYZ;", false));
        assertEquals("", Entities.unescape("", false));
    }

}