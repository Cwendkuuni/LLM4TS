package org.jsoup.nodes.test;

import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for Entities class.
 */
public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        Map<String, Character> fullMap = Entities.full;
        for (Map.Entry<String, Character> entry : fullMap.entrySet()) {
            String name = entry.getKey();
            Character value = entry.getValue();
            boolean expectedResult = true;
            boolean actualResult = entities.isNamedEntity(name);
            Assert.assertEquals("Expected named entity '" + name + "' to be in the map.", expectedResult, actualResult);
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        Map<String, Character> baseMap = Entities.base;
        for (Map.Entry<String, Character> entry : baseMap.entrySet()) {
            String name = entry.getKey();
            Character value = entry.getValue();
            boolean expectedResult = true;
            boolean actualResult = entities.isBaseNamedEntity(name);
            Assert.assertEquals("Expected named entity '" + name + "' to be in the base set.", expectedResult, actualResult);
        }
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        Map<String, Character> fullMap = Entities.full;
        for (Map.Entry<String, Character> entry : fullMap.entrySet()) {
            String name = entry.getKey();
            Character expectedResult = entry.getValue();
            Character actualResult = entities.getCharacterByName(name);
            Assert.assertEquals("Expected character for named entity '" + name + "' to be '" + expectedResult + "'.",
                    expectedResult, actualResult);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNamedEntityWithInvalidName() {
        Entities entities = new Entities();
        String invalidName = "invalidName";
        boolean actualResult = entities.isNamedEntity(invalidName);
        Assert.assertFalse("Expected named entity with invalid name to not be in the map.", actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharacterByNameWithInvalidName() {
        Entities entities = new Entities();
        String invalidName = "invalidName";
        Character actualResult = entities.getCharacterByName(invalidName);
        Assert.assertNull("Expected null for character of invalid named entity '" + invalidName + "'.", actualResult);
    }

    @Test
    public void testEscapeMethod() {
        Entities entities = new Entities();
        StringBuilder accumulator = new StringBuilder();
        String input = "&amp;<lt>&gt;\"";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        entities.escape(accumulator, input, outputSettings, false, false, false);
        String expectedOutput = "&amp;amp;<amp;&lt;>";
        String actualOutput = accumulator.toString();
        Assert.assertEquals("Expected output of escape method to be '" + expectedOutput + "'.", expectedOutput, actualOutput);
    }

    @Test
    public void testUnescapeMethod() {
        Entities entities = new Entities();
        String input = "&amp;amp;<amp;&lt;>";
        String output = entities.unescape(input);
        String expectedOutput = "&amp;<lt>";
        Assert.assertEquals("Expected output of unescape method to be '" + expectedOutput + "'.", expectedOutput, output);
    }

    @Test
    public void testCanEncodeMethod() {
        Entities entities = new Entities();
        CoreCharset coreCharset = CoreCharset.ascii;
        char c = 'À';
        boolean expectedResult = true;
        boolean actualResult = entities.canEncode(coreCharset, c, entities.getEncoder());
        Assert.assertEquals("Expected char '" + c + "' to be encodable.", expectedResult, actualResult);
    }

    @Test
    public void testCanEncodeWithNonEncodableChar() {
        Entities entities = new Entities();
        CoreCharset coreCharset = CoreCharset.ascii;
        char c = '\u0345';
        boolean expectedResult = false;
        boolean actualResult = entities.canEncode(coreCharset, c, entities.getEncoder());
        Assert.assertEquals("Expected char '" + c + "' to not be encodable.", expectedResult, actualResult);
    }

}