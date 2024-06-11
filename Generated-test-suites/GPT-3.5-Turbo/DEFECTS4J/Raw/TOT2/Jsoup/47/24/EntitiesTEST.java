import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        String input = "This is a <div> tag";
        String expectedOutput = "This is a &lt;div&gt; tag";
        assertEquals(expectedOutput, Entities.escape(input, null)); // Passing null to mimic Document.OutputSettings
    }

    @Test
    public void testUnescape() {
        String input = "This is a &lt;div&gt; tag";
        String expectedOutput = "This is a <div> tag";
        assertEquals(expectedOutput, Entities.unescape(input));
    }

    // Additional test cases for edge cases and exception handling should be added here

}