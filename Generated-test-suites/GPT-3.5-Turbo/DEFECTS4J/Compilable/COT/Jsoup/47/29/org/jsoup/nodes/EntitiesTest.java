package org.jsoup.nodes;
import org.junit.Test;
import org.jsoup.nodes.Entities;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertFalse(Entities.isNamedEntity("foo"));
        assertTrue(Entities.isNamedEntity("amp"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertFalse(Entities.isBaseNamedEntity("foo"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("foo"));
    }

    @Test
    public void testEscape() {
        String string = "<foo>&amp;";
        String escapedString = Entities.escape(string, null);
        assertEquals("&lt;foo&gt;&amp;amp;", escapedString);
    }

    @Test
    public void testUnescape() {
        String string = "&lt;foo&gt;&amp;amp;";
        String unescapedString = Entities.unescape(string);
        assertEquals("<foo>&amp;", unescapedString);
    }

    @Test
    public void testUnescapeStrict() {
        String string = "&lt;foo&gt;&amp;amp;";
        String unescapedString = Entities.unescape(string, true);
        assertEquals("<foo>&amp;", unescapedString);
    }

    // Additional test cases for edge cases and exception handling

    @Test
    public void testEscapeEmptyString() {
        String string = "";
        String escapedString = Entities.escape(string, null);
        assertEquals("", escapedString);
    }

    @Test
    public void testUnescapeEmptyString() {
        String string = "";
        String unescapedString = Entities.unescape(string);
        assertEquals("", unescapedString);
    }

    @Test
    public void testEscapeNullString() {
        String string = null;
        String escapedString = Entities.escape(string, null);
        assertNull(escapedString);
    }

    @Test
    public void testUnescapeNullString() {
        String string = null;
        String unescapedString = Entities.unescape(string);
        assertNull(unescapedString);
    }

}