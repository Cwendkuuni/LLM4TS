package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            assertTrue("Named entity " + name + " should exist", Entities.isNamedEntity(name));
        }

        String nonEntity = "foo";
        assertFalse("Non-named entity " + nonEntity + " should not be a named entity", Entities.isNamedEntity(nonEntity));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals("Named entity " + name + " should have the character '" + character + "'", character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testEscape() {
        Document.OutputSettings settings = new Document.OutputSettings();
        EscapeMode escapeMode = Entities.EscapeMode.base;
        String input = "This <text> contains &amp; some &lt;entities&gt;, &apos; single quotes, and &#1234; unicode.";
        String expected = "This &lt;text&gt; contains &amp;amp; some &lt;entities&gt;, &apos;apos; and &#1234;.";

        String output = Entities.escape(input, settings, escapeMode);
        assertEquals("Escaped output should be " + expected, expected, output);
    }

    @Test
    public void testUnescape() {
        String input = "This &lt;text&gt; contains &amp;some &lt;entities&gt;, &apos;single quotes&apos; and &#1234;.";
        String expected = "This <text> contains &amp;some other &<entities>;, 'single quotes' and .";

        String output = Entities.unescape(input);
        assertEquals("Unescaped output should be " + expected, expected, output);
    }

    @Test
    public void testUnescapeStrict() {
        String input = "This &lt;text&gt; contains &amp;some &lt;entities&gt;, &apos;single quotes&apos; and &#1234;.";
        String output = Entities.unescape(input);
        String expected = "This <text> contains &amp;some other &<entities>;, 'single quotes' and .";

        String strictOutput = Entities.unescape(output, true);
        assertEquals("Strictly unescaped output should be " + expected, expected, strictOutput);
    }

    @Test
    public void testIsNamedEntityXhtml() {
        Map<Character, String> xhtmlByVal = Entities.EscapeMode.xhtml.getMap();
        for (Map.Entry<Character, String> entry : xhtmlByVal.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue();
            assertTrue("Named entity " + name + " should exist in XHTML", Entities.isNamedEntity(name));
            assertTrue("Named entity " + name + " should map to character " + character, Equals.equalChars(character, Entities.getCharacterByName(name)));
        }

        String nonEntity = "foo";
        assertFalse("Non-named entity " + nonEntity + " should not be a named entity", Entities.isNamedEntity(nonEntity));
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> expected = new HashMap<String, Character>();
        Properties properties = new Properties();
        properties.setProperty("quot", "00022");
        properties.setProperty("amp", "00026");
        properties.setProperty("apos", "00027");
        properties.setProperty("lt", "0003C");
        properties.setProperty("gt", "0003E");

        Map<String, Character> actual = Entities.loadEntities("entities-base.properties");
        assertEquals("Loaded entities should equal", expected, actual);
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> input = new HashMap<String, Character>() {{
            put("quot", '\"');
            put("amp", '&');
            put("apos", '\'');
            put("lt", '<');
            put("gt", '>');
        }};

        Map<Character, String> output = Entities.toCharacterKey(input);
        assertEquals("Maps should be equal", input, output);
    }

}