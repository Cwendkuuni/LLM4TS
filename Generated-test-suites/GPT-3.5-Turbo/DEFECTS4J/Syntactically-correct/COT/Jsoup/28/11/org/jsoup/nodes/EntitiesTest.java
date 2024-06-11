
import org.jsoup.nodes.Entities;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    // Test for isNamedEntity method
    @Test
    public void testIsNamedEntity() {
        // Test with a known named entity
        assertTrue(Entities.isNamedEntity("lt"));
        
        // Test with an unknown named entity
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    // Test for getCharacterByName method
    @Test
    public void testGetCharacterByName() {
        // Test with a known named entity
        assertEquals('<', Entities.getCharacterByName("lt").charValue());
        
        // Test with an unknown named entity
        assertNull(Entities.getCharacterByName("unknown"));
    }

    // Test for escape method
    @Test
    public void testEscape() {
        // Test with a string that contains special characters
        String input = "<tag>&&name";
        String expectedOutput = "&lt;tag&gt;&amp;&amp;name";
        assertEquals(expectedOutput, Entities.escape(input, Document.OutputSettings.html()));
        
        // Test with a string that contains no special characters
        String noEscapeInput = "plain text";
        assertEquals(noEscapeInput, Entities.escape(noEscapeInput, Document.OutputSettings.html()));
    }

    // Test for unescape method
    @Test
    public void testUnescape() {
        // Test with a string that contains escaped characters
        String input = "&lt;tag&gt;&amp;&amp;name";
        String expectedOutput = "<tag>&&name";
        assertEquals(expectedOutput, Entities.unescape(input));
        
        // Test with a string that contains no escaped characters
        String noUnescapeInput = "plain text";
        assertEquals(noUnescapeInput, Entities.unescape(noUnescapeInput));
    }
    
    // Test for unescape method with strict mode
    @Test
    public void testUnescapeStrict() {
        // Test with a string that contains escaped characters and a missing semicolon
        String input = "&lt;tag&gt;&amp;&amp";
        String expectedOutput = "<tag>&&";
        assertEquals(expectedOutput, Entities.unescape(input, true));
        
        // Test with a string that contains valid escaped characters
        String validInput = "&lt;tag&gt;&amp;&amp;;";
        String expectedValidOutput = "<tag>&&;";
        assertEquals(expectedValidOutput, Entities.unescape(validInput, true));
    }
    
}
