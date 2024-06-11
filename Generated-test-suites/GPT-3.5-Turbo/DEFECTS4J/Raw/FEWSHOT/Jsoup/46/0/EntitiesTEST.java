package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharsetEncoder;
import java.util.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("foo"));
        assertFalse(Entities.isNamedEntity(null));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("foo"));
        assertFalse(Entities.isBaseNamedEntity(null));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("foo"));
        assertNull(Entities.getCharacterByName(null));
    }
    
    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        assertEquals("foo", Entities.escape("foo", out));
        assertEquals("a &lt; b", Entities.escape("a < b", out));
        assertEquals("a &gt; b", Entities.escape("a > b", out));
        assertEquals("foo &amp; bar", Entities.escape("foo & bar", out));
        assertEquals("&quot;foo&quot;", Entities.escape("\"foo\"", out));
    }
    
    @Test
    public void testUnescape() {
        assertEquals("foo", Entities.unescape("foo"));
        assertEquals("a < b", Entities.unescape("a &lt; b"));
        assertEquals("a > b", Entities.unescape("a &gt; b"));
        assertEquals("foo & bar", Entities.unescape("foo &amp; bar"));
        assertEquals("\"foo\"", Entities.unescape("&quot;foo&quot;"));
    }
    
    @Test
    public void testUnescapeStrict() {
        assertEquals("foo", Entities.unescape("foo", true));
        assertEquals("a < b", Entities.unescape("a &lt; b", true));
        assertEquals("a > b", Entities.unescape("a &gt; b", true));
        assertEquals("foo & bar", Entities.unescape("foo &amp; bar", true));
        assertEquals("\"foo\"", Entities.unescape("&quot;foo&quot;", true));
    }
    
    // Helper Methods
    
    private Set<Character> getCharacterSet(String str) {
        Set<Character> characterSet = new HashSet<>();
        for (char c : str.toCharArray()) {
            characterSet.add(c);
        }
        return characterSet;
    }
    
    private List<Character> getCharacterList(String str) {
        List<Character> characterList = new ArrayList<>();
        for (char c : str.toCharArray()) {
            characterList.add(c);
        }
        return characterList;
    }
}