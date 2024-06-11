
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test 1: Check if a known named entity returns true
        assertTrue(Entities.isNamedEntity("lt"));
        
        // Test 2: Check if an unknown named entity returns false
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test 1: Check if a known named entity in the base set returns true
        assertTrue(Entities.isBaseNamedEntity("lt"));
        
        // Test 2: Check if an unknown named entity in the base set returns false
        assertFalse(Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        // Test 1: Check if a known named entity returns the correct character
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        
        // Test 2: Check if an unknown named entity returns null
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        // Test 1: Check if escaping a string works correctly
        String escaped = Entities.escape("Hello < World");
        assertEquals("Hello &lt; World", escaped);
    }

    @Test
    public void testUnescape() {
        // Test 1: Check if unescaping a string works correctly
        String unescaped = Entities.unescape("Hello &lt; World");
        assertEquals("Hello < World", unescaped);
    }
    
    // Additional tests for edge cases and exception handling scenarios
    
    @Test
    public void testEscapeStripLeadingWhite() {
        // Test 1: Check if stripping leading whitespace works correctly
        String escaped = Entities.escape("    Hello");
        assertEquals(" Hello", escaped);
    }
    
    // Add more edge case and exception handling tests as needed
    
}