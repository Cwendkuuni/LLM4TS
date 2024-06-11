package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertFalse(Entities.isNamedEntity("foo"));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertFalse(Entities.isBaseNamedEntity("foo"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertNull(Entities.getCharacterByName("foo"));
    }
    
    @Test
    public void testEscape() {
        String string = "<html>";
        assertEquals("&lt;html&gt;", Entities.escape(string, new Document.OutputSettings()));
        assertEquals("<html>", Entities.escape(string, new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml)));
    }
    
    @Test
    public void testUnescape() {
        String string = "&lt;html&gt;";
        assertEquals("<html>", Entities.unescape(string, false));
        assertEquals("&lt;html&gt;", Entities.unescape(string, true));
    }
    
    @Test
    public void testEscape_mode_xhtml() {
        assertEquals("quot", Entities.EscapeMode.xhtml.getMap().get(Character.valueOf('"')));
        assertEquals("amp", Entities.EscapeMode.xhtml.getMap().get(Character.valueOf('&')));
        assertEquals("lt", Entities.EscapeMode.xhtml.getMap().get(Character.valueOf('<')));
        assertEquals("gt", Entities.EscapeMode.xhtml.getMap().get(Character.valueOf('>')));
    }
    
    @Test
    public void testEscape_mode_base() {
        assertEquals("quot", Entities.EscapeMode.base.getMap().get(Character.valueOf('"')));
        assertEquals("amp", Entities.EscapeMode.base.getMap().get(Character.valueOf('&')));
        assertEquals("lt", Entities.EscapeMode.base.getMap().get(Character.valueOf('<')));
        assertEquals("gt", Entities.EscapeMode.base.getMap().get(Character.valueOf('>')));
    }
    
    @Test
    public void testEscape_mode_extended() {
        assertEquals("quot", Entities.EscapeMode.extended.getMap().get(Character.valueOf('"')));
        assertEquals("amp", Entities.EscapeMode.extended.getMap().get(Character.valueOf('&')));
        assertEquals("lt", Entities.EscapeMode.extended.getMap().get(Character.valueOf('<')));
        assertEquals("gt", Entities.EscapeMode.extended.getMap().get(Character.valueOf('>')));
    }
}