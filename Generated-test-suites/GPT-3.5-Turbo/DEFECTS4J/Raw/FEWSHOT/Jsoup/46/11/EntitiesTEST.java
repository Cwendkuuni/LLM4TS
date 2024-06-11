package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("foo"));
        assertFalse(Entities.isNamedEntity("bar"));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("foo"));
        assertFalse(Entities.isBaseNamedEntity("bar"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("foo"));
        assertNull(Entities.getCharacterByName("bar"));
    }
    
    @Test
    public void testEscape() {
        String unescaped = "<html> & \"";
        String escaped = Entities.escape(unescaped, new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        
        assertEquals("&lt;html&gt; &amp; &quot;", escaped);
    }
    
    @Test
    public void testUnescape() {
        String escaped = "&lt;html&gt; &amp; &quot;";
        String unescaped = Entities.unescape(escaped);
        
        assertEquals("<html> & \"", unescaped);
    }    
    
    @Test
    public void testEscapeModeXhtml() {
        Map<Character, String> xhtmlMap = Entities.EscapeMode.xhtml.getMap();
        
        assertEquals(4, xhtmlMap.size());
        assertEquals("&quot;", xhtmlMap.get('\"'));
        assertEquals("&amp;", xhtmlMap.get('&'));
        assertEquals("&lt;", xhtmlMap.get('<'));
        assertEquals("&gt;", xhtmlMap.get('>'));
    }
    
    @Test
    public void testEscapeModeBase() {
        Map<Character, String> baseMap = Entities.EscapeMode.base.getMap();
        
        assertEquals(2128, baseMap.size());
        assertEquals("reg;", baseMap.get('\u00AE'));
        assertEquals("Ouml;", baseMap.get('\u00D6'));
        assertEquals("Yacute;", baseMap.get('\u00DD'));
        assertEquals("zigrarr;", baseMap.get('\u21DD'));
    }
    
    @Test
    public void testEscapeModeExtended() {
        Map<Character, String> extendedMap = Entities.EscapeMode.extended.getMap();
        
        assertEquals(2530, extendedMap.size());
        assertEquals("HalfUpArrow;", extendedMap.get('\u21CD'));
        assertEquals("Iukcy;", extendedMap.get('\u0406'));
        assertEquals("curren;", extendedMap.get('\u00A4'));
        assertEquals("othorn;", extendedMap.get('\u01EB'));
    }
}