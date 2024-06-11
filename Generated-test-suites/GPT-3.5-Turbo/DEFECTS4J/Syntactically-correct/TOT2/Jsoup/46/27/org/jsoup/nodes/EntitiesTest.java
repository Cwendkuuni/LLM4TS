import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Typical use case
        assertTrue(Entities.isNamedEntity("lt"));
        
        // Edge cases
        assertFalse(Entities.isNamedEntity("nonexistent"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Typical use case
        assertTrue(Entities.isBaseNamedEntity("gt"));
        
        // Edge cases
        assertFalse(Entities.isBaseNamedEntity("nonexistent"));
    }

    @Test
    public void testGetCharacterByName() {
        // Typical use case
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        
        // Edge cases
        assertNull(Entities.getCharacterByName("nonexistent"));
    }

    @Test
    public void testEscape() {
        // Typical use case
        assertEquals("&lt;&gt;&amp;&quot;", Entities.escape("<>&\""));
        
        // Edge cases
        assertEquals("", Entities.escape("", null));
    }

    @Test
    public void testUnescape() {
        // Typical use case
        assertEquals("<>&\"'", Entities.unescape("&lt;&gt;&amp;&quot;&apos;"));
        
        // Edge cases
        assertEquals("", Entities.unescape(""));
    }

}