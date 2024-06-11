import org.junit.Test;
import org.junit.Assert.*;
import java.util.*;
import static org.jsoup.nodes.Entities.getCharacterByName;
import static org.jsoup.nodes.Entities.isNamedEntity;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Basic test for isNamedEntity() method
        for (Entities.EscapeMode mode : Entities.EscapeMode.values()) {
            for (Map<Character, String> map : new Map[] {mode.getMap(), Entities.full}) {
                for (Map.Entry<Character, String> entry : map.entrySet()) {
                    Assert.assertTrue("Named entity " + entry.getKey() + " should be known.", isNamedEntity(entry.getKey().toString()));
                }
            }
        }

        // Edge cases and exception handling tests for isNamedEntity() method
        Assert.assertFalse("lt should not be a named entity in base mode.", isNamedEntity("lt"));
        Assert.assertFalse("Unknown entity should not be a named entity.", "xxxxx".isNamedEntity());
    }

    @Test
    public void testGetCharacterByName() {
        // Basic test for getCharacterByName() method
        for (Entities.EscapeMode mode : Entities.EscapeMode.values()) {
            Map<Character, String> map = mode.getMap();
            for (Map.Entry<Character, String> entry : map.entrySet()) {
                Character expected = entry.getKey();
                String name = entry.getValue();
                Character actual = getCharacterByName(name);
                Assert.assertEquals("Named entity " + name + " should return " + expected, expected, actual);
            }
        }

        // Edge cases and exception handling tests for getCharacterByName() method
        Assert.assertNull("GetCharacterByName should return null for unknown entities.", getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        // Basic test for escape() method
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String testString = "This is a string with entities: <b>Hello</b> & World!";
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        Entities.EscapeMode escapeMode = Entities.EscapeMode.base;
        String expectedOutput = "This is a string with entities: &lt;b&gt;Hello&lt;/b&gt; &amp; World!";
        String actualOutput = escape(testString, outputSettings, escapeMode, encoder);
        Assert.assertEquals("escape() should return expected output.", expectedOutput, actualOutput);

        // Edge cases and exception handling tests for escape() method
        testString = "";
        expectedOutput = "";
        assertNull("escape() should return null for empty strings.", actualOutput = escape(testString, outputSettings, escapeMode, encoder));
        testString = "abc";
        expectedOutput = "abc";
        assertSame("escape() should not change unescaped characters.", actualOutput, escape(testString, outputSettings, escapeMode, encoder));
    }

    @Test
    public void testUnescape() {
        // Basic test for unescape() method
        String testString = "This is a string with entities: &lt;b&gt;Hello&lt;/b&gt; &amp; World!";
        String expectedOutput = "This is a string with entities: <b>Hello</b> & World!";
        String actualOutput = unescape(testString);
        Assert.assertEquals("unescape() should return expected output.", expectedOutput, actualOutput);

        // Edge cases and exception handling tests for unescape() method
        testString = "";
        expectedOutput = "";
        assertSame("unescape() should return empty string for empty strings.", actualOutput, unescape(""));
        testString = "abc";
        expectedOutput = "abc";
        assertSame("unescape() should return input string for unescaped characters.", actualOutput, unescape("abc"));
        testString = "&lt; &gt; &amp; &quot; &apos; &amp;#128; &amp;#x1F602; &amp;#N{113};";
        expectedOutput = "< > & \" ' ⌨";
        assertSame("unescape() should correctly decode simple entities.", actualOutput, unescape(testString));
        testString = "&lt;script&gt;alert('XSS');&lt;/script&gt;";
        expectedOutput = "<script>alert('XSS');</script>";
        assertSame("unescape() should correctly decode XSS entities.", actualOutput, unescape(testString, true));
    }

    // Add more test methods as needed for other methods in the Entities class.

    private static String escape(String string, Document.OutputSettings out, EscapeMode escapeMode, CharsetEncoder encoder) {
        return Entities.escape(string, out, escapeMode.getMap());
    }

    private static String unescape(String string, boolean strict) {
        return Entities.unescape(string, strict);
    }
}