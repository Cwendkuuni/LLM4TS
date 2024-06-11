import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("test"));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("test"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("test"));
    }
    
    @Test
    public void testEscape() {
        String string = "Test String";
        String expected = "Test String";
        assertEquals(expected, Entities.escape(string, null));
    }
    
    @Test
    public void testUnescape() {
        String string = "Test String";
        String expected = "Test String";
        assertEquals(expected, Entities.unescape(string));
    }
}