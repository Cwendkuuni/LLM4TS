package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<Character, String> fullMap = Entities.full;
        for (Map.Entry<Character, String> entry : fullMap.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue();
            boolean expectedResult = true;
            boolean result = Entities.isNamedEntity(name);
            Assert.assertEquals("Unexpected result for named entity: " + name, expectedResult, result);
        }

        // Test for non-existent entity
        String nonExistentEntity = "nonesuch";
        boolean result = Entities.isNamedEntity(nonExistentEntity);
        Assert.assertFalse("Unexpectedly recognized non-existent entity: " + nonExistentEntity, result);
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> baseMap = Entities.base;
        for (Map.Entry<String, Character> entry : baseMap.entrySet()) {
            String name = entry.getKey();
            Character key = entry.getValue();
            boolean expectedResult = true;
            boolean result = Entities.isBaseNamedEntity(name);
            Assert.assertEquals("Unexpected result for base named entity: " + name, expectedResult, result);
        }

        // Test for non-existent entity in base set
        String nonExistentBaseEntity = "nonesuch";
        boolean result = Entities.isBaseNamedEntity(nonExistentBaseEntity);
        Assert.assertFalse("Unexpectedly recognized non-existent base entity: " + nonExistentBaseEntity, result);
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, String> fullMap = Entities.full;
        for (Map.Entry<Character, String> entry : fullMap.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue();
            Character expectedResult = key;
            Character result = Entities.getCharacterByName(name);
            Assert.assertEquals("Unexpected result for named entity: " + name, expectedResult, result);
        }

        // Test for non-existent entity
        String nonExistentEntity = "nonesuch";
        Character result = Entities.getCharacterByName(nonExistentEntity);
        Assert.assertNull("Unexpected result for non-existent entity: " + nonExistentEntity, result);
    }

    @Test
    public void testEscape() {
        StringBuilder accum = new StringBuilder();
        String input = "&lt;foo&amp;bar&quot;<>&#x26;";
        Entities.escape(accum, input, new Document.OutputSettings(Entities.EscapeMode.base), false, false, false);
        String expectedOutput = "&amp;lt;foo&amp;amp;amp;bar&amp;quot;&amp;lt;&amp;gt;&amp;#x26;";
        String output = accum.toString();
        Assert.assertEquals("Unexpected output for given input", expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;foo&amp;amp;amp;bar&amp;quot;&amp;lt;&amp;gt;&amp;#x26;";
        String expectedOutput = "&lt;foo&amp;amp;bar&quot;<>&amp;#x26;";
        String output = Entities.unescape(input, false);
        Assert.assertEquals("Unexpected output for given input", expectedOutput, output);

        String strictInput = "&amp;lt;foo&amp;amp;amp;bar&amp;quot;&amp;lt;&amp;gt;&amp;#x26;&amp;#x27;";
        String strictOutput = "&lt;foo&amp;amp;bar&quot;&lt;&gt;&amp;#x26;&#x27;";
        String outputStrict = Entities.unescape(strictInput, true);
        Assert.assertEquals("Unexpected output for given strict input", strictOutput, outputStrict);
    }
}