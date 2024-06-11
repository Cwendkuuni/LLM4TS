package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt")); // typical use case
        assertFalse(Entities.isNamedEntity("xyz")); // error scenario
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("gt")); // typical use case
        assertFalse(Entities.isBaseNamedEntity("xyz")); // error scenario
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt")); // typical use case
        assertNull(Entities.getCharacterByName("xyz")); // error scenario
    }
    
    @Test
    public void testEscape() {
        String input = "<script> alert('Hello World!') </script>";
        String expectedOutput = "&lt;script&gt; alert(&#x27;Hello World!&#x27;) &lt;/script&gt;";
        assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings()));
    }
    
    @Test
    public void testUnescape() {
        String input = "&lt;script&gt; alert(&#x27;Hello World!&#x27;) &lt;/script&gt;";
        String expectedOutput = "<script> alert('Hello World!') </script>";
        assertEquals(expectedOutput, Entities.unescape(input));
    }
}
