import org.jsoup.nodes.Entities;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    // Test case for isNamedEntity() method
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt")); // typical use case
        assertTrue(Entities.isNamedEntity("amp")); // typical use case
        assertFalse(Entities.isNamedEntity("xyz")); // edge case - entity that doesn't exist
    }

    // Test case for isBaseNamedEntity() method
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt")); // typical use case
        assertTrue(Entities.isBaseNamedEntity("amp")); // typical use case
        assertFalse(Entities.isBaseNamedEntity("xyz")); // edge case - entity that doesn't exist in the base set
    }

    // Test case for getCharacterByName() method
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt")); // typical use case
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp")); // typical use case
        assertNull(Entities.getCharacterByName("xyz")); // edge case - entity that doesn't exist
    }

    // Test case for escape() method
    @Test
    public void testEscape() {
        String input = "Hello < World";
        String expectedOutput = "Hello &lt; World";
        String actualOutput = Entities.escape(input, new Document.OutputSettings());
        assertEquals(expectedOutput, actualOutput); // typical use case
        
        input = "Hello & World";
        expectedOutput = "Hello &amp; World";
        actualOutput = Entities.escape(input, new Document.OutputSettings());
        assertEquals(expectedOutput, actualOutput); // typical use case
        
        input = "Hello > World";
        expectedOutput = "Hello &gt; World";
        actualOutput = Entities.escape(input, new Document.OutputSettings());
        assertEquals(expectedOutput, actualOutput); // typical use case
        
        input = "Hello \" World";
        expectedOutput = "Hello &quot; World";
        actualOutput = Entities.escape(input, new Document.OutputSettings());
        assertEquals(expectedOutput, actualOutput); // typical use case
    }

    // Test case for unescape() method
    @Test
    public void testUnescape() {
        String input = "Hello &lt; World";
        String expectedOutput = "Hello < World";
        String actualOutput = Entities.unescape(input);
        assertEquals(expectedOutput, actualOutput); // typical use case
        
        input = "Hello &amp; World";
        expectedOutput = "Hello & World";
        actualOutput = Entities.unescape(input);
        assertEquals(expectedOutput, actualOutput); // typical use case
        
        input = "Hello &gt; World";
        expectedOutput = "Hello > World";
        actualOutput = Entities.unescape(input);
        assertEquals(expectedOutput, actualOutput); // typical use case
        
        input = "Hello &quot; World";
        expectedOutput = "Hello \" World";
        actualOutput = Entities.unescape(input);
        assertEquals(expectedOutput, actualOutput); // typical use case
    }

}