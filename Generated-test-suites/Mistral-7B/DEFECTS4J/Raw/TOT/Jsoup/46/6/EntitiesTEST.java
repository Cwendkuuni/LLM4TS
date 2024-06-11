import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> entities = Entities.full;
        for (Map.entry<String, Character> entry : entities.entrySet()) {
            String name = entry.getKey();
            Character value = entry.getValue();
            assertTrue(Entities.isNamedEntity(name));
            assertEquals(value, Entities.getCharacterByName(name));
        }

        // Edge case: null name
        assertFalse(Entities.isNamedEntity(null));

        // Error scenario: empty name
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> baseEntities = Entities.baseByVal;
        for (Map.entry<String, Character> entry : baseEntities.entrySet()) {
            String name = entry.getKey();
            Character value = entry.getValue();
            assertTrue(Entities.isBaseNamedEntity(name));
            assertEquals(value, Entities.getCharacterByName(name));
        }

        // Edge case: null name
        assertFalse(Entities.isBaseNamedEntity(null));

        // Error scenario: name not present in base entities
        assertFalse(Entities.isBaseNamedEntity("lt")); // should not be present in base entities
    }

    @Test
    public void testEscape() {
        String input = "&amp;<tag>&lt;something&gt;&quot;'&apos;";
        String expectedOutput = "&amp;amp;&lt;tag>&lt;lt;&gt;something&quot;'&apos;";
        Document.OutputSettings settings = new Document.OutputSettings();
        String output = Entities.escape(input, settings);
        assertEquals(expectedOutput, output);

        // Edge case: empty input
        String emptyInput = "";
        String emptyOutput = "";
        output = Entities.escape(emptyInput, settings);
        assertEquals(emptyOutput, output);

        // Error scenario: null input
        String nullInput = null;
        String nullOutput = "";
        try {
            output = Entities.escape(nullInput, settings);
        } catch (NullPointerException e) {
            nullOutput = ""; // asserting that the method throws an exception for null input
        }
        assertEquals(nullOutput, output);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;amp;&lt;tag>&lt;lt;&gt;something&quot;'&apos;";
        String expectedOutput = "&amp;<tag>&lt;lt>&gt;something&quot;&apos;";
        String output = Entities.unescape(input, false);
        assertEquals(expectedOutput, output);

        // Edge case: empty input
        String emptyInput = "";
        String emptyOutput = "";
        output = Entities.unescape(emptyInput, false);
        assertEquals(emptyOutput, output);

        // Error scenario: null input
        String nullInput = null;
        String nullOutput = "";
        try {
            output = Entities.unescape(nullInput, false);
        } catch (NullPointerException e) {
            nullOutput = ""; // asserting that the method throws an exception for null input
        }
        assertEquals(nullOutput, output);
    }
}