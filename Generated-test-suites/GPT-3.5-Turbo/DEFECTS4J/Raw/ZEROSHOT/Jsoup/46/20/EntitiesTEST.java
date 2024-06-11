package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharsetEncoder;
import java.util.*;

import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("unknown"));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("unknown"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("unknown"));
    }
    
    @Test
    public void testEscape() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        
        String escapedString = Entities.escape("<tag>", outputSettings);
        assertEquals("&lt;tag&gt;", escapedString);
        
        escapedString = Entities.escape("Hello &amp; World", outputSettings);
        assertEquals("Hello &amp;amp; World", escapedString);
        
        escapedString = Entities.escape("Hello & world", outputSettings);
        assertEquals("Hello &amp; world", escapedString);
    }
    
    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&lt;tag&gt;");
        assertEquals("<tag>", unescapedString);
        
        unescapedString = Entities.unescape("Hello &amp;amp; World");
        assertEquals("Hello &amp; World", unescapedString);
        
        unescapedString = Entities.unescape("Hello & world");
        assertEquals("Hello & world", unescapedString);
    }
}