package org.jsoup.nodes;

import org.jsoup.nodes.Entities;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("abc"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("abc"));
    }

    @Test
    public void testEscape() {
        String input = "<span>test</span>";
        String expected = "&lt;span&gt;test&lt;/span&gt;";
        assertEquals(expected, Entities.escape(input, null));
    }

    @Test
    public void testUnescape() {
        String input = "&lt;span&gt;test&lt;/span&gt;";
        String expected = "<span>test</span>";
        assertEquals(expected, Entities.unescape(input));
    }

    @Test
    public void testUnescapeStrict() {
        String input = "&lt;span&gt;test&lt;/span&gt;";
        String expected = "<span>test</span>";
        assertEquals(expected, Entities.unescape(input, true));
    }
}
