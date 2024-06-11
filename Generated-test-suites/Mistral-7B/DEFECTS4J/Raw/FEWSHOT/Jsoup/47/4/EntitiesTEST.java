package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> entities = Entities.full;
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals("The named entity '" + name + "' should be recognized.", character, Entities.getCharacterByName(name));
            assertTrue("The named entity '" + name + "' should be present.", Entities.isNamedEntity(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> entities = Entities.base;
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("The base named entity '" + name + "' should be present.", Entities.isBaseNamedEntity(name));
            assertEquals("The base named entity '" + name + "' should map to the same character.", character, Entities.getCharacterByName(name));
        }

        for (Character c : Entities.xhtmlByVal.keySet()) {
            assertFalse("The xhtml restricted named entity '" + c + "' should not be present.", Entities.isBaseNamedEntity(Character.toString(c)));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> entities = Entities.full;
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals("The named entity '" + name + "' should map to the character " + character, character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testEscape() {
        Document.OutputSettings settings = new Document.OutputSettings();
        String input = "This & that & < > &quot; &apos; &amp; &gt; &lt; &nbsp; &ldquo; &rdquo; &hellip; &copy; &reg; &trade; &euro;";
        String expected = "This &amp;amp; amp &amp;amp; &lt; &amp;lt; > &amp;gt; &quot; &amp;quot; &amp;amp;amp; &ldquo; &rdquo; &hellip; &copy; &reg; &trade; &euro;";

        String output = Entities.escape(input, settings);
        assertEquals("The escaped string should match the expected output.", expected, output);
    }

    @Test
    public void testUnescape() {
        String input = "This &amp;amp; amp &lt; &amp;lt; > &amp;gt; &quot; &amp;quot; &amp;amp;amp; &ldquo; &rdquo; &hellip; &copy; &reg; &trade; &euro;";
        String expected = "This &amp;amp; amp < > &quot; &quot; &amp;amp;amp; “ &rdquo; … &copy; &reg; &trade; &euro;";

        String output = Entities.unescape(input, false);
        assertEquals("The unescaped string should match the expected output.", expected, output);
    }

    @Test
    public void testCanEncode() {
        Map<Character, String> map = Entities.fullByVal;
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();

        for (Map.Entry<Character, String> entry : map.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue();
            boolean canEncode = Entities.canEncode(Entities.CoreCharset.utf, character, encoder);
            boolean expected = name.length() == 1;
            assertTrue("The character '" + character + "' (" + name + ") should be encodable as " + (expected ? "ASCII" : "UTF-8"), canEncode == expected);
        }

        Character unsupported = Character.toChars(0x1F000)[0];
        assertFalse("The character " + unsupported + " should not be encodable as UTF-8.", Entities.canEncode(Entities.CoreCharset.utf, unsupported, encoder));
    }
}