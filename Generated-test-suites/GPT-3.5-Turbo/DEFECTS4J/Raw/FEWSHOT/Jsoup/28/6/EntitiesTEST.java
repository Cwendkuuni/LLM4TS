package org.jsoup.nodes;

import org.junit.Test;

import java.nio.charset.CharsetEncoder;
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
        assertEquals(Character.valueOf('\"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("foo"));
        assertNull(Entities.getCharacterByName(""));
    }

    @Test
    public void testEscape() {
        assertEquals("abc", Entities.escape("abc", Document.OutputSettings.escapeMode));
        assertEquals("a&amp;b", Entities.escape("a&b", Document.OutputSettings.escapeMode));
        assertEquals("a&lt;b&gt;c", Entities.escape("a<b>c", Document.OutputSettings.escapeMode));
        assertEquals("a&#39;b&quot;c", Entities.escape("a'b\"c", Document.OutputSettings.escapeMode));
    }

    @Test
    public void testUnescape() {
        assertEquals("abc", Entities.unescape("abc"));
        assertEquals("a&b", Entities.unescape("a&amp;b"));
        assertEquals("a<b>c", Entities.unescape("a&lt;b&gt;c"));
        assertEquals("a'b\"c", Entities.unescape("a&#39;b&quot;c"));
    }
}