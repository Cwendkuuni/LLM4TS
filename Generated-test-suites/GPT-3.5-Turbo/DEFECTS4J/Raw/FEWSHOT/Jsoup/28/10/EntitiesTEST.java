
package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        
        assertFalse(Entities.isNamedEntity("foo"));
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        
        assertNull(Entities.getCharacterByName("foo"));
        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName(null));
    }
    
    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.charset(Charset.forName("UTF-8"));
        
        assertEquals("lt", Entities.escape("<", out));
        assertEquals("gt", Entities.escape(">", out));
        assertEquals("amp", Entities.escape("&", out));
        assertEquals("apos", Entities.escape("'", out));
        assertEquals("quot", Entities.escape("\"", out));
        assertEquals("&#60;", Entities.escape("&#60;", out));
        assertEquals("&amp;lt", Entities.escape("&lt", out));
        assertEquals("&amp;&amp;", Entities.escape("&&", out));
        
        assertNull(Entities.escape(null, out));
    }
    
    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("'", Entities.unescape("&apos;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("&#60;", Entities.unescape("&#60;"));
        assertEquals("&lt", Entities.unescape("&lt"));
        assertEquals("&&", Entities.unescape("&&"));
        
        assertNull(Entities.unescape(null));
    }
    
    @Test
    public void testUnescapeStrict() {
        assertEquals("<", Entities.unescape("&lt;", true));
        assertEquals(">", Entities.unescape("&gt;", true));
        assertEquals("&", Entities.unescape("&amp;", true));
        assertEquals("'", Entities.unescape("&apos;", true));
        assertEquals("\"", Entities.unescape("&quot;", true));
        assertEquals("&#60;", Entities.unescape("&#60;", true));
        assertEquals("&lt", Entities.unescape("&lt", true));
        assertEquals("&&", Entities.unescape("&&", true));
        
        assertNull(Entities.unescape(null, true));
    }
    
    @Test
    public void testUnescapePattern() {
        assertEquals("<", Entities.unescapePattern.matcher("&lt;").replaceAll("<"));
        assertEquals(">", Entities.unescapePattern.matcher("&gt;").replaceAll(">"));
        assertEquals("&", Entities.unescapePattern.matcher("&amp;").replaceAll("&"));
        assertEquals("'", Entities.unescapePattern.matcher("&apos;").replaceAll("'"));
        assertEquals("\"", Entities.unescapePattern.matcher("&quot;").replaceAll("\""));
        assertEquals("&#60;", Entities.unescapePattern.matcher("&#60;").replaceAll("&#60;"));
        assertEquals("&lt", Entities.unescapePattern.matcher("&lt").replaceAll("&lt"));
        assertEquals("&&", Entities.unescapePattern.matcher("&&").replaceAll("&&"));
        
        assertEquals("&#x1E05;", Entities.unescapePattern.matcher("&#x1E05;").replaceAll("&#1E05;"));
    }
    
    @Test
    public void testLoadEntities() {
        assertNotNull(Entities.loadEntities("entities-base.properties"));
        assertNotNull(Entities.loadEntities("entities-full.properties"));
    }
    
    @Test
    public void testToCharacterKey() {
        Entities entities = new Entities();
        assertNotNull(entities.toCharacterKey(entities.loadEntities("entities-base.properties")));
    }
    
}
