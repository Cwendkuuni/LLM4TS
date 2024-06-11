package org.jsoup.nodes;

import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("foo"));
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("foo"));
        assertNull(Entities.getCharacterByName(""));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;", Entities.escape("<", Document.OutputSettings.xhtml));
        assertEquals("&gt;", Entities.escape(">", Document.OutputSettings.xhtml));
        assertEquals("&amp;", Entities.escape("&", Document.OutputSettings.xhtml));
        assertEquals("&apos;", Entities.escape("'", Document.OutputSettings.xhtml));
        assertEquals("&quot;", Entities.escape("\"", Document.OutputSettings.xhtml));
        assertEquals("&lt;&gt;&amp;&apos;&quot;", Entities.escape("<>&'\"", Document.OutputSettings.xhtml));
        assertEquals("\u20AC", Entities.escape("\u20AC", Document.OutputSettings.xhtml));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("'", Entities.unescape("&apos;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("<>&'\"", Entities.unescape("&lt;&gt;&amp;&apos;&quot;"));
        assertEquals("\u20AC", Entities.unescape("\u20AC"));
    }
}