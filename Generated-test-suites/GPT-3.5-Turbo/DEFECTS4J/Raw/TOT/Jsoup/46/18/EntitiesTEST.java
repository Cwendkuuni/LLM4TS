import org.jsoup.nodes.Entities;
import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Typical use case
        assertTrue(Entities.isNamedEntity("lt"));
        
        // Edge case
        assertFalse(Entities.isNamedEntity("xyz"));
        
        // Error scenario
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Typical use case
        assertTrue(Entities.isBaseNamedEntity("amp"));
        
        // Edge case
        assertFalse(Entities.isBaseNamedEntity("xyz"));
        
        // Error scenario
        assertFalse(Entities.isBaseNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        // Typical use case
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        
        // Edge case
        assertNull(Entities.getCharacterByName("xyz"));
        
        // Error scenario
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscape() {
        // Typical use case
        assertEquals("&lt;&amp;&gt;", Entities.escape("<&>"));
        
        // Edge case
        assertEquals("", Entities.escape("", null));
        
        // Error scenario
        assertEquals("", Entities.escape(null, null));
    }

    @Test
    public void testUnescape() {
        // Typical use case
        assertEquals("<&>", Entities.unescape("&lt;&amp;&gt;"));
        
        // Edge case
        assertEquals("", Entities.unescape("", false));
        
        // Error scenario
        assertEquals("", Entities.unescape(null, false));
    }

}