package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("ltt")); // not a valid named entity
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("ltt")); // not a valid base named entity
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("ltt")); // not a valid named entity
    }

    @Test
    public void testEscape() {
        String input = "&lt;foo&amp;bar&quot; &nbsp; &hellip; &copy; 2023";
        String expectedOutput = "&amp;lt;foo&amp;amp;amp;amp;amp;amp;amp;bar&amp;quot; &nbsp; &hellip; &copy; 2023";

        assertEquals(expectedOutput, Entities.escape(input, Document.OutputSettings.base()));
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;foo&amp;amp;amp;amp;amp;amp;amp;bar&amp;quot; &nbsp; &hellip; &copy; 2023";
        String expectedOutput = "&lt;foo&amp;amp;amp;bar&quot; &nbsp; &hellip; &copy; 2023";

        assertEquals(expectedOutput, Entities.unescape(input));
    }
}