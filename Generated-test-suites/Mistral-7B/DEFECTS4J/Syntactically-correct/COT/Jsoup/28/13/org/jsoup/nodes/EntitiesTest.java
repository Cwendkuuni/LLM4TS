package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for Entities.
 */
public class EntitiesTest {

    private static Map<Character, String> baseByVal;
    private static Map<Character, String> fullByVal;

    @BeforeClass
    public static void init() {
        baseByVal = Entities.EscapeMode.base.getMap();
        fullByVal = Entities.EscapeMode.extended.getMap();
    }

    @Test
    public void testIsNamedEntity_true() {
        assertTrue(Entities.isNamedEntity("lt"));
    }

    @Test
    public void testIsNamedEntity_false() {
        assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void testGetCharacterByName_returnsExpectedCharacter() {
        assertEquals('<', Entities.getCharacterByName("lt"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetCharacterByName_nullName() {
        Entities.getCharacterByName(null);
    }

    @Test
    public void testEscape_normal() {
        String input = "This is a test & this is a test.";
        String expected = "This is a test &amp; this is a test.";
        assertEquals(expected, Entities.escape(input, null, Entities.EscapeMode.base));
    }

    @Test
    public void testEscape_unicode() {
        String input = "This is a test ♥ this is a test.";
        String expected = "This is a test &heta; this is a test.";
        assertEquals(expected, Entities.escape(input, null, Entities.EscapeMode.base));
    }

    @Test
    public void testEscape_customEncoder() {
        CharsetEncoder encoder = (charset, unicode) -> "test".getBytes("UTF-8");
        String input = "This is a test ♥ this is a test.";
        String expected = "This is a test &heta; this is a test.";
        assertEquals(expected, Entities.escape(input, encoder, Entities.EscapeMode.base));
    }

    @Test(expected = NullPointerException.class)
    public void testEscape_nullInput() {
        Entities.escape(null, null, Entities.EscapeMode.base);
    }

    @Test
    public void testUnescape_normal() {
        String input = "This is a test &amp; this is a test.";
        String expected = "This is a test & this is a test.";
        assertEquals(expected, Entities.unescape(input));
    }

    @Test
    public void testUnescape_unicode() {
        String input = "This is a test &heta; this is a test.";
        String expected = "This is a test ♥ this is a test.";
        assertEquals(expected, Entities.unescape(input));
    }

    @Test
    public void testUnescape_strict() {
        String input = "This is a test &heta; this is a test.";
        String expected = "This is a test &heta; this is a test.";
        assertEquals(expected, Entities.unescape(input, true));
    }

    @Test
    public void testUnescape_invalid() {
        String input = "This is a test &asdf; this is a test.";
        assertNull(Entities.unescape(input));
    }

    @Test
    public void testIsNamedEntity_base() {
        assertTrue(Entities.isNamedEntity("lt", Entities.EscapeMode.base));
        assertFalse(Entities.isNamedEntity("asdf", Entities.EscapeMode.base));
    }

    @Test
    public void testIsNamedEntity_xhtml() {
        assertTrue(Entities.isNamedEntity("quot", Entities.EscapeMode.xhtml));
        assertFalse(Entities.isNamedEntity("asdf", Entities.EscapeMode.xhtml));
    }

    @Test
    public void testIsNamedEntity_extended() {
        assertTrue(Entities.isNamedEntity("copy;", Entities.EscapeMode.extended));
        assertFalse(Entities.isNamedEntity("asdf", Entities.EscapeMode.extended));
    }

    @Test
    public void testGetCharacterByName_base() {
        assertEquals('>', Entities.getCharacterByName("gt", Entities.EscapeMode.base));
        assertNull(Entities.getCharacterByName("asdf", Entities.EscapeMode.base));
    }

    @Test
    public void testGetCharacterByName_xhtml() {
        assertEquals("'", Entities.getCharacterByName("apos", Entities.EscapeMode.xhtml));
        assertNull(Entities.getCharacterByName("asdf", Entities.EscapeMode.xhtml));
    }

    @Test
    public void testGetCharacterByName_extended() {
        assertEquals("®", Entities.getCharacterByName("reg;", Entities.EscapeMode.extended));
        assertNull(Entities.getCharacterByName("asdf", Entities.EscapeMode.extended));
    }

    @Test
    public void testToString() {
        assertEquals("mapSize:11", Entities.EscapeMode.base.toString());
        assertEquals("mapSize:62", Entities.EscapeMode.extended.toString());
    }
}