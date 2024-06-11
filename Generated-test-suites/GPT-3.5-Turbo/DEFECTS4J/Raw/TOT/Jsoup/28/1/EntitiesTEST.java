
import org.junit.Test;
import org.jsoup.nodes.Entities;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Typical use case: entity name is present in the map
        assertTrue(Entities.isNamedEntity("lt"));

        // Typical use case: entity name is not present in the map
        assertFalse(Entities.isNamedEntity("xyz"));

        // Edge case: entity name is an empty string
        assertFalse(Entities.isNamedEntity(""));

        // Error scenario: entity name is null
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        // Typical use case: entity name is present in the map
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));

        // Typical use case: entity name is not present in the map
        assertNull(Entities.getCharacterByName("xyz"));

        // Edge case: entity name is an empty string
        assertNull(Entities.getCharacterByName(""));

        // Error scenario: entity name is null
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscape() {
        // Typical use case: string contains special characters that need to be escaped
        String input = "<div>'Hello' & \"World\"";
        String expectedOutput = "&lt;div&gt;&apos;Hello&apos; &amp; &quot;World&quot;";
        assertEquals(expectedOutput, Entities.escape(input, null, Entities.EscapeMode.base));

        // Typical use case: string does not contain any special characters that need to be escaped
        input = "hello";
        expectedOutput = "hello";
        assertEquals(expectedOutput, Entities.escape(input, null, Entities.EscapeMode.base));

        // Edge case: string is empty
        input = "";
        expectedOutput = "";
        assertEquals(expectedOutput, Entities.escape(input, null, Entities.EscapeMode.base));

        // Error scenario: string is null
        assertNull(Entities.escape(null, null, Entities.EscapeMode.base));
    }

    @Test
    public void testUnescape() {
        // Typical use case: string contains escaped characters
        String input = "&lt;div&gt;&apos;Hello&apos; &amp; &quot;World&quot;";
        String expectedOutput = "<div>'Hello' & \"World\"";
        assertEquals(expectedOutput, Entities.unescape(input));

        // Typical use case: string does not contain any escaped characters
        input = "hello";
        expectedOutput = "hello";
        assertEquals(expectedOutput, Entities.unescape(input));

        // Edge case: string is empty
        input = "";
        expectedOutput = "";
        assertEquals(expectedOutput, Entities.unescape(input));

        // Error scenario: string is null
        assertNull(Entities.unescape(null));
    }
}
