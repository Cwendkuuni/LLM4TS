package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {

    private Entities entities;

    @Before
    public void setUp() {
        entities = new Entities();
    }

    @Test
    public void testIsNamedEntity_True() {
        assertTrue(Entities.isNamedEntity("lt"));
    }

    @Test
    public void testIsNamedEntity_False() {
        assertFalse(Entities.isNamedEntity("abcd"));
    }

    @Test
    public void testIsBaseNamedEntity_True() {
        assertTrue(Entities.isBaseNamedEntity("gt"));
    }

    @Test
    public void testIsBaseNamedEntity_False() {
        assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName_ValidName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
    }

    @Test
    public void testGetCharacterByName_InvalidName() {
        assertNull(Entities.getCharacterByName("abcd"));
    }

    @Test
    public void testEscape_NoSpecialCharacters() {
        String input = "This is a test";
        String expected = input;

        String result = Entities.escape(input, null);

        assertEquals(expected, result);
    }

    @Test
    public void testEscape_WithSpecialCharacters() {
        String input = "<html>";
        String expected = "&lt;html&gt;";

        String result = Entities.escape(input, null);

        assertEquals(expected, result);
    }

    @Test
    public void testEscape_NormaliseWhite_StripLeadingWhite() {
        String input = "  This is a test";
        String expected = " This is a test";

        String result = Entities.escape(input, null);

        assertEquals(expected, result);
    }

    @Test
    public void testEscape_NormaliseWhite_KeepLeadingWhite() {
        String input = "  This is a test";
        String expected = "  This is a test";

        String result = Entities.escape(input, null);

        assertEquals(expected, result);
    }

    @Test
    public void testUnescape_NoSpecialCharacters() {
        String input = "This is a test";
        String expected = input;

        String result = Entities.unescape(input);

        assertEquals(expected, result);
    }

    @Test
    public void testUnescape_WithSpecialCharacters() {
        String input = "&lt;html&gt;";
        String expected = "<html>";

        String result = Entities.unescape(input);

        assertEquals(expected, result);
    }

    @Test
    public void testUnescape_Strict_NoSemicolon() {
        String input = "&quot";
        String expected = input;

        String result = Entities.unescape(input, true);

        assertEquals(expected, result);
    }

    @Test
    public void testUnescape_Strict_WithSemicolon() {
        String input = "&quot;";
        String expected = "\"";

        String result = Entities.unescape(input, true);

        assertEquals(expected, result);
    }

    @Test
    public void testUnescape_NonStrict_NoSemicolon() {
        String input = "&quot";
        String expected = "\"";

        String result = Entities.unescape(input, false);

        assertEquals(expected, result);
    }

    @Test
    public void testUnescape_NonStrict_WithSemicolon() {
        String input = "&quot;";
        String expected = "\"";

        String result = Entities.unescape(input, false);

        assertEquals(expected, result);
    }

    @Test
    public void testCanEncode_Ascii() {
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, 'A', null));
    }

    @Test
    public void testCanEncode_Utf() {
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, '\u0394', null));
    }

    @Test
    public void testCanEncode_Fallback() {
        assertTrue(Entities.canEncode(Entities.CoreCharset.fallback, 'A', null));
    }

    @Test
    public void testCanEncode_Fallback_CannotEncode() {
        assertFalse(Entities.canEncode(Entities.CoreCharset.fallback, '\u00A9', null));
    }

    @Test
    public void testLoadEntities_ValidFile() {
        String filename = "entities-base.properties";

        assertDoesNotThrow(() -> Entities.loadEntities(filename));
    }

    @Test
    public void testLoadEntities_InvalidFile() {
        String filename = "entities-invalid.properties";

        assertThrows(MissingResourceException.class, () -> Entities.loadEntities(filename));
    }

    @Test
    public void testToCharacterKey_EmptyMap() {
        Map<String, Character> input = new HashMap<>();
        Map<Character, String> expected = new HashMap<>();

        Map<Character, String> result = Entities.toCharacterKey(input);

        assertEquals(expected, result);
    }

    @Test
    public void testToCharacterKey_NonEmptyMap() {
        Map<String, Character> input = new HashMap<>();
        input.put("lt", '<');
        input.put("gt", '>');

        Map<Character, String> expected = new HashMap<>();
        expected.put('<', "lt");
        expected.put('>', "gt");

        Map<Character, String> result = Entities.toCharacterKey(input);

        assertEquals(expected, result);
    }

    @Test
    public void testEscape_Entities_XHTML() {
        Entities.EscapeMode escapeMode = Entities.EscapeMode.xhtml;
        String input = "<html>";
        String expected = "&lt;html&gt;";

        Document.OutputSettings out = new Document.OutputSettings().escapeMode(escapeMode);
        String result = Entities.escape(input, out);

        assertEquals(expected, result);
    }

    @Test
    public void testEscape_Entities_Base() {
        Entities.EscapeMode escapeMode = Entities.EscapeMode.base;
        String input = "<html>";
        String expected = "<html>";

        Document.OutputSettings out = new Document.OutputSettings().escapeMode(escapeMode);
        String result = Entities.escape(input, out);

        assertEquals(expected, result);
    }

    @Test
    public void testEscape_Entities_Extended() {
        Entities.EscapeMode escapeMode = Entities.EscapeMode.extended;
        String input = "<html>";
        String expected = "<html>";

        Document.OutputSettings out = new Document.OutputSettings().escapeMode(escapeMode);
        String result = Entities.escape(input, out);

        assertEquals(expected, result);
    }
}