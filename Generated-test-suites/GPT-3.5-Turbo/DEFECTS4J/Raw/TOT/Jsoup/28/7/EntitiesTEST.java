import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt")); // typical case - entity is known
        assertFalse(Entities.isNamedEntity("unknown")); // edge case - entity is unknown
        assertFalse(Entities.isNamedEntity(null)); // error scenario - null value
    }

    @Test
    public void testGetCharacterByName() {
        Character expected1 = Character.valueOf((char) 0x00022);
        Character expected2 = Character.valueOf((char) 0x0003C);
        
        assertEquals(expected1, Entities.getCharacterByName("quot")); // typical case - known entity
        assertEquals(expected2, Entities.getCharacterByName("lt")); // typical case - known entity
        assertNull(Entities.getCharacterByName("unknown")); // edge case - unknown entity
        assertNull(Entities.getCharacterByName(null)); // error scenario - null value
    }

    @Test
    public void testEscape() {
        String input1 = "<div>";
        String input2 = "This & That";
        String input3 = "Testing ";
        
        String expected1 = "&lt;div&gt;"; // typical case - escape characters
        String expected2 = "This &amp; That"; // typical case - escape characters
        String expected3 = "Testing "; // typical case - no escape characters
        
        assertEquals(expected1, Entities.escape(input1, null, Entities.EscapeMode.xhtml)); 
        assertEquals(expected2, Entities.escape(input2, null, Entities.EscapeMode.xhtml)); 
        assertEquals(expected3, Entities.escape(input3, null, Entities.EscapeMode.xhtml)); 
    }

    @Test
    public void testUnescape() {
        String input1 = "&lt;div&gt;";
        String input2 = "This &amp; That";
        String input3 = "Testing ";
        
        String expected1 = "<div>"; // typical case - unescape characters
        String expected2 = "This & That"; // typical case - unescape characters
        String expected3 = "Testing "; // typical case - no escape characters

        assertEquals(expected1, Entities.unescape(input1));
        assertEquals(expected2, Entities.unescape(input2));
        assertEquals(expected3, Entities.unescape(input3));
    }
    
    // Additional test cases for other methods
    
    @Test
    public void testEscape_additional() {
        String input1 = "<test>";
        String input2 = "This & That";
        String input3 = "";
        
        String expected1 = "&lt;test&gt;"; // typical case - escape characters
        String expected2 = "This &amp; That"; // typical case - escape characters
        String expected3 = ""; // typical case - empty string
        
        assertEquals(expected1, Entities.escape(input1, null, Entities.EscapeMode.xhtml)); 
        assertEquals(expected2, Entities.escape(input2, null, Entities.EscapeMode.xhtml)); 
        assertEquals(expected3, Entities.escape(input3, null, Entities.EscapeMode.xhtml)); 
    }

    @Test
    public void testUnescape_additional() {
        String input1 = "&lt;test&gt;";
        String input2 = "This &amp; That";
        String input3 = "";
        
        String expected1 = "<test>"; // typical case - unescape characters
        String expected2 = "This & That"; // typical case - unescape characters
        String expected3 = ""; // typical case - empty string

        assertEquals(expected1, Entities.unescape(input1));
        assertEquals(expected2, Entities.unescape(input2));
        assertEquals(expected3, Entities.unescape(input3));
    }

    // ... additional test cases for the remaining methods

}