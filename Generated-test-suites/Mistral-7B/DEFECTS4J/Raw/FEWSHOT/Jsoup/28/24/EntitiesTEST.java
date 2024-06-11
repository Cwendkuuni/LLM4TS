package org.jsoup.nodes;

import static org.junit.Assert.*;

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
        Map<String, Character> fullEntities = Entities.full;
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String key = entry.getKey();
            Character value = entry.getValue();
            assertTrue(Entities.isNamedEntity(key));
            assertEquals(value, Entities.getCharacterByName(key));
        }

        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> fullEntities = Entities.full;
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String key = entry.getKey();
            Character value = entry.getValue();
            assertEquals(value, Entities.getCharacterByName(key));
        }

        assertNull(Entities.getCharacterByName(null));
        assertNull(Entities.getCharacterByName(""));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings settings = new Document.OutputSettings();
        String text = "Hello, World! & < > \" '";
        String expected = "Hello,%20World!%20%26%20%3C%20%3E%20%22%20%27";
        assertEquals(expected, Entities.escape(text, settings));
    }

    @Test
    public void testEscapeWithEncoder() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.encoder = new StringCharacterEncodingSchema("UTF-8");
        String text = "Hello, World! & < > \" '";
        String expected = "Hello, World! & < > \" '";
        assertEquals(expected, Entities.escape(text, settings.encoder, Entities.EscapeMode.xhtml));
    }

    @Test
    public void testEscapeWithExtended() {
        Document.OutputSettings settings = new Document.OutputSettings();
        String text = "Hello, World! &#x26;&#x23; &<>& \"'";
        String expected = "Hello,%20World!%20%23%23%26%23%3B%20%26%26%3C%26%3E%3C%26%3E%20%22%27";
        assertEquals(expected, Entities.escape(text, settings.encoder, Entities.EscapeMode.extended));
    }

    @Test
    public void testUnescape() {
        String text = "Hello,%20World!%20%23%23%26%23%3B%20%26%26%3C%26%3E%3C%26%3E%20%22%27";
        String expected = "Hello, World! ## && <> <> \"'";
        assertEquals(expected, Entities.unescape(text));
    }

    @Test
    public void testUnescapeStrict() {
        String text = "Hello,%20World!%20%23%23%26%23%3B%20%26%26%3C%26%3E%3C%26%3E%20%22%27";
        String expected = "Hello, World! &#x23;&#x23;#x26;&#x26;#x3C;&#x3C;#x3E;&#x3E; \"'";
        assertEquals(expected, Entities.unescape(text, true));
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assertEquals(Entities.base.size(), entities.size());
        for (Map.Entry<String, Character> entry : Entities.base.entrySet()) {
            String key = entry.getKey();
            Character value = entry.getValue();
            assertEquals(value, entities.get(key));
        }
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> entities = new HashMap<>();
        entities.put("lt", '<');
        entities.put("gt", '>');
        Map<Character, String> expected = new HashMap<>();
        expected.put('<', "lt");
        expected.put('>', "gt");
        Map<Character, String> result = Entities.toCharacterKey(entities);
        assertEquals(expected, result);
    }

}