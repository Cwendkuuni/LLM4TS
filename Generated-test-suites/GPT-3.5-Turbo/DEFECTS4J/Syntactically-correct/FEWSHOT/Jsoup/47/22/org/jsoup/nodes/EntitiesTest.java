package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.HashMap;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt")); // test with known named entity
        assertFalse(Entities.isNamedEntity("abc")); // test with unknown named entity
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt")); // test with known named entity in base set
        assertFalse(Entities.isBaseNamedEntity("epsilon")); // test with known named entity not in base set
        assertFalse(Entities.isBaseNamedEntity("abc")); // test with unknown named entity
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt")); // test with known named entity
        assertNull(Entities.getCharacterByName("abc")); // test with unknown named entity
    }

    @Test
    public void testEscape() {
        // Test escape method with various inputs and escape modes
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.escapeMode(Entities.EscapeMode.xhtml); // set escape mode to xhtml
        assertEquals("&amp;", Entities.escape("&", settings)); // test with simple special character
        assertEquals("&lt;", Entities.escape("<", settings));
        assertEquals("&#xa0;", Entities.escape("\u00a0", settings)); // test with non-breaking space
        
        settings.escapeMode(Entities.EscapeMode.base); // set escape mode to base
        assertEquals("&amp;", Entities.escape("&", settings));
        assertEquals("&lt;", Entities.escape("<", settings));
        assertEquals("&nbsp;", Entities.escape("\u00a0", settings));

        settings.escapeMode(Entities.EscapeMode.extended); // set escape mode to extended
        assertEquals("&amp;", Entities.escape("&", settings));
        assertEquals("&lt;", Entities.escape("<", settings));
        assertEquals("&nbsp;", Entities.escape("\u00a0", settings));
        
    }

    @Test
    public void testUnescape() {
        assertEquals("&", Entities.unescape("&amp;")); // test with escaped special character
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals("\u00a0", Entities.unescape("&nbsp;")); // test with escaped non-breaking space
    }
    
    @Test
    public void testEscapeAndUnescape() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.escapeMode(Entities.EscapeMode.xhtml); // set escape mode to xhtml
        
        String original = "This is a test & < > \u00a0";
        
        String escaped = Entities.escape(original, settings);
        String unescaped = Entities.unescape(escaped);
        
        assertEquals(original, unescaped); // test that escape and unescape methods are inverse of each other
    }
    
    @Test
    public void testEscapeByMode() {
        Map<Character, String> map = new HashMap<>();
        map.put('a', "&alpha;");
        map.put('b', "&beta;");
        
        Entities.EscapeMode customMode = new Entities.EscapeMode(map);
        
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.escapeMode(customMode);
        
        assertEquals("&alpha;&amp;&beta;", Entities.escape("a&b", settings)); // test with custom escape mode
    }
    
    @Test
    public void testEscapeNormaliseWhite() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.escapeMode(Entities.EscapeMode.xhtml);
        
        String original = "This\n is\na\ttest";
        String expected = "This is a test";
        
        String escaped = Entities.escape(original, settings);
        
        assertEquals(expected, escaped); // test that escape method normalizes whitespace
        
    }
    
    @Test
    public void testEscapeStripLeadingWhite() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.escapeMode(Entities.EscapeMode.xhtml);
        
        String original = "   This is a test";
        String expected = " This is a test";
        
        String escaped = Entities.escape(original, settings);
        
        assertEquals(expected, escaped); // test that escape method strips leading white space
        
    }

}