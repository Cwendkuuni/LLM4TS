
package org.jsoup.nodes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("xyz"));
    }
    
    @Test
    public void testEscape() {
        assertEquals("&lt;&gt;&amp;&apos;&quot;", Entities.escape("<>&'\""));
    }
    
    @Test
    public void testUnescape() {
        assertEquals("<>&'\"", Entities.unescape("&lt;&gt;&amp;&apos;&quot;"));
    }
    
    @Test
    public void testUnescapeStrict() {
        assertEquals("<>&\"", Entities.unescape("&lt;&gt;&amp;'&quot;", true));
    }
    
    @Test
    public void testUnescapeNonStrict() {
        assertEquals("<>&'\"", Entities.unescape("&lt;&gt;&amp;'&quot;", false));
    }
    
    @Test
    public void testLoadEntities() {
        assertEquals(Character.valueOf('<'), Entities.loadEntities("entities-base.properties").get("lt"));
        assertEquals(Character.valueOf('&'), Entities.loadEntities("entities-base.properties").get("amp"));
        assertEquals(Character.valueOf('<'), Entities.loadEntities("entities-full.properties").get("lt"));
        assertEquals(Character.valueOf('&'), Entities.loadEntities("entities-full.properties").get("amp"));
    }
    
    @Test
    public void testToCharacterKey() {
        assertEquals("lt", Entities.toCharacterKey(Entities.loadEntities("entities-base.properties")).get('<'));
        assertEquals("amp", Entities.toCharacterKey(Entities.loadEntities("entities-base.properties")).get('&'));
        assertNull(Entities.toCharacterKey(Entities.loadEntities("entities-full.properties")).get('<'));
        assertNull(Entities.toCharacterKey(Entities.loadEntities("entities-full.properties")).get('&'));
    }
    
    // Add more specific test cases for edge cases and exception handling scenarios
    
}

