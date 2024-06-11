import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Basic test case
        assertTrue(Entities.isNamedEntity("lt"));
        
        // Edge case: null input
        assertFalse(Entities.isNamedEntity(null));
        
        // Edge case: empty string input
        assertFalse(Entities.isNamedEntity(""));
        
        // Error case: invalid entity name
        assertFalse(Entities.isNamedEntity("invalidEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Basic test case
        assertTrue(Entities.isBaseNamedEntity("amp"));
        
        // Edge case: null input
        assertFalse(Entities.isBaseNamedEntity(null));
        
        // Edge case: empty string input
        assertFalse(Entities.isBaseNamedEntity(""));
        
        // Error case: invalid entity name
        assertFalse(Entities.isBaseNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        // Basic test case
        assertEquals('<', Entities.getCharacterByName("lt"));
        
        // Edge case: null input
        assertNull(Entities.getCharacterByName(null));
        
        // Edge case: invalid entity name
        assertNull(Entities.getCharacterByName("invalidEntity"));
    }
    
    // Add remaining test cases here...
}