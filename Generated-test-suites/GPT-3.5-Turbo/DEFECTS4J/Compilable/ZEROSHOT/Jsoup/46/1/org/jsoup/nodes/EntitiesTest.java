package org.jsoup.nodes;
import org.junit.*;
import static org.junit.Assert.*;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        String string = "<div>&amp;</div>";
        String expected = "&lt;div&gt;&amp;amp;&lt;/div&gt;";
        assertEquals(expected, Entities.escape(string, null));
    }

    @Test
    public void testUnescape() {
        String string = "&lt;div&gt;&amp;amp;&lt;/div&gt;";
        String expected = "<div>&amp;</div>";
        assertEquals(expected, Entities.unescape(string));
    }

}