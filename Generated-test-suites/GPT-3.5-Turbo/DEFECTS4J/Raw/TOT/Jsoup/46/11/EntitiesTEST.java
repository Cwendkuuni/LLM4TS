package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        
        assertFalse(Entities.isNamedEntity("abc"));
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        
        assertFalse(Entities.isBaseNamedEntity("nbsp"));
        assertFalse(Entities.isBaseNamedEntity("ldquo"));
        assertFalse(Entities.isBaseNamedEntity("rdquo"));
    }

    @Test
    public void testGetCharacterByName() {
        Character expected1 = '&#x0003C;';
        Character expected2 = '&#x0003E;';
        
        assertEquals(expected1, Entities.getCharacterByName("lt"));
        assertEquals(expected2, Entities.getCharacterByName("gt"));
        
        assertNull(Entities.getCharacterByName("abc"));
        assertNull(Entities.getCharacterByName(""));
    }

    @Test
    public void testEscape() {
        String expected = "&lt;&nbsp;&gt;&amp;&quot;";
        
        String result = Entities.escape("< > & \"", new Document.OutputSettings(Entities.EscapeMode.xhtml));
        
        assertEquals(expected, result);
    }

    @Test
    public void testUnescape() {
        String expected = "< > & \"";
        
        String result = Entities.unescape("&lt; &gt; &amp; &quot;");
        
        assertEquals(expected, result);
    }

    @Test
    public void testUnescapeStrict() {
        String expected = "< > & \"";
        
        String result = Entities.unescape("&lt; &gt; &amp; &quot;", true);
        
        assertEquals(expected, result);
    }
}