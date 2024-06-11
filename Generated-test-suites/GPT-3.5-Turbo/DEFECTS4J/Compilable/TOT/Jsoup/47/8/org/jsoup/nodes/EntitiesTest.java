package org.jsoup.nodes;
import org.junit.Test;
import org.jsoup.nodes.Entities;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Typical use cases
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));

        // Edge case: empty string
        assertFalse(Entities.isNamedEntity(""));

        // Error scenario: entity does not exist
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Typical use cases
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));

        // Edge case: empty string
        assertFalse(Entities.isBaseNamedEntity(""));

        // Error scenario: entity does not exist
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        // Typical use cases
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));

        // Edge case: empty string
        assertNull(Entities.getCharacterByName(""));

        // Error scenario: entity does not exist
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        // Typical use cases
        assertEquals("This is &amp; test", Entities.escape("This is & test", null));
        assertEquals("This is &lt; test", Entities.escape("This is < test", null));
        assertEquals("This is &gt; test", Entities.escape("This is > test", null));
        assertEquals("This is &quot; test", Entities.escape("This is \" test", null));

        // Edge case: empty string
        assertEquals("", Entities.escape("", null));

        // Error scenario: null input
        assertNull(Entities.escape(null, null));
    }

    @Test
    public void testUnescape() {
        // Typical use cases
        assertEquals("This is & test", Entities.unescape("This is &amp; test"));
        assertEquals("This is < test", Entities.unescape("This is &lt; test"));
        assertEquals("This is > test", Entities.unescape("This is &gt; test"));
        assertEquals("This is \" test", Entities.unescape("This is &quot; test"));

        // Edge case: empty string
        assertEquals("", Entities.unescape(""));

        // Error scenario: null input
        assertNull(Entities.unescape(null));
    }

}