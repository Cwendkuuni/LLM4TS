package org.jsoup.nodes.test;

import org.jsoup.nodes.*;
import org.junit.*;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("gt"));
    }

    @Test
    public void testIsNamedEntity_InvalidEntity() {
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testIsNamedEntity_EmptyString() {
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test(expected = NullPointerException.class)
    public void testIsNamedEntity_NullInput() {
        String name = null;
        Entities.isNamedEntity(name);
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("gt"));
    }

    @Test
    public void testIsBaseNamedEntity_InvalidEntity() {
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }

    @Test
    public void testIsBaseNamedEntity_EmptyString() {
        assertFalse(Entities.isBaseNamedEntity(""));
    }

    @Test(expected = NullPointerException.class)
    public void testIsBaseNamedEntity_NullInput() {
        String name = null;
        Entities.isBaseNamedEntity(name);
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('>', Entities.getCharacterByName("gt"));
        assertEquals('"', Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetCharacterByName_NullInput() {
        String name = null;
        Entities.getCharacterByName(name);
    }

    @Test
    public void testEscape() {
        String input = "& <> \"";
        String expectedOutput = "&amp; &lt; &gt; &quot;";
        assertEquals(expectedOutput, Entities.escape(input, Document.OutputSettings.base()));
    }

    @Test
    public void testUnescape() {
        String input = "&amp; &lt; &gt; &quot;";
        String expectedOutput = "& <> \"";
        assertEquals(expectedOutput, Entities.unescape(input));
    }

    @Test
    public void testUnescape_Strict() {
        String input = "&amp; &lt; &gt; &quot;";
        String expectedOutput = "&amp; &lt; &gt; &quot;";
        assertEquals(expectedOutput, Entities.unescape(input, true));
    }
}