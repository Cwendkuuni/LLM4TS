package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Typical use cases
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("quot"));

        // Edge case: empty string
        assertFalse(Entities.isNamedEntity(""));

        // Edge case: non-existent entity
        assertFalse(Entities.isNamedEntity("nonexistent"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Typical use cases
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        
        // Edge case: empty string
        assertFalse(Entities.isBaseNamedEntity(""));

        // Edge case: non-existent entity
        assertFalse(Entities.isBaseNamedEntity("nonexistent"));
    }

    @Test
    public void testGetCharacterByName() {
        // Typical use cases
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        
        // Edge case: empty string
        assertNull(Entities.getCharacterByName(""));

        // Edge case: non-existent entity
        assertNull(Entities.getCharacterByName("nonexistent"));
    }

    @Test
    public void testEscape() {
        // Typical use cases
        assertEquals("&amp;", Entities.escape("&", Document.OutputSettings.html()));
        assertEquals("&lt;", Entities.escape("<", Document.OutputSettings.html()));
        assertEquals("&gt;", Entities.escape(">", Document.OutputSettings.html()));
        assertEquals("&quot;", Entities.escape("\"", Document.OutputSettings.html()));

        // Edge case: empty string
        assertEquals("", Entities.escape("", Document.OutputSettings.html()));
    }

    @Test
    public void testUnescape() {
        // Typical use cases
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("\"", Entities.unescape("&quot;"));

        // Edge case: empty string
        assertEquals("", Entities.unescape(""));

        // Error scenario: strict unescaping with missing trailing ';'
        assertEquals("&lt;", Entities.unescape("&lt"));

        // Error scenario: strict unescaping with non-existent entity
        assertEquals("&nonexistent;", Entities.unescape("&nonexistent;", true));
    }

    @Test
    public void testEscapeAdvanced() {
        // Typical use case: Escaping non-ASCII characters
        assertEquals("&#x00E9;", Entities.escape("é", Document.OutputSettings.html()));
    }

    @Test
    public void testUnescapeAdvanced() {
        // Typical use case: Unescaping non-ASCII characters
        assertEquals("é", Entities.unescape("&#x00E9;"));

        // Error scenario: Unescaping non-existent entity
        assertEquals("&nonexistent;", Entities.unescape("&nonexistent;"));
    }

    @Test
    public void testCanEncodeAscii() {
        // Typical use case: ASCII character
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, 'A', null));

        // Edge cases: ASCII control characters
        assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, '\u007F', null));
        assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, '\0', null));
    }

    @Test
    public void testCanEncodeUtf() {
        // Typical use case: Non-ASCII character
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'é', null));

        // Edge case: Surrogate pair
        assertFalse(Entities.canEncode(Entities.CoreCharset.utf, '\uD83D', null));
        assertFalse(Entities.canEncode(Entities.CoreCharset.utf, '\uDC4D', null));
    }

    @Test
    public void testCanEncodeFallback() {
        // Typical use case: Character from a fallback charset
        assertTrue(Entities.canEncode(Entities.CoreCharset.fallback, 'ß', null));

        // Edge case: Invalid character
        assertFalse(Entities.canEncode(Entities.CoreCharset.fallback, '\uFFFF', null));
    }

    @Test
    public void testCanEncodeDefaultEncoderUtf8() {
        // Typical use case: UTF-8 encoding can encode non-ASCII characters
        Entities.CoreCharset coreCharset = Entities.CoreCharset.byName("UTF-8");
        assertTrue(Entities.canEncode(coreCharset, 'é', null));
    }

    @Test
    public void testCanEncodeDefaultEncoderAscii() {
        // Typical use case: ASCII encoding can only encode ASCII characters
        Entities.CoreCharset coreCharset = Entities.CoreCharset.byName("US-ASCII");
        assertTrue(Entities.canEncode(coreCharset, 'A', null));
        assertFalse(Entities.canEncode(coreCharset, 'é', null));
    }
}