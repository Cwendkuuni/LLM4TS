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
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("gt")); // not in base set
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape_Basic() {
        String input = "&lt;abc&gt;def&amp;";
        String expectedOutput = "&amp;lt;abc&amp;gt;def&amp;amp;";
        String output = Entities.escape(input, null);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscape_XHTML() {
        String input = "<abc>&gt;def&quot;";
        String expectedOutput = "&lt;abc&gt;(&gt;)&amp;quot;";
        String output = Entities.escape(input, Entities.EscapeMode.xhtml);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscape_EdgeCases() {
        String input = "&amp;lt; &amp;#60; &amp;#x3C; < > \" &amp;";
        String expectedOutput = "&amp;amp;lt; &amp;#34; &amp;#x3C; &amp;lt; &amp;gt; &amp;quot; &amp;amp;";
        String output = Entities.escape(input, null);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscape_NullInput() {
        Entities.escape(null, null);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;abc&gt;def&amp;amp;";
        String expectedOutput = "&lt;abc&gt;def&amp;";
        String output = Entities.unescape(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCanEncode() {
        CoreCharset coreCharset = CoreCharset.utf;
        CharsetEncoder encoder = null; // not used in this test

        // common ASCII characters
        assertTrue(Entities.canEncode(coreCharset, 'A', encoder));
        assertTrue(Entities.canEncode(coreCharset, ' ', encoder));
        assertTrue(Entities.canEncode(coreCharset, '!', encoder));

        // special characters
        assertTrue(Entities.canEncode(coreCharset, '&', encoder));
        assertTrue(Entities.canEncode(coreCharset, '<', encoder));
        assertTrue(Entities.canEncode(coreCharset, '>', encoder));
        assertTrue(Entities.canEncode(coreCharset, '"', encoder));

        // surrogate pairs
        assertFalse(Entities.canEncode(coreCharset, '\uD800', encoder)); // high surrogate
        assertFalse(Entities.canEncode(coreCharset, '\uDC00', encoder)); // low surrogate

        // UTF-8 specific character
        assertFalse(Entities.canEncode(coreCharset, '\uD840', encoder)); // Unicode character outside the UTF-8 standard
    }
}