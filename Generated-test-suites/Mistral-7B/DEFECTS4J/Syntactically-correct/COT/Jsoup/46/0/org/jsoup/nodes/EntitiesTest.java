import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        Map<String, Character> full = entities.getFullMap();
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            Assert.assertTrue(entities.isNamedEntity(name));
            Assert.assertEquals(character, entities.getCharacterByName(name));
        }

        // edge case: empty string
        Assert.assertFalse(entities.isNamedEntity(""));

        // exception handling: null name
        try {
            entities.isNamedEntity(null);
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        Map<Character, String> base = entities.getBaseMap();
        for (Map.Entry<Character, String> entry : base.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue();
            Assert.assertTrue(entities.isBaseNamedEntity(name));
            Assert.assertEquals(character, entities.getCharacterByName(name));
            Assert.assertEquals(character, entities.getCharacterByName(name.toUpperCase()));
        }

        // edge case: empty string
        Assert.assertFalse(entities.isBaseNamedEntity(""));

        // edge case: upper case named entity
        Assert.assertTrue(entities.isBaseNamedEntity("AMP"));

        // exception handling: null name
        try {
            entities.isBaseNamedEntity(null);
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        String input = "&lt;<&amp;&gt;\"'";
        String expectedOutput = "&amp;lt;&amp;lt;&amp;amp;&amp;gt;&amp;quot;&amp;apos;";
        StringBuilder actualOutput = new StringBuilder();
        entities.escape(actualOutput, input, null, false, false, false);
        Assert.assertEquals(expectedOutput, actualOutput.toString());

        // edge case: empty string
        StringBuilder emptyOutput = new StringBuilder();
        entities.escape(emptyOutput, "", null, false, false, false);
        Assert.assertEquals("", emptyOutput.toString());

        // edge case: null string
        StringBuilder nullOutput = new StringBuilder();
        entities.escape(nullOutput, null, null, false, false, false);
        Assert.assertEquals("", nullOutput.toString());

        // exception handling: null StringBuilder
        try {
            entities.escape(null, null, null, false, false, false);
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String input = "&amp;lt;&amp;lt;&amp;amp;&amp;gt;&amp;quot;&amp;apos;";
        String expectedOutput = "&lt;&lt;&amp;&gt;&quot;&apos;";
        String actualOutput = entities.unescape(input, false);
        Assert.assertEquals(expectedOutput, actualOutput);

        // edge case: empty string
        String emptyOutput = entities.unescape("", false);
        Assert.assertEquals("", emptyOutput);

        // edge case: null string
        String nullOutput = entities.unescape(null, false);
        Assert.assertEquals("", nullOutput);

        // edge case: strict unescape
        String strictInput = "&amp;lt;&amp;amp;&lt;";
        String strictExpectedOutput = "<";
        String strictActualOutput = entities.unescape(strictInput, true);
        Assert.assertEquals(strictExpectedOutput, strictActualOutput);

        // exception handling: null string for unescape
        try {
            entities.unescape(null, false);
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();
        CoreCharset[] coreCharsets = CoreCharset.values();
        for (CoreCharset coreCharset : coreCharsets) {
            for (char c = (char) 0; c <= 0x10FFFF; c++) {
                if (Character.isSupplementaryCodePoint(c)) {
                    continue; // surrogate pairs not supported
                }
                CharsetEncoder encoder = Entities.class.getResourceAsStream("/resources/encoders/" + coreCharset.name() + ".class")
                        .getFields()[0].get(null);
                boolean canEncode = entities.canEncode(coreCharset, c, encoder);
                Assert.assertEquals(Character.isDefined(c), canEncode);
            }
        }
    }
}