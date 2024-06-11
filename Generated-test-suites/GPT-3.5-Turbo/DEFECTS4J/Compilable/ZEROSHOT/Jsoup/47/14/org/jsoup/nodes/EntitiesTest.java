package org.jsoup.nodes;
import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        boolean result = Entities.isNamedEntity("lt");
        assertTrue(result);
        
        result = Entities.isNamedEntity("gt");
        assertTrue(result);
        
        result = Entities.isNamedEntity("amp");
        assertTrue(result);
        
        result = Entities.isNamedEntity("quot");
        assertTrue(result);
        
        result = Entities.isNamedEntity("invalid");
        assertFalse(result);
    }

    @Test
    public void testIsBaseNamedEntity() {
        boolean result = Entities.isBaseNamedEntity("lt");
        assertTrue(result);
        
        result = Entities.isBaseNamedEntity("gt");
        assertFalse(result);
        
        result = Entities.isBaseNamedEntity("amp");
        assertFalse(result);
        
        result = Entities.isBaseNamedEntity("quot");
        assertFalse(result);
        
        result = Entities.isBaseNamedEntity("invalid");
        assertFalse(result);
    }

    @Test
    public void testGetCharacterByName() {
        Character result = Entities.getCharacterByName("lt");
        assertEquals(result, Character.valueOf('<'));
        
        result = Entities.getCharacterByName("gt");
        assertEquals(result, Character.valueOf('>'));
        
        result = Entities.getCharacterByName("amp");
        assertEquals(result, Character.valueOf('&'));
        
        result = Entities.getCharacterByName("quot");
        assertEquals(result, Character.valueOf('"'));
        
        result = Entities.getCharacterByName("invalid");
        assertNull(result);
    }
    
    @Test
    public void testEscape() {
        String result = Entities.escape("<html>", new Document.OutputSettings());
        assertEquals(result, "&lt;html&gt;");
        
        result = Entities.escape("<p>This is a paragraph.</p>", new Document.OutputSettings());
        assertEquals(result, "&lt;p&gt;This is a paragraph.&lt;/p&gt;");
        
        result = Entities.escape("<p>This is a paragraph.</p>", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        assertEquals(result, "&lt;p&gt;This is a paragraph.&lt;/p&gt;");
        
        result = Entities.escape("Hello&amp;World", new Document.OutputSettings());
        assertEquals(result, "Hello&amp;amp;World");
        
        result = Entities.escape("Hello&amp;World", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        assertEquals(result, "Hello&amp;amp;World");
    }

    @Test
    public void testUnescape() {
        String result = Entities.unescape("&lt;html&gt;");
        assertEquals(result, "<html>");
        
        result = Entities.unescape("&lt;p&gt;This is a paragraph.&lt;/p&gt;");
        assertEquals(result, "<p>This is a paragraph.</p>");
        
        result = Entities.unescape("Hello&amp;amp;World");
        assertEquals(result, "Hello&amp;World");
        
        result = Entities.unescape("Hello&#x3C;World");
        assertEquals(result, "Hello<World");
    }
}