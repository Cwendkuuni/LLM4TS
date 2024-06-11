import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt")); // Typical use case
        assertFalse(Entities.isNamedEntity("abc")); // Edge case
        assertFalse(Entities.isNamedEntity(null)); // Error scenario
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt").charValue()); // Typical use case
        assertEquals('&', Entities.getCharacterByName("amp").charValue()); // Typical use case
        assertNull(Entities.getCharacterByName("abc")); // Edge case
        assertNull(Entities.getCharacterByName(null)); // Error scenario
    }

    @Test
    public void testEscape() {
        String input = "<div>Example Text</div>";
        String expectedOutput = "&lt;div&gt;Example Text&lt;/div&gt;";

        assertEquals(expectedOutput, Entities.escape(input, Document.OutputSettings.xhtml())); // Typical use case

        String input2 = "<div>Example Text";
        String expectedOutput2 = "&lt;div&gt;Example Text";

        assertEquals(expectedOutput2, Entities.escape(input2, Document.OutputSettings.xhtml())); // Edge case

        String input3 = "";
        String expectedOutput3 = "";

        assertEquals(expectedOutput3, Entities.escape(input3, Document.OutputSettings.xhtml())); // Error scenario
    }

    @Test
    public void testUnescape() {
        String input = "&lt;div&gt;Example Text&lt;/div&gt;";
        String expectedOutput = "<div>Example Text</div>";

        assertEquals(expectedOutput, Entities.unescape(input)); // Typical use case

        String input2 = "&lt;div&gt;Example Text";
        String expectedOutput2 = "<div>Example Text";

        assertEquals(expectedOutput2, Entities.unescape(input2)); // Edge case

        String input3 = "";
        String expectedOutput3 = "";

        assertEquals(expectedOutput3, Entities.unescape(input3)); // Error scenario
    }

    // Additional test cases for other methods can be added here

}