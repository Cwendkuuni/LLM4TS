import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("gt")); // edge case: not a named entity
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("quot")); // edge case: not a base named entity
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("gt")); // edge case: not a named entity
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNamedEntity() {
        Entities.getCharacterByName("invalidEntity"); // error scenario: invalid named entity
    }

    
}