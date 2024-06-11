package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.full;
        for (Map.Entry<String, Character> entity : fullEntities.entrySet()) {
            String entityName = entity.getKey();
            Character entityValue = entity.getValue();
            assertTrue("Named entity '" + entityName + "' should be recognized", Entities.isNamedEntity(entityName));
            assertEquals("Named entity '" + entityName + "' should map to '" + entityValue + "'", entityValue, Entities.getCharacterByName(entityName));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> xhtmlEntities = Entities.xhtmlByVal;
        for (Map.Entry<Character, String> entity : xhtmlEntities.entrySet()) {
            Character entityValue = entity.getKey();
            String entityName = entity.getValue();
            assertTrue("Base named entity '" + entityName + "' should be recognized", Entities.isBaseNamedEntity(entityName));
            assertEquals("Base named entity '" + entityName + "' should map to '" + entityValue + "'", entityValue, Entities.getCharacterByName(entityName));
        }

        String invalidEntityName = "invalidEntity";
        assertFalse("Invalid named entity '" + invalidEntityName + "' should not be recognized", Entities.isNamedEntity(invalidEntityName));
        assertFalse("Invalid named entity '" + invalidEntityName + "' should not be recognized as base", Entities.isBaseNamedEntity(invalidEntityName));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> fullEntities = Entities.full;
        Map<Character, String> xhtmlEntities = Entities.xhtmlByVal;

        // Test valid named entities
        for (Map.Entry<String, Character> entity : fullEntities.entrySet()) {
            String entityName = entity.getKey();
            Character entityValue = entity.getValue();
            assertEquals("Named entity '" + entityName + "' should map to '" + entityValue + "'", entityValue, Entities.getCharacterByName(entityName));
        }

        // Test valid base named entities
        for (Map.Entry<Character, String> entity : xhtmlEntities.entrySet()) {
            Character entityValue = entity.getKey();
            String entityName = entity.getValue();
            assertEquals("Base named entity '" + entityName + "' should map to '" + entityValue + "'", entityValue, Entities.getCharacterByName(entityName));
        }

        // Test invalid named entities
        String invalidEntityName = "invalidEntity";
        try {
            Entities.getCharacterByName(invalidEntityName);
            fail("Invalid named entity '" + invalidEntityName + "' should throw MissingResourceException");
        } catch (MissingResourceException e) {
            // expected
        }
    }

    @Test
    public void testEscape() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setEscapeMode(Entities.EscapeMode.base);

        String input = "Hello & World < tag > with &quot; quotes & #123;special character# 123;";
        String expectedOutput = "Hello &amp; World &lt; tag &gt; with &quot; quotes & #x26;special character# 123;";

        String output = Entities.escape(input, outputSettings);
        assertEquals("Input '"+ input + "' should be escaped to '" + expectedOutput + "'", expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        String input = "Hello &amp; World &lt; tag &gt; with &quot; quotes & #x26;special character# 123;";
        String expectedOutput = "Hello & World < tag > with \" quotes &special character# 123;";

        String output = Entities.unescape(input, false);
        assertEquals("Input '"+ input + "' should be unescaped to '" + expectedOutput + "'", expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithInvalidInput() {
        String input = "Hello &amp; World &lt; tag &gt; with &quot; quotes & #x26;special character# 123;<html>";
        Entities.unescape(input, false);
    }
}