import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        boolean result = Entities.isNamedEntity("lt");
        assertTrue(result);
        
        result = Entities.isNamedEntity("gt");
        assertTrue(result);
        
        result = Entities.isNamedEntity("amp");
        assertTrue(result);
        
        result = Entities.isNamedEntity("quot");
        assertTrue(result);
        
        result = Entities.isNamedEntity("some_unknown_entity");
        assertFalse(result);
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        boolean result = Entities.isBaseNamedEntity("lt");
        assertFalse(result);
        
        result = Entities.isBaseNamedEntity("gt");
        assertFalse(result);
        
        result = Entities.isBaseNamedEntity("amp");
        assertFalse(result);
        
        result = Entities.isBaseNamedEntity("quot");
        assertFalse(result);
        
        result = Entities.isBaseNamedEntity("some_unknown_entity");
        assertFalse(result);
    }
    
    @Test
    public void testGetCharacterByName() {
        Character result = Entities.getCharacterByName("lt");
        assertEquals('<', (char) result);
        
        result = Entities.getCharacterByName("gt");
        assertEquals('>', (char) result);
        
        result = Entities.getCharacterByName("amp");
        assertEquals('&', (char) result);
        
        result = Entities.getCharacterByName("quot");
        assertEquals('"', (char) result);
        
        result = Entities.getCharacterByName("some_unknown_entity");
        assertNull(result);
    }
}