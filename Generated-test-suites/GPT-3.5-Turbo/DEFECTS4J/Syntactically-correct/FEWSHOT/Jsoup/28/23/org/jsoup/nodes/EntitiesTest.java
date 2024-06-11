package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.charset.CharsetEncoder;
import java.util.*;

public class EntitiesTest {
    private CharsetEncoder encoder = CharsetEncoder.defaultCharset().newEncoder();

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("gt"));

        assertFalse(Entities.isNamedEntity("foo"));
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf((char) 60), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf((char) 38), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf((char) 39), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf((char) 62), Entities.getCharacterByName("gt"));

        assertNull(Entities.getCharacterByName("foo"));
        assertNull(Entities.getCharacterByName(""));
    }

    @Test
    public void testEscape() {
        assertEquals("quot", Entities.escape("\"", encoder, Entities.EscapeMode.xhtml));
        assertEquals("amp", Entities.escape("&", encoder, Entities.EscapeMode.xhtml));
        assertEquals("apos", Entities.escape("'", encoder, Entities.EscapeMode.xhtml));
        assertEquals("lt", Entities.escape("<", encoder, Entities.EscapeMode.xhtml));
        assertEquals("gt", Entities.escape(">", encoder, Entities.EscapeMode.xhtml));

        assertEquals("quot", Entities.escape("\"", encoder, Entities.EscapeMode.base));
        assertEquals("amp", Entities.escape("&", encoder, Entities.EscapeMode.base));
        assertEquals("apos", Entities.escape("'", encoder, Entities.EscapeMode.base));
        assertEquals("lt", Entities.escape("<", encoder, Entities.EscapeMode.base));
        assertEquals("gt", Entities.escape(">", encoder, Entities.EscapeMode.base));

        assertEquals("#34", Entities.escape("\"", encoder, Entities.EscapeMode.extended));
        assertEquals("#38", Entities.escape("&", encoder, Entities.EscapeMode.extended));
        assertEquals("#39", Entities.escape("'", encoder, Entities.EscapeMode.extended));
        assertEquals("#60", Entities.escape("<", encoder, Entities.EscapeMode.extended));
        assertEquals("#62", Entities.escape(">", encoder, Entities.EscapeMode.extended));
    }

    @Test
    public void testUnescape() {
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("'", Entities.unescape("&apos;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&lt;&amp;&gt;", Entities.unescape("&lt;&amp;&gt;"));

        assertEquals("&quot;", Entities.unescape("quot", true));
        assertEquals("&amp;", Entities.unescape("amp", true));
        assertEquals("&apos;", Entities.unescape("apos", true));
        assertEquals("&lt;", Entities.unescape("lt", true));
        assertEquals("&gt;", Entities.unescape("gt", true));
        assertEquals("&lt;&amp;&gt;", Entities.unescape("lt&amp;gt;", true));
    }
}