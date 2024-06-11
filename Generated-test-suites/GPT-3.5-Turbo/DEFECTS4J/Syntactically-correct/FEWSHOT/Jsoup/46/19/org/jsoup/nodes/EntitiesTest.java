package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertFalse(Entities.isNamedEntity("foo"));
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertFalse(Entities.isBaseNamedEntity("foo"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        assertNull(Entities.getCharacterByName("foo"));
        assertEquals((Character) '<', Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName(""));
    }

    @Test
    public void testEscape() {
        assertEquals("", Entities.escape("", new Document.OutputSettings()));
        assertEquals("This is an &amp; example", Entities.escape("This is an & example", new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        assertEquals("", Entities.unescape(""));
        assertEquals("This is an & example", Entities.unescape("This is an &amp; example"));
    }
}