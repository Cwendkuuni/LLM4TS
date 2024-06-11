package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for Entities class.
 */
public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test known named entities
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("gt"));
        // Test unknown named entities
        assertFalse(Entities.isNamedEntity("abc"));
        assertFalse(Entities.isNamedEntity("def"));
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test known base named entities
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        // Test unknown named entities
        assertFalse(Entities.isBaseNamedEntity("abc"));
        assertFalse(Entities.isBaseNamedEntity("def"));
        assertFalse(Entities.isBaseNamedEntity(""));
        assertFalse(Entities.isBaseNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        // Test known named entities
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('>', Entities.getCharacterByName("gt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('"', Entities.getCharacterByName("quot"));
        // Test unknown named entities
        assertNull(Entities.getCharacterByName("abc"));
        assertNull(Entities.getCharacterByName("def"));
        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscape() {
        String input = "&lt;abc&gt;def&quot;";
        StringBuilder expectedOutput = new StringBuilder();
        Entities.escape(expectedOutput, input, null, false, false, false);
        String output = expectedOutput.toString();
        assertEquals("<&lt;abc&gt;&gt;def&quot;", output);

        input = "&amp;lt;abc&gt;def&quot;";
        expectedOutput = new StringBuilder();
        Entities.escape(expectedOutput, input, null, false, false, false);
        output = expectedOutput.toString();
        assertEquals("&amp;lt;abc&gt;&amp;gt;def&amp;quot;", output);
    }

    @Test
    public void testUnescape() {
        String input = "<&lt;abc&gt;&gt;def&quot;>";
        String expectedOutput = "&lt;abc&gt;def&quot;";
        String output = Entities.unescape(input);
        assertEquals(expectedOutput, output);

        input = "&amp;lt;abc&gt;&amp;gt;def&amp;quot;";
        expectedOutput = "&lt;abc&gt;def&quot;";
        output = Entities.unescape(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCanEncode() {
        Map<Character, String> map = new HashMap<Character, String>() {{
            put('a', "US-ASCII");
            put('ß', "UTF-8");
            put('¥', "UTF-8");
            put('\u0345', "UTF-8");
        }};

        for (Map.Entry<Character, String> entry : map.entrySet()) {
            Character character = entry.getKey();
            String charsetName = entry.getValue();
            CharsetEncoder encoder = Character.isDefined(character) ? Character.isDefined(character) : Character.isDefined(character) ? Character.encodingFor(character) : null;
            CoreCharset coreCharset = Entities.CoreCharset.byName(charsetName);
            boolean expectedResult = (coreCharset == Entities.CoreCharset.ascii && character < 0x80) || encoder != null;
            boolean result = Entities.canEncode(Entities.CoreCharset.ascii, character, encoder);
            assertTrue(expectedResult == result);
        }
    }
}