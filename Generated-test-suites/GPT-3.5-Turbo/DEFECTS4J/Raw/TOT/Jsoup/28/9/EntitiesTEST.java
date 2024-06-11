
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    // Test case for isNamedEntity() method
    @Test
    public void testIsNamedEntity() {
        // Typical use case: Test with a known named entity
        assertTrue(Entities.isNamedEntity("lt"));

        // Edge case: Test with a non-existent named entity
        assertFalse(Entities.isNamedEntity("nonexistent"));

        // Error scenario: Test with null input
        assertFalse(Entities.isNamedEntity(null));
    }

    // Test case for getCharacterByName() method
    @Test
    public void testGetCharacterByName() {
        // Typical use case: Test with a known named entity
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));

        // Edge case: Test with a non-existent named entity
        assertNull(Entities.getCharacterByName("nonexistent"));

        // Error scenario: Test with null input
        assertNull(Entities.getCharacterByName(null));
    }

    // Test case for escape() method
    @Test
    public void testEscape() {
        // Typical use case: Test with special characters
        String input = "<Test> & " + '"' + "'";
        String expectedOutput = "&lt;Test&gt; &amp; &quot;&apos;";
        assertEquals(expectedOutput, Entities.escape(input, Document.OutputSettings.html()));

        // Edge case: Test with empty string
        assertEquals("", Entities.escape("", Document.OutputSettings.html()));

        // Error scenario: Test with null input
        assertNull(Entities.escape(null, Document.OutputSettings.html()));
    }

    // Test case for unescape() method
    @Test
    public void testUnescape() {
        // Typical use case: Test with escaped characters
        String input = "&lt;Test&gt; &amp; &quot;&apos;";
        String expectedOutput = "<Test> & " + '"' + "'";
        assertEquals(expectedOutput, Entities.unescape(input));

        // Edge case: Test with empty string
        assertEquals("", Entities.unescape(""));

        // Error scenario: Test with null input
        assertNull(Entities.unescape(null));
    }

    // Additional test case for unescape() method with strict mode
    @Test
    public void testUnescapeStrict() {
        // Typical use case: Test with correctly escaped characters
        String input = "&lt;Test&gt; &amp; &quot;&apos;";
        String expectedOutput = "<Test> & " + '"' + "'";
        assertEquals(expectedOutput, Entities.unescape(input, true));

        // Edge case: Test with empty string
        assertEquals("", Entities.unescape("", true));

        // Error scenario: Test with missing trailing ';' for named entity
        assertEquals("&amp", Entities.unescape("&amp", true));

        // Error scenario: Test with invalid decimal entity value
        assertEquals("&#1000i;", Entities.unescape("&#1000i;", true));
    }
}

