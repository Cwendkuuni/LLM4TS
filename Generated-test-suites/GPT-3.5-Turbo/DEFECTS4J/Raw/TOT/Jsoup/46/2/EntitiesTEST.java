
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertFalse(Entities.isNamedEntity("invalid")); // Testing with an invalid entity name
        assertTrue(Entities.isNamedEntity("lt")); // Testing with a valid entity name
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertFalse(Entities.isBaseNamedEntity("invalid")); // Testing with an invalid entity name
        assertTrue(Entities.isBaseNamedEntity("lt")); // Testing with a valid entity name
    }

    @Test
    public void testGetCharacterByName() {
        assertNull(Entities.getCharacterByName("invalid")); // Testing with an invalid entity name
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt")); // Testing with a valid entity name
    }

    @Test
    public void testEscape() {
        String input = "Hello < World &";
        String expectedOutput = "Hello &lt; World &amp;";
        assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings()));
    }

    @Test
    public void testEscape_inAttribute() {
        String input = "<a href=\"http://example.com\">";
        String expectedOutput = "&lt;a href=&quot;http://example.com&quot;&gt;";
        assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings(), true, false, false));
    }

    @Test
    public void testEscape_normaliseWhite() {
        String input = " Hello   World   ";
        String expectedOutput = " Hello World ";

        // Enable normaliseWhite flag
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.normaliseWhite(true);

        assertEquals(expectedOutput, Entities.escape(input, outputSettings));
    }

    @Test
    public void testEscape_stripLeadingWhite() {
        String input = "  Hello   World   ";
        String expectedOutput = " Hello   World ";

        // Enable normaliseWhite and stripLeadingWhite flags
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.normaliseWhite(true);
        outputSettings.stripLeadingWhite(true);

        assertEquals(expectedOutput, Entities.escape(input, outputSettings));
    }

    @Test
    public void testUnescape() {
        String input = "Hello &lt; World &amp;";
        String expectedOutput = "Hello < World &";
        assertEquals(expectedOutput, Entities.unescape(input));
    }

    @Test
    public void testUnescape_strict() {
        String input = "Hello &lt; World &amp";
        String expectedOutput = "Hello < World &amp";
        assertEquals(expectedOutput, Entities.unescape(input, true));
    }

    // Additional test cases can be added for comprehensive coverage

}

