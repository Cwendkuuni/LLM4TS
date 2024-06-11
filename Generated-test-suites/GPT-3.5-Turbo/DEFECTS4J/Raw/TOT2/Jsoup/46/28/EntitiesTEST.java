import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("unknown"));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("unknown"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("unknown"));
    }
    
    @Test
    public void testEscape() {
        String input = "<div>Test</div>";
        String expected = "&lt;div&gt;Test&lt;/div&gt;";
        assertEquals(expected, Entities.escape(input, new Document.OutputSettings()));
    }
    
    @Test
    public void testUnescape() {
        String input = "&lt;div&gt;Test&lt;/div&gt;";
        String expected = "<div>Test</div>";
        assertEquals(expected, Entities.unescape(input));
    }
    
    // Add more test cases for edge cases and error scenarios
    
}