package org.jsoup.nodes;
import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("nbsp"));
    }

    @Test
    public void testGetCharacterByName() {
        Character lt = Entities.getCharacterByName("lt");
        assertNotNull(lt);
        assertEquals('<', lt.charValue());

        Character gt = Entities.getCharacterByName("gt");
        assertNotNull(gt);
        assertEquals('>', gt.charValue());

        Character amp = Entities.getCharacterByName("amp");
        assertNotNull(amp);
        assertEquals('&', amp.charValue());

        Character nbsp = Entities.getCharacterByName("nbsp");
        assertNull(nbsp);
    }

    @Test
    public void testEscape() {
        String input = "This is <html> & \"escape\" test";
        String expected = "This is &lt;html&gt; &amp; &quot;escape&quot; test";
        String escaped = Entities.escape(input, null);
        assertEquals(expected, escaped);
    }

    @Test
    public void testUnescape() {
        String input = "This is &lt;html&gt; &amp; &quot;escape&quot; test";
        String expected = "This is <html> & \"escape\" test";
        String unescaped = Entities.unescape(input);
        assertEquals(expected, unescaped);
    }

    @Test
    public void testUnescapeStrict() {
        String input = "This is &lt;html&gt; &amp; &quot;escape&quot; test;";
        String expected = "This is <html> & \"escape\" test";
        String unescaped = Entities.unescape(input, true);
        assertEquals(expected, unescaped);
    }
}