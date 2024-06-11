package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        Map<Character, String> fullMap = entities.getFullMap();
        for (Map.Entry<Character, String> entry : fullMap.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            assertTrue(entities.isNamedEntity(value));
            assertTrue(entities.isNamedEntity(String.valueOf(key)));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        Map<Character, String> baseMap = entities.getBaseMap();
        for (Map.Entry<Character, String> entry : baseMap.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            assertTrue(entities.isBaseNamedEntity(value));
            assertTrue(entities.isBaseNamedEntity(String.valueOf(key)));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        Map<Character, String> fullMap = entities.getFullMap();
        for (Map.Entry<Character, String> entry : fullMap.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            assertEquals(key, entities.getCharacterByName(value));
            assertEquals(value, entities.getCharacterByName(String.valueOf(key)));
        }
    }

    @Test
    public void testEscapeMethodBasic() {
        Entities entities = new Entities();
        StringBuilder accumulator = new StringBuilder();
        String input = "&lt;<>&amp;\"";
        OutputSettings outputSettings = new OutputSettings();
        entities.escape(accumulator, input, outputSettings, false, false, false);
        String expectedOutput = "&amp;lt;&lt;amp;&amp;amp;&quot;";
        assertEquals(expectedOutput, accumulator.toString());
    }

    @Test
    public void testIsNamedEntityWithInvalidName() {
        Entities entities = new Entities();
        assertFalse(entities.isNamedEntity(""));
        assertFalse(entities.isNamedEntity(null));
    }

    @Test(expected = NullPointerException.class)
    public void testGetCharacterByNameWithInvalidName() {
        Entities entities = new Entities();
        assertFalse(entities.isNamedEntity(null));
        entities.getCharacterByName(null);
    }

    @Test
    public void testEscapeWithDifferentOutputSettings() {
        Entities entities = new Entities();
        StringBuilder accumulator = new StringBuilder();
        String input = "&lt;<>&amp;\"";

        OutputSettings outputSettings1 = new OutputSettings();
        outputSettings1.escapeMode(Entities.EscapeMode.xhtml);
        entities.escape(accumulator, input, outputSettings1, false, false, false);
        String expectedOutput1 = "&lt;";
        assertEquals(expectedOutput1, accumulator.toString());

        OutputSettings outputSettings2 = new OutputSettings();
        outputSettings2.escapeMode(Entities.EscapeMode.extended);
        entities.escape(accumulator, input, outputSettings2, false, false, false);
        String expectedOutput2 = "&lt;lt;&amp;amp;&quot;";
        assertEquals(expectedOutput2, accumulator.toString());
    }

    @Test
    public void testCanEncodeWithValidChar() {
        Entities entities = new Entities();
        CoreCharset charset = CoreCharset.ascii;
        char c = 'A';
        assertTrue(entities.canEncode(charset, c, null));
    }

    @Test
    public void testCanEncodeWithInvalidChar() {
        Entities entities = new Entities();
        CoreCharset charset = CoreCharset.ascii;
        char c = '\u00FF';
        assertFalse(entities.canEncode(charset, c, null));
    }

    @Test
    public void testLoadEntitiesFromFile() throws IOException {
        Entities entities = new Entities();
        Map<String, Character> loadedEntities = entities.loadEntities("entities-base.properties");
        assertEquals(Entities.BASE.size(), loadedEntities.size());
    }

    @Test(expected = MissingResourceException.class)
    public void testLoadEntitiesFromMissingFile() {
        Entities entities = new Entities();
        entities.loadEntities("missingFile.properties");
    }

    @Test
    public void testToCharacterKey() {
        Entities entities = new Entities();
        Map<String, Character> stringToCharacterMap = entities.getBaseMap();
        Map<Character, String> characterToStringMap = entities.toCharacterKey(stringToCharacterMap);

        for (Map.Entry<Character, String> entry : characterToStringMap.entrySet()) {
            assertEquals(entry.getValue(), entities.getCharacterByName(String.valueOf(entry.getKey())));
        }
    }

    @Test
    public void testIsBaseNamedEntityWithInvalidName() {
        Entities entities = new Entities();
        assertFalse(entities.isBaseNamedEntity(""));
        assertFalse(entities.isBaseNamedEntity(null));
    }

    @Test
    public void testParseEntitiesWithNullInput() {
        Entities entities = new Entities();
        assertNull(entities.parseEntities(null));
    }

    @Test
    public void testParseEntities() {
        Entities entities = new Entities();
        String input = "&lt;<>&amp;\"";
        String expectedOutput = "&lt;<>&amp;\"";
        assertEquals(expectedOutput, entities.parseEntities(input).toString());
    }

    @Test
    public void testParseEntitiesWithInvalidInput() {
        Entities entities = new Entities();
        String input = "&lt;&gt;&amp;";
        String expectedOutput = "&amp;lt;&gt;&amp;";
        assertEquals(expectedOutput, entities.parseEntities(input).toString());
    }

    @Test
    public void testParseEntitiesWithInvalidInput2() {
        Entities entities = new Entities();
        String input = "&invalidEntity;&amp;";
        String expectedOutput = "&amp;invalidEntity;&amp;";
        assertEquals(expectedOutput, entities.parseEntities(input).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEntitiesWithInvalidInput3() {
        Entities entities = new Entities();
        String input = "&";
        entities.parseEntities(input);
    }
}