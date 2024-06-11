package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Named entity " + name + " not found in full map", Entities.isNamedEntity(name));
            assertEquals("Named entity " + name + " not equals expected character " + character, character, Entities.getCharacterByName(name));
        }

        assertFalse("lt not a named entity", Entities.isNamedEntity("lt"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals("Named entity " + name + " not equals expected character " + character, character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testEscape() {
        String testString = "this is a test string with <entities> & <special> characters: &quot; &amp; &apos; &lt; &gt;";

        for (Entities.EscapeMode escapeMode : Entities.EscapeMode.values()) {
            String escaped = Entities.escape(testString, null, escapeMode);
            System.out.println(escapeMode + ": " + testString + " => " + escaped);
            assertEquals("Escape mode " + escapeMode + " does not produce correct output for testString", testString, Entities.unescape(escaped));
        }
    }

    @Test
    public void testUnescape() {
        String testString = "this is a test string with <entities> & &quot; &amp; &apos; &lt; &gt;";

        for (Entities.EscapeMode escapeMode : Entities.EscapeMode.values()) {
            String escaped = Entities.escape(testString, null, escapeMode);
            String unescaped = Entities.unescape(escaped);
            assertEquals("Escape mode " + escapeMode + " does not produce correct unescape output for testString", testString, unescaped);
        }

        String testString2 = "this is a test string with &nbsp; &copy; &trade; &reg; &deg; &euro; &micro; &sup1; &sup2; &sup3;";
        String unescaped2 = Entities.unescape(testString2);
        assertEquals("Unescape does not produce correct output for testString2", testString2, unescaped2);
    }

    @Test
    public void testUnescapeWithStrict() {
        String testString = "this is a test string with <entities> & &quot; &amp; &apos; &lt; &gt;";

        String escaped = Entities.escape(testString, null, Entities.EscapeMode.base);
        assertEquals("unescape with strict does not return the expected output for a base escaped string", testString, Entities.unescape(escaped, true));

        escaped = Entities.escape(testString, null, Entities.EscapeMode.extended);
        assertEquals("unescape with strict does not return the expected output for an extended escaped string", testString, Entities.unescape(escaped, true));
    }

    @Test
    public void testEntities() throws IOException {
        Map<String, Character> full = Entities.full;
        Map<String, Character> base = Entities.base;
        Map<Character, String> xhtmlByVal = Entities.xhtmlByVal;

        Properties properties = new Properties();
        try (InputStream in = Entities.class.getResourceAsStream("/entities-full.properties")) {
            properties.load(in);
            assertEquals("entities-full.properties does not have the expected number of entries", 1151, properties.size());
        }

        Map<String, Character> loadedFull = loadEntities(properties, "entities-full.properties");
        assertEquals("entities-full.properties does not contain the expected entries", full.size(), loadedFull.size());
        assertEquals("entities-full.properties does not have the same entries as the class constant", full, loadedFull);

        properties = new Properties();
        try (InputStream in = Entities.class.getResourceAsStream("/entities-base.properties")) {
            properties.load(in);
            assertEquals("entities-base.properties does not have the expected number of entries", 52, properties.size());
        }

        Map<String, Character> loadedBase = loadEntities(properties, "entities-base.properties");
        assertEquals("entities-base.properties does not contain the expected entries", base.size(), loadedBase.size());
        assertEquals("entities-base.properties does not have the same entries as the class constant", base, loadedBase);

        assertEquals("xhtmlByVal and Entities.xhtmlByVal are not equal", Entities.xhtmlByVal, Entities.xhtmlByVal);
    }

    private Map<String, Character> loadEntities(Properties properties, String filename) {
        Map<String, Character> entities = new HashMap<String, Character>();
        try {
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                Character value = Character.valueOf((char) Integer.parseInt((String) entry.getValue(), 16));
                String key = (String) entry.getKey();
                entities.put(key, value);
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("Could not parse character value as hexadecimal: " + e.getMessage());
        }
        return entities;
    }
}