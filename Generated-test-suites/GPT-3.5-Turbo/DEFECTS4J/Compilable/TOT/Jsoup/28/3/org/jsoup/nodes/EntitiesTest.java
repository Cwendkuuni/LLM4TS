package org.jsoup.nodes;
import org.junit.Test;
import org.jsoup.nodes.Entities;

import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("greater"));
        assertFalse(Entities.isNamedEntity("at"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("greater"));
        assertNull(Entities.getCharacterByName("at"));
    }

    @Test
    public void testEscape() {
        String input = "<div>&</div>";
        String expectedOutput = "&lt;div&gt;&amp;&lt;/div&gt;";
        assertEquals(expectedOutput, Entities.escape(input, null, Entities.EscapeMode.base));
        assertEquals(expectedOutput, Entities.escape(input, null, Entities.EscapeMode.xhtml));
        assertEquals(expectedOutput, Entities.escape(input, null, Entities.EscapeMode.extended));
    }

    @Test
    public void testUnescape() {
        String input = "&lt;div&gt;&amp;&lt;/div&gt;";
        String expectedOutput = "<div>&</div>";
        assertEquals(expectedOutput, Entities.unescape(input));
    }
}