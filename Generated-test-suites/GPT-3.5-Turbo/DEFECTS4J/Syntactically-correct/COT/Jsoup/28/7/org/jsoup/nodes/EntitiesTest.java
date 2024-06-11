import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EntitiesTest {

    // Test for isNamedEntity(String name) method
    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertFalse(Entities.isNamedEntity("xyz"));
    }

    // Test for getCharacterByName(String name) method
    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        Assert.assertNull(Entities.getCharacterByName("xyz"));
    }

    // Test for escape(String string, Document.OutputSettings out) method
    @Test
    public void testEscape() {
        String input = "<p>example</p>";
        String expected = "&lt;p&gt;example&lt;/p&gt;";
        String result = Entities.escape(input, new Document.OutputSettings());
        Assert.assertEquals(expected, result);
    }

    // Test for escape(String string, CharsetEncoder encoder, EscapeMode escapeMode) method
    @Test
    public void testEscapeWithEncoder() {
        String input = "<p>example</p>";
        String expected = "&lt;p&gt;example&lt;/p&gt;";
        String result = Entities.escape(input, StandardCharsets.UTF_8.newEncoder(), Entities.EscapeMode.xhtml);
        Assert.assertEquals(expected, result);
    }

    // Test for unescape(String string) method
    @Test
    public void testUnescape() {
        String input = "&lt;p&gt;example&lt;/p&gt;";
        String expected = "<p>example</p>";
        String result = Entities.unescape(input);
        Assert.assertEquals(expected, result);
    }

    // Test for unescape(String string, boolean strict) method
    @Test
    public void testUnescapeWithStrict() {
        String input = "&lt;p&gt;example&lt;/p&gt;";
        String expected = "<p>example</p>";
        String result = Entities.unescape(input, true);
        Assert.assertEquals(expected, result);
    }

    // Additional test cases for isNamedEntity(String name) method
    @Test
    public void testIsNamedEntityAdditional() {
        Assert.assertTrue(Entities.isNamedEntity("gt"));
        Assert.assertTrue(Entities.isNamedEntity("quot"));
    }

    // Additional test cases for getCharacterByName(String name) method
    @Test
    public void testGetCharacterByNameAdditional() {
        Assert.assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        Assert.assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
    }

    // Additional test cases for escape(String string, Document.OutputSettings out) method
    @Test
    public void testEscapeAdditional() {
        String input = "Example &amp; Test";
        String expected = "Example &amp;amp; Test";
        String result = Entities.escape(input, new Document.OutputSettings());
        Assert.assertEquals(expected, result);
    }

    // Additional test cases for escape(String string, CharsetEncoder encoder, EscapeMode escapeMode) method
    @Test
    public void testEscapeWithEncoderAdditional() {
        String input = "Example &amp; Test";
        String expected = "Example &amp;amp; Test";
        String result = Entities.escape(input, StandardCharsets.UTF_8.newEncoder(), Entities.EscapeMode.xhtml);
        Assert.assertEquals(expected, result);
    }

    // Additional test cases for unescape(String string) method
    @Test
    public void testUnescapeAdditional() {
        String input = "&amp;lt;p&amp;gt;example&amp;lt;/p&amp;gt;";
        String expected = "&lt;p&gt;example&lt;/p&gt;";
        String result = Entities.unescape(input);
        Assert.assertEquals(expected, result);
    }

    // Additional test cases for unescape(String string, boolean strict) method
    @Test
    public void testUnescapeWithStrictAdditional() {
        String input = "&amp;lt;p&amp;gt;example&amp;lt;/p&amp;gt;";
        String expected = "&lt;p&gt;example&lt;/p&gt;";
        String result = Entities.unescape(input, true);
        Assert.assertEquals(expected, result);
    }

    // Test for loadEntities(String filename) method
    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        List<String> expectedNames = Arrays.asList("lt", "gt", "apos", "amp", "quot");

        Assert.assertEquals(expectedNames.size(), entities.size());
        for (String name : expectedNames) {
            Assert.assertTrue(entities.containsKey(name));
        }
    }

    // Test for toCharacterKey(Map<String, Character> inMap) method
    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = Entities.loadEntities("entities-base.properties");
        Map<Character, String> outMap = Entities.toCharacterKey(inMap);

        Assert.assertEquals(inMap.size(), outMap.size());
        for (Map.Entry<String, Character> entry : inMap.entrySet()) {
            Assert.assertTrue(outMap.containsKey(entry.getValue()));
        }
    }

    // Additional test cases for loadEntities(String filename) method
    @Test(expected = MissingResourceException.class)
    public void testLoadEntitiesInvalidFilename() {
        Entities.loadEntities("non-existing.properties");
    }

    // Additional test cases for toCharacterKey(Map<String, Character> inMap) method
    @Test
    public void testToCharacterKeyAdditional() {
        Map<String, Character> inMap = Entities.loadEntities("entities-base.properties");
        inMap.put("XYZ", 'x');

        Map<Character, String> outMap = Entities.toCharacterKey(inMap);
        Assert.assertFalse(outMap.containsKey('x')); // 'x' will not be present in the outMap
    }

    // Test for EscapeMode enum
    @Test
    public void testEscapeMode() {
        Map<Character, String> xhtmlMap = Entities.EscapeMode.xhtml.getMap();
        Map<Character, String> baseMap = Entities.EscapeMode.base.getMap();
        Map<Character, String> extendedMap = Entities.EscapeMode.extended.getMap();

        List<Character> xhtmlChars = Arrays.asList('\"', '\'', '<', '>', '&');
        List<Character> baseChars = new ArrayList<>(xhtmlChars);
        baseChars.add('`');
        List<Character> extendedChars = new ArrayList<>(baseChars);
        extendedChars.add('¤');

        for (Character c : xhtmlChars) {
            Assert.assertTrue(xhtmlMap.containsKey(c));
            Assert.assertFalse(baseMap.containsKey(c));
            Assert.assertFalse(extendedMap.containsKey(c));
        }

        for (Character c : baseChars) {
            Assert.assertTrue(baseMap.containsKey(c));
            Assert.assertFalse(xhtmlMap.containsKey(c));
            Assert.assertFalse(extendedMap.containsKey(c));
        }

        for (Character c : extendedChars) {
            Assert.assertTrue(extendedMap.containsKey(c));
            Assert.assertFalse(xhtmlMap.containsKey(c));
            Assert.assertFalse(baseMap.containsKey(c));
        }
    }
}