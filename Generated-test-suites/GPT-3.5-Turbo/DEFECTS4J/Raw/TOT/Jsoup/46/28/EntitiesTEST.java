import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class EntitiesTest {
    
    @Before
    public void setUp() {
        // Add any necessary setup code here
    }
    
    @Test
    public void testIsNamedEntity() {
        // Test for a known named entity
        assertTrue(Entities.isNamedEntity("lt"));
        
        // Test for an unknown named entity
        assertFalse(Entities.isNamedEntity("unknown"));
        
        // Test for a null input
        assertFalse(Entities.isNamedEntity(null));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        // Test for a known named entity in the base set
        assertTrue(Entities.isBaseNamedEntity("lt"));
        
        // Test for a known named entity not in the base set
        assertFalse(Entities.isBaseNamedEntity("copy"));
        
        // Test for an unknown named entity
        assertFalse(Entities.isBaseNamedEntity("unknown"));
        
        // Test for a null input
        assertFalse(Entities.isBaseNamedEntity(null));
    }
    
    @Test
    public void testGetCharacterByName() {
        // Test for a known named entity
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        
        // Test for an unknown named entity
        assertNull(Entities.getCharacterByName("unknown"));
        
        // Test for a null input
        assertNull(Entities.getCharacterByName(null));
    }
    
    @Test
    public void testEscape() {
        // Test for escaping special characters in a string
        String input = "This is <test> & string \"to\" escape";
        String expected = "This is &lt;test&gt; &amp; string &quot;to&quot; escape";
        assertEquals(expected, Entities.escape(input, new Document.OutputSettings()));
        
        // Test for an empty input string
        assertEquals("", Entities.escape("", new Document.OutputSettings()));
        
        // Test for a null input string
        assertNull(Entities.escape(null, new Document.OutputSettings()));
    }
    
    @Test
    public void testUnescape() {
        // Test for unescaping escaped special characters in a string
        String input = "This is &lt;test&gt; &amp; string &quot;to&quot; unescape";
        String expected = "This is <test> & string \"to\" unescape";
        assertEquals(expected, Entities.unescape(input));
        
        // Test for an empty input string
        assertEquals("", Entities.unescape(""));
        
        // Test for a null input string
        assertNull(Entities.unescape(null));
    }
    
    @Test
    public void testUnescapeStrict() {
        // Test for unescaping escaped special characters in a string with strict mode
        String input = "This is &lt;test&gt; &amp; string &quot;to&quot; unescape;";
        String expected = "This is <test> & string \"to\" unescape";
        assertEquals(expected, Entities.unescape(input, true));
    }

}