import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt")); // Test typical use case
        assertTrue(Entities.isNamedEntity("amp")); // Test typical use case
        assertTrue(Entities.isNamedEntity("apos")); // Test typical use case
        assertTrue(Entities.isNamedEntity("quot")); // Test typical use case
        assertTrue(Entities.isNamedEntity("gt")); // Test typical use case
        assertFalse(Entities.isNamedEntity("fake")); // Test non-existent entity
        assertFalse(Entities.isNamedEntity("")); // Test empty input
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt")); // Test typical use case
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp")); // Test typical use case
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos")); // Test typical use case
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot")); // Test typical use case
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt")); // Test typical use case
        assertNull(Entities.getCharacterByName("fake")); // Test non-existent entity
        assertNull(Entities.getCharacterByName("")); // Test empty input
    }

    @Test
    public void testEscape() {
        // Test typical use case
        String expected1 = "&lt;&gt;&amp;&apos;&quot;";
        String result1 = Entities.escape("<>&'\"", Document.OutputSettings.xhtml());
        assertEquals(expected1, result1);

        // Test edge case - empty input string
        String expected2 = "";
        String result2 = Entities.escape("", Document.OutputSettings.xhtml());
        assertEquals(expected2, result2);

        // Test edge case - input string with no escape characters
        String expected3 = "abc123";
        String result3 = Entities.escape("abc123", Document.OutputSettings.xhtml());
        assertEquals(expected3, result3);
    }

    @Test
    public void testUnescape() {
        // Test typical use case
        String expected1 = "<>&'\"";
        String result1 = Entities.unescape("&lt;&gt;&amp;&apos;&quot;");
        assertEquals(expected1, result1);

        // Test edge case - empty input string
        String expected2 = "";
        String result2 = Entities.unescape("");
        assertEquals(expected2, result2);

        // Test edge case - input string with no escape characters
        String expected3 = "abc123";
        String result3 = Entities.unescape("abc123");
        assertEquals(expected3, result3);

        // Test error scenario - unescaped ampersand
        String expected4 = "abc&123";
        String result4 = Entities.unescape("abc&123");
        assertEquals(expected4, result4);

        // Test error scenario - hexadecimal character reference
        String expected5 = "€";
        String result5 = Entities.unescape("&#x20ac;");
        assertEquals(expected5, result5);

        // Test error scenario - decimal character reference
        String expected6 = "€";
        String result6 = Entities.unescape("&#8364;");
        assertEquals(expected6, result6);

        // Test error scenario - invalid character reference
        String expected7 = "&amp;invalid;";
        String result7 = Entities.unescape("&invalid;");
        assertEquals(expected7, result7);

        // Test error scenario - missing semicolon in character reference
        String expected8 = "&amp;123";
        String result8 = Entities.unescape("&amp;123");
        assertEquals(expected8, result8);
    }

}