package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<Character, String> fullEntities = Entities.full;
        for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue().toString();
            assertTrue("Named entity '" + name + "' not detected", Entities.isNamedEntity(name));
            assertTrue("Named entity '" + character + "' not detected", Entities.isNamedEntity(character.toString()));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> baseEntities = Entities.base;
        for (Map.Entry<Character, String> entry : baseEntities.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue().toString();
            assertTrue("Base named entity '" + name + "' not detected", Entities.isBaseNamedEntity(name));
            assertTrue("Base named entity '" + character + "' not detected", Entities.isBaseNamedEntity(character.toString()));
        }

        // Test non-base named entities
        assertFalse("Non-base named entity 'lt' is base named entity", Entities.isBaseNamedEntity("lt"));
        assertFalse("Non-base named entity 'amp' is base named entity", Entities.isBaseNamedEntity("amp"));
        assertFalse("Non-base named entity 'quot' is base named entity", Entities.isBaseNamedEntity("quot"));
        assertFalse("Non-base named entity 'gt' is base named entity", Entities.isBaseNamedEntity("gt"));
        assertFalse("Non-base named entity 'nbsp' is base named entity", Entities.isBaseNamedEntity("nbsp"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, String> fullEntities = Entities.full;
        for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue().toString();
            assertEquals("Named entity '" + name + "' does not return correct character", character, Entities.getCharacterByName(name));
            assertEquals("Named entity '" + character + "' does not return correct name", name, Entities.getCharacterByName(character.toString()));
        }
    }

    @Test
    public void testEscape() {
        String input = "Hello & World! C<e4>hào & < and & > & 'singlequote & & &amp; & quot & \"doublequote & &gt; & &lt;";
        String expectedOutput = "Hello &amp;amp; World! C&amp;#xE4;h&amp;#x41;a &amp;amp; &amp;amp;lt; &amp;amp;amp; &amp;quot; &amp;#x22; &amp;gt;";

        StringBuilder output = new StringBuilder();
        Entities.escape(output, input, null, false, false, false);

        assertEquals("Escaped string does not match expected output", expectedOutput, output.toString());
    }

    @Test
    public void testUnescape() {
        String input = "Hello &amp;amp; World! C&amp;#xE4;h&amp;#x41;a &amp;amp;lt; &amp;amp;amp; &amp;quot; &amp;#x22; &amp;gt;";
        String expectedOutput = "Hello & World! CẤo & < and & > & 'singlequote & &amp; & quot & \"doublequote & > <";

        StringBuilder output = new StringBuilder();
        Entities.unescape(input, false);
        output.append(input);

        assertEquals("Unescaped string does not match expected output", expectedOutput, output.toString());
    }

    // Add more tests for other methods in the Entities class as needed

    private static void assertTrue(String message, boolean condition) {
        if (!condition) {
            fail(message);
        }
    }

    private static void assertFalse(String message, boolean condition) {
        if (condition) {
            fail(message);
        }
    }
}