import org.jsoup.nodes.Entities;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test case 1: Valid named entity
        assertTrue(Entities.isNamedEntity("lt"));

        // Test case 2: Invalid named entity
        assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void testGetCharacterByName() {
        // Test case 1: Valid named entity
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));

        // Test case 2: Invalid named entity
        assertNull(Entities.getCharacterByName("abc"));
    }

    @Test
    public void testEscape() {
        // Test case 1: String with special characters to be escaped
        String input = "This is <b>bold</b> text.";
        String expectedOutput = "This is &lt;b&gt;bold&lt;/b&gt; text.";
        assertEquals(expectedOutput, Entities.escape(input, null));

        // Test case 2: Empty string
        String emptyInput = "";
        assertEquals(emptyInput, Entities.escape(emptyInput, null));
    }

    @Test
    public void testUnescape() {
        // Test case 1: Unescaping a string with named entities
        String input = "This is &lt;b&gt;bold&lt;/b&gt; text.";
        String expectedOutput = "This is <b>bold</b> text.";
        assertEquals(expectedOutput, Entities.unescape(input));

        // Test case 2: Unescaping a string with numerical entities
        String numericalInput = "This is &#60;b&#62;bold&#60;/b&#62; text.";
        assertEquals(expectedOutput, Entities.unescape(numericalInput));

        // Test case 3: Unescaping a string with invalid entities
        String invalidInput = "This is &invalid; text.";
        assertEquals(invalidInput, Entities.unescape(invalidInput));
    }

    @Test
    public void testEscapeWithEncoderAndEscapeMode() {
        // Test case 1: String with special characters to be escaped
        String input = "This is <b>bold</b> text.";
        String expectedOutput = "This is &lt;b&gt;bold&lt;/b&gt; text.";
        assertEquals(expectedOutput, Entities.escape(input, null, Entities.EscapeMode.xhtml));

        // Test case 2: Empty string
        String emptyInput = "";
        assertEquals(emptyInput, Entities.escape(emptyInput, null, Entities.EscapeMode.xhtml));
    }

    @Test
    public void testUnescapeWithStrictFlag() {
        // Test case 1: Unescaping a string with strict flag
        String input = "This is &lt;b&gt;bold&lt;/b&gt; text.";
        String expectedOutput = "This is <b>bold</b> text.";
        assertEquals(expectedOutput, Entities.unescape(input, true));

        // Test case 2: Unescaping a string without strict flag
        String nonStrictInput = "This is &lt;b&gt;bold&lt;/b&gt; text;";
        assertEquals(expectedOutput, Entities.unescape(nonStrictInput, false));

        // Test case 3: Unescaping a string with invalid entities
        String invalidInput = "This is &invalid; text.";
        assertEquals(invalidInput, Entities.unescape(invalidInput, true));
    }

    @Test
    public void testLoadEntities() {
        // Test case 1: Loading base entities
        HashMap<String, Character> baseEntities = new HashMap<>();
        baseEntities.put("lt", '<');
        baseEntities.put("gt", '>');
        baseEntities.put("amp", '&');
        baseEntities.put("apos", '\'');
        baseEntities.put("quot", '"');
        assertEquals(baseEntities, Entities.loadEntities("entities-base.properties"));

        // Test case 2: Loading full entities
        HashMap<String, Character> fullEntities = new HashMap<>();
        fullEntities.put("lt", '<');
        fullEntities.put("gt", '>');
        fullEntities.put("amp", '&');
        fullEntities.put("apos", '\'');
        fullEntities.put("quot", '"');
        // Add more entities here if necessary
        assertEquals(fullEntities, Entities.loadEntities("entities-full.properties"));
    }
}