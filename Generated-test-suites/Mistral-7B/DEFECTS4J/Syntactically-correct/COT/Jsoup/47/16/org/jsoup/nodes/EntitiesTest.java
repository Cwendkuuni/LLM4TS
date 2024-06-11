import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.full;
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String entityName = entry.getKey();
            Character entityValue = entry.getValue();
            assertTrue("Named entity '" + entityName + "' should be recognized", Entities.isNamedEntity(entityName));
            assertEquals("Named entity '" + entityName + "' should map to character '" + entityValue + "'", entityValue, Entities.getCharacterByName(entityName));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> baseEntities = Entities.baseByVal;
        for (Map.Entry<Character, String> entry : baseEntities.entrySet()) {
            Character entity = entry.getKey();
            String entityName = entry.getValue();
            assertTrue("Base entity '" + entityName + "' should be recognized", Entities.isBaseNamedEntity(entityName));
            assertEquals("Base entity '" + entityName + "' should map to character '" + entity + "'", entity, Entities.getCharacterByName(entityName));
        }

        // Test for edge case: entities not present in base set
        assertFalse("Entity 'nonExistent' should not be recognized", Entities.isBaseNamedEntity("nonExistent"));
    }

    @Test
    public void testEscapeMethod() {
        String input = "Hello & World! <> \"";
        Document.OutputSettings out = new Document.OutputSettings();

        String expected = "Hello &amp; World! &lt;&gt; &quot;";
        assertEquals("escape method should produce expected output for given input", expected, Entities.escape(input, out));

        // Test edge cases for input characters:
        // Character codes that are not in ASCII or UTF-8
        String charWithCodePointBeyondUTF8 = "\uD800";
        String charWithCodePointBeyondASCII = "\u0080";

        out.setEncoder(Character.isDefinedChar(charWithCodePointBeyondUTF8.codePointAt(0)) ? Character.unicodeBlock() : StandardCharsets.UTF_8);
        String escapedCharWithCodePointBeyondUTF8 = Entities.escape(charWithCodePointBeyondUTF8, out);
        assertTrue("Escaped character with code point beyond UTF-8 should start with &#x", escapedCharWithCodePointBeyondUTF8.startsWith("&#x"));
        assertEquals("Escaped character with code point beyond UTF-8 should have correct hex code", Integer.toHexString(0xD800).substring(1), escapedCharWithCodePointBeyondUTF8.substring(1, escapedCharWithCodePointBeyondUTF8.indexOf(';')));

        out.setEncoder(Character.isDefinedChar(charWithCodePointBeyondASCII.codePointAt(0)) ? Character.unicodeBlock() : StandardCharsets.UTF_8);
        String escapedCharWithCodePointBeyondASCII = Entities.escape(charWithCodePointBeyondASCII, out);
        assertTrue("Escaped character with code point beyond ASCII should start with &#x", escapedCharWithCodePointBeyondASCII.startsWith("&#x"));
        assertEquals("Escaped character with code point beyond ASCII should have correct hex code", Integer.toHexString(0x0080).substring(1), escapedCharWithCodePointBeyondASCII.substring(1, escapedCharWithCodePointBeyondASCII.indexOf(';')));

        // Edge cases for input strings
        String inputWithEmptyString = "";
        String expectedEmptyString = "";
        assertEquals("escape method should return empty string for empty input", expectedEmptyString, Entities.escape(inputWithEmptyString, out));

        String inputWithLeadingWhiteSpaces = "   Hello & World! <> \"";
        String expectedWithLeadingWhiteSpaces = "   &nbsp;&nbsp;&nbsp;Hello &amp; World! &lt;&gt; &quot;";
        assertEquals("escape method should produce expected output for given input with leading white spaces", expectedWithLeadingWhiteSpaces, Entities.escape(inputWithLeadingWhiteSpaces, out));

        String inputWithTrailingWhiteSpaces = "Hello & World! <> \"   ";
        String expectedWithTrailingWhiteSpaces = "Hello &amp; World! &lt;&gt; &quot;   ";
        assertEquals("escape method should produce expected output for given input with trailing white spaces", expectedWithTrailingWhiteSpaces, Entities.escape(inputWithTrailingWhiteSpaces, out));
    }
}