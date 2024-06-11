import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Before
    public void setUp() {
        // Initialize any necessary variables or objects before each test
    }
    
    @Test
    public void testIsNamedEntity() {
        // Test cases for isNamedEntity method
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        
        assertFalse(Entities.isNamedEntity("xyz"));
        assertFalse(Entities.isNamedEntity("123"));
        assertFalse(Entities.isNamedEntity("&lt"));
        assertFalse(Entities.isNamedEntity("&gt"));
        assertFalse(Entities.isNamedEntity("&amp"));
        assertFalse(Entities.isNamedEntity("&apos"));
        assertFalse(Entities.isNamedEntity("&quot"));
    }
    
    @Test
    public void testGetCharacterByName() {
        // Test cases for getCharacterByName method
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        
        assertEquals(null, Entities.getCharacterByName("xyz"));
        assertEquals(null, Entities.getCharacterByName("123"));
    }
    
    @Test
    public void testEscape() {
        // Test cases for escape method
        String input1 = "<tag>";
        String expectedOutput1 = "&lt;tag&gt;";
        assertEquals(expectedOutput1, Entities.escape(input1, Document.OutputSettings.html()));
        
        String input2 = "Test &amp;";
        String expectedOutput2 = "Test &amp;amp;";
        assertEquals(expectedOutput2, Entities.escape(input2, Document.OutputSettings.html()));
        
        String input3 = "\"Quotes\"";
        String expectedOutput3 = "&quot;Quotes&quot;";
        assertEquals(expectedOutput3, Entities.escape(input3, Document.OutputSettings.html()));
    }
    
    @Test
    public void testUnescape() {
        // Test cases for unescape method
        String input1 = "&lt;tag&gt;";
        String expectedOutput1 = "<tag>";
        assertEquals(expectedOutput1, Entities.unescape(input1));
        
        String input2 = "Test &amp;amp;";
        String expectedOutput2 = "Test &amp;";
        assertEquals(expectedOutput2, Entities.unescape(input2));
        
        String input3 = "&quot;Quotes&quot;";
        String expectedOutput3 = "\"Quotes\"";
        assertEquals(expectedOutput3, Entities.unescape(input3));
    }
}