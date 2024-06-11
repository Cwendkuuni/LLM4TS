package org.jsoup.nodes;

import org.jsoup.nodes.Entities;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity_typicalUseCase() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
    }

    @Test
    public void testIsNamedEntity_edgeCase() {
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }
    
    @Test
    public void testGetCharacterByName_typicalUseCase() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
    }

    @Test
    public void testGetCharacterByName_edgeCase() {
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape_typicalUseCase() {
        String string = "<h1>Test String</h1>";
        String expected = "&lt;h1&gt;Test String&lt;/h1&gt;";
        assertEquals(expected, Entities.escape(string, null));
    }

    @Test
    public void testEscape_edgeCase() {
        String string = "";
        String expected = "";
        assertEquals(expected, Entities.escape(string, null));
    }

    @Test
    public void testUnescape_typicalUseCase() {
        String string = "&lt;h1&gt;Test String&lt;/h1&gt;";
        String expected = "<h1>Test String</h1>";
        assertEquals(expected, Entities.unescape(string));
    }

    @Test
    public void testUnescape_edgeCase() {
        String string = "";
        String expected = "";
        assertEquals(expected, Entities.unescape(string));
    }

    @Test
    public void testUnescape_strict_typicalUseCase() {
        String string = "&lt;h1&gt;Test String&lt;/h1&gt;";
        String expected = "<h1>Test String</h1>";
        assertEquals(expected, Entities.unescape(string, true));
    }

    @Test
    public void testUnescape_strict_edgeCase() {
        String string = "";
        String expected = "";
        assertEquals(expected, Entities.unescape(string, true));
    }
    
    @Test
    public void testEscapeMode_getMap_typicalUseCase() {
        List<Character> expectedKeys = Arrays.asList('<', '>', '&', '\'', '"');
        List<String> expectedValues = Arrays.asList("lt", "gt", "amp", "apos", "quot");

        Entities.EscapeMode escapeMode = Entities.EscapeMode.xhtml;
        assertEquals(expectedKeys.size(), escapeMode.getMap().size());
        assertEquals(expectedValues.size(), escapeMode.getMap().size());
        assertTrue(escapeMode.getMap().keySet().containsAll(expectedKeys));
        assertTrue(escapeMode.getMap().values().containsAll(expectedValues));
    }

    @Test
    public void testEscapeMode_getMap_edgeCase() {
        Entities.EscapeMode escapeMode = Entities.EscapeMode.extended;
        assertEquals(0, escapeMode.getMap().size());
    }
}

