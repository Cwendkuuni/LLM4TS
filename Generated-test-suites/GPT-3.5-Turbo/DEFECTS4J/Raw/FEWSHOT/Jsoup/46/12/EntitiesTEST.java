
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.jsoup.Jsoup;
import org.junit.Test;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity("abc"));
        assertFalse(Entities.isNamedEntity("&"));
        assertFalse(Entities.isNamedEntity("&amp;"));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertFalse(Entities.isBaseNamedEntity(""));
        assertFalse(Entities.isBaseNamedEntity("abc"));
        assertFalse(Entities.isBaseNamedEntity("&"));
        assertFalse(Entities.isBaseNamedEntity("&amp;"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(null, Entities.getCharacterByName(""));
        assertEquals(null, Entities.getCharacterByName("abc"));
        assertEquals(null, Entities.getCharacterByName("&"));
        assertEquals(null, Entities.getCharacterByName("&amp;"));
    }
    
    @Test
    public void testEscape() {
        assertEquals("foo", Entities.escape("foo", new Document.OutputSettings()));
        assertEquals("abc", Entities.escape("abc", new Document.OutputSettings()));
        assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings()));
        assertEquals("&gt;", Entities.escape(">", new Document.OutputSettings()));
        assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings()));
        assertEquals("&quot;", Entities.escape("\"", new Document.OutputSettings()));
    }
    
    @Test
    public void testUnescape() {
        assertEquals("foo", Entities.unescape("foo"));
        assertEquals("abc", Entities.unescape("abc"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\"", Entities.unescape("&quot;"));
    }
}

