package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        for (Map.Entry<Character, String> entry : Entities.full.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue().toString();
            assertTrue("Named entity " + value + " is not recognized as named entity", Entities.isNamedEntity(value));
            assertTrue("Named entity " + key + " is not recognized as named entity", Entities.isNamedEntity(key.toString()));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        for (Map.Entry<Character, String> entry : Entities.base.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue().toString();
            assertTrue("Named entity " + value + " is not recognized as base named entity", Entities.isBaseNamedEntity(value));
            assertTrue("Named entity " + key + " is not recognized as base named entity", Entities.isBaseNamedEntity(key.toString()));
        }

        assertFalse("lt is not a base named entity", Entities.isBaseNamedEntity("lt"));
        assertFalse("amp is not a base named entity", Entities.isBaseNamedEntity("amp"));
    }

    @Test
    public void testGetCharacterByName() {
        for (Map.Entry<Character, String> entry : Entities.full.entrySet()) {
            Character key = entry.getKey();
            Character value = entry.getValue();
            assertEquals("Named entity " + value + " should return character " + key, key, Entities.getCharacterByName(value.toString()));
        }
    }

    @Test
    public void testEscapeModeXhtml() {
        assertEquals("&quot;", Entities.escape("\"", Entities.EscapeMode.xhtml.getMap()));
        assertEquals("&amp;", Entities.escape("&", Entities.EscapeMode.xhtml.getMap()));
        assertEquals("&lt;", Entities.escape("<", Entities.EscapeMode.xhtml.getMap()));
        assertEquals("&gt;", Entities.escape(">", Entities.EscapeMode.xhtml.getMap()));
    }

    @Test
    public void testEscapeModeBase() {
        assertEquals("&quot;", Entities.escape("\"", Entities.EscapeMode.base.getMap()));
        assertEquals("&amp;", Entities.escape("&", Entities.EscapeMode.base.getMap()));
        assertEquals("&lt;", Entities.escape("<", Entities.EscapeMode.base.getMap()));
        assertEquals("&gt;", Entities.escape(">", Entities.EscapeMode.base.getMap()));
        assertEquals("&nbsp;", Entities.escape("\u00A0", Entities.EscapeMode.base.getMap()));
    }

    @Test
    public void testEscapeModeExtended() {
        assertEquals("&quot;", Entities.escape("\"", Entities.EscapeMode.extended.getMap()));
        assertEquals("&amp;", Entities.escape("&", Entities.EscapeMode.extended.getMap()));
        assertEquals("&lt;", Entities.escape("<", Entities.EscapeMode.extended.getMap()));
        assertEquals("&gt;", Entities.escape(">", Entities.EscapeMode.extended.getMap()));
        assertEquals("&nbsp;", Entities.escape("\u00A0", Entities.EscapeMode.extended.getMap()));
        // other entities
        assertEquals("&eacute;", Entities.escape("é", Entities.EscapeMode.extended.getMap()));
        assertEquals("&OElig;", Entities.escape("Ö", Entities.EscapeMode.extended.getMap()));
    }

    @Test
    public void testUnescape() {
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
    }

    @Test
    public void testCanEncode() {
        assertFalse("ASCII char '!' cannot be encoded with ascii CharsetEncoder", Entities.canEncode(Entities.CoreCharset.ascii, '!', null));
        assertTrue("ASCII char 'A' can be encoded with ascii CharsetEncoder", Entities.canEncode(Entities.CoreCharset.ascii, 'A', null));
        assertTrue("UTF-8 char 'é' can be encoded with UTF-8 CharsetEncoder", Entities.canEncode(Entities.CoreCharset.utf, 'é', null));
        assertTrue("UTF-8 char 'Ö' can be encoded with UTF-8 CharsetEncoder", Entities.canEncode(Entities.CoreCharset.utf, 'Ö', null));
    }
}