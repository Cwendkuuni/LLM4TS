package org.jsoup.nodes;

// Import statements

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharsetEncoder;
import java.util.*;

import static org.junit.Assert.*;

public class EntitiesTest {

    // Test cases for isNamedEntity method
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("abc"));
    }

    // Test cases for isBaseNamedEntity method
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("abc"));
    }

    // Test cases for getCharacterByName method
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("abc"));
    }

    // Test cases for escape method
    @Test
    public void testEscape() {
        assertEquals("&amp;", Entities.escape("&", Document.OutputSettings.xhtml()));
        assertEquals("&nbsp;", Entities.escape("\u00A0", Document.OutputSettings.xhtml()));
        assertEquals("&lt;", Entities.escape("<", Document.OutputSettings.xhtml()));
        assertEquals("&gt;", Entities.escape(">", Document.OutputSettings.xhtml()));
        assertEquals("&quot;", Entities.escape("\"", Document.OutputSettings.xhtml()));
        assertEquals("abc", Entities.escape("abc", Document.OutputSettings.xhtml()));
    }

    // Test cases for unescape method
    @Test
    public void testUnescape() {
        assertEquals("&", Entities.unescape("&amp;"));
        assertNull(Entities.unescape("&abc;"));
    }

}