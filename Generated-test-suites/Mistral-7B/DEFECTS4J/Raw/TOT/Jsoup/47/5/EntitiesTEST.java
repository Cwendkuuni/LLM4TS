package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("unknown"));

        // Test edge case: empty string
        assertFalse(Entities.isNamedEntity(""));

        // Test error case: null input
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("lt"));

        // Test edge case: empty string
        assertFalse(Entities.isBaseNamedEntity(""));

        // Test error case: null input
        assertFalse(Entities.isBaseNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("unknown"));

        // Test edge case: empty string
        assertNull(Entities.getCharacterByName(""));

        // Test error case: null input
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscape() {
        String input = "&amp;lt;foo&gt;bar&quot;";
        String expected = "&amp;amp;lt;lt;&gt;foo&gt;bar&amp;quot;";

        String output = Entities.escape(input, Document.OutputSettings.base());
        assertEquals(expected, output);

        // Test edge case: empty string
        String emptyOutput = Entities.escape("", Document.OutputSettings.base());
        assertEquals("", emptyOutput);

        // Test error case: null input
        String nullOutput = Entities.escape(null, Document.OutputSettings.base());
        assertEquals("", nullOutput);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;foo&gt;bar&amp;quot;";
        String expected = "&lt;foo&gt;bar&quot;";

        String output = Entities.unescape(input, false);
        assertEquals(expected, output);

        // Test strict unescape
        String strictOutput = Entities.unescape(input, true);
        assertEquals(expected, strictOutput);

        // Test edge case: empty string
        String emptyInput = "";
        String emptyOutput = Entities.unescape(emptyInput, false);
        assertEquals(emptyInput, emptyOutput);

        // Test error case: null input
        String nullInput = null;
        String nullOutput = Entities.unescape(nullInput, false);
        assertEquals("", nullOutput);
    }

    @Test
    public void testCanEncode() {
        assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, '€')); // not ASCII
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, '€')); // should be UTF-8
    }
}