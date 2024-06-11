import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        // Basic test
        assertTrue(Entities.isNamedEntity("lt"));
        // Edge case: empty string
        assertFalse(Entities.isNamedEntity(""));
        // Edge case: null input
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Basic test
        assertTrue(Entities.isBaseNamedEntity("lt"));
        // Edge case: empty string
        assertFalse(Entities.isBaseNamedEntity(""));
        // Edge case: null input
        assertFalse(Entities.isBaseNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        // Basic test
        assertEquals('<', Entities.getCharacterByName("lt"));
        // Edge case: unknown entity
        assertNull(Entities.getCharacterByName("unknown"));
        // Edge case: null input
        assertNull(Entities.getCharacterByName(null));
    }

    // Add other test methods for remaining methods here...
}