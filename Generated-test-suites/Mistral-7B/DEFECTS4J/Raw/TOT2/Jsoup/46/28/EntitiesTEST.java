import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test basic functionality
        assertTrue(Entities.isNamedEntity("lt"));
        
        // Test edge cases and exception handling
        assertFalse(Entities.isNamedEntity("lt_invalid")); // Invalid named entity
        assertFalse(Entities.isNamedEntity("")); // Empty string
        assertFalse(Entities.isNamedEntity(null)); // Null input
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test basic functionality
        assertTrue(Entities.isBaseNamedEntity("amp"));
        
        // Test edge cases and exception handling
        assertFalse(Entities.isBaseNamedEntity("amp_invalid")); // Invalid named entity
        assertFalse(Entities.isBaseNamedEntity("")); // Empty string
        assertFalse(Entities.isBaseNamedEntity(null)); // Null input
    }

    @Test
    public void testGetCharacterByName() {
        // Test basic functionality
        assertEquals('<', Entities.getCharacterByName("lt"));
        
        // Test edge cases and exception handling
        assertNull(Entities.getCharacterByName("invalid_entity")); // Non-existent named entity
        assertNull(Entities.getCharacterByName(null)); // Null input
    }

    // Test other methods such as escape, unescape, etc., following the same pattern
}