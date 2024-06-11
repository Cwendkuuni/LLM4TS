package org.jsoup.nodes;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("notentity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("notentity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("notentity"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        
        String escaped = Entities.escape("&<>'\"", outputSettings);
        
        assertEquals("&amp;&lt;&gt;&#34;", escaped);
    }

    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("&amp;&lt;&gt;&#34;", false);
        assertEquals("&<>'\"", unescaped);
    }
    
    @Test
    public void testEscapeWithAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        
        String escaped = Entities.escape("&<>'\"", outputSettings, true, false, false);
        
        assertEquals("&amp;<>'\"", escaped);
    }

    @Test
    public void testUnescapeStrict() {
        String unescaped = Entities.unescape("&amp;&lt;&gt;&#34;", true);
        assertEquals("&<>'\"", unescaped);
    }
    
    @Test
    public void testCanEncode() {
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, '<', null));
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'a', null));
        assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, '\u00A0', null));
        assertTrue(Entities.canEncode(Entities.CoreCharset.fallback, 'a', null));
    }
}