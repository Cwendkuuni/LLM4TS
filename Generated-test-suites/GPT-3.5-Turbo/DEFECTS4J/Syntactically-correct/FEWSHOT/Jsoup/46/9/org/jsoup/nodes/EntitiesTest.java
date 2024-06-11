package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.parser.Parser;
import java.util.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("nbsp"));
        assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("foo"));
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
        Document.OutputSettings outSettings = new Document.OutputSettings();
        outSettings.escapeMode(Document.EscapeMode.base);

        assertEquals("foo", Entities.escape("foo", outSettings));
        assertEquals("&lt;foo&gt;", Entities.escape("<foo>", outSettings));
        assertEquals("&amp;&lt;&gt;&nbsp;", Entities.escape("&<> ", outSettings));
        assertEquals("&#x12345;", Entities.escape("\uD808\uDF45", outSettings));
    }

    @Test
    public void testUnescape() {
        assertEquals("foo", Entities.unescape("foo"));
        assertEquals("<foo>", Entities.unescape("&lt;foo&gt;"));
        assertEquals("&<> ", Entities.unescape("&amp;&lt;&gt;&nbsp;"));
        assertEquals("\uD808\uDF45", Entities.unescape("&#x12345;"));
    }
}