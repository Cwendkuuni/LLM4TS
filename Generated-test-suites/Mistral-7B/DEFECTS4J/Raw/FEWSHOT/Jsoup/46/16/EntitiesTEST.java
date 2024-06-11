package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue(Entities.isNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> baseByVal = Entities.baseByVal;
        for (Map.Entry<Character, String> entry : baseByVal.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue();
            assertTrue(Entities.isBaseNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('>', Entities.getCharacterByName("gt"));
        assertEquals('\u00A0', Entities.getCharacterByName("nbsp"));
        assertEquals('\u0022', Entities.getCharacterByName("quot"));
    }

    @Test
    public void testIsNamedEntityAndIsBaseNamedEntityDifference() {
        Map<String, Character> full = Entities.full;
        Map<Character, String> baseByVal = Entities.baseByVal;

        for (Character character : full.values()) {
            String name = Entities.getCharacterByName(String.valueOf(character)).toLowerCase();
            if (baseByVal.containsKey(character)) {
                assertTrue(Entities.isNamedEntity(name));
                assertTrue(Entities.isBaseNamedEntity(name));
            } else {
                assertTrue(Entities.isNamedEntity(name));
                assertFalse(Entities.isBaseNamedEntity(name));
            }
        }
    }

    @Test
    public void testEscape() {
        Document.OutputSettings outputSettings = new Document.OutputSettings(Entities.EscapeMode.extended);
        String input = "Hello & World 🌎! <foo bar> <baz quux>&#x1234;</baz> <quux quux>&quot;";
        String expectedOutput = "Hello &amp; World &#x1F0DD;&#x1F0DC;! &lt;foo bar&gt; &lt;baz quux&gt;&amp;#x022;";
        String output = Entities.escape(input, outputSettings);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeWithXhtmlOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings(Entities.EscapeMode.xhtml);
        String input = "Hello & World 🌎! <foo bar> <baz quux>&#x1234;</baz> <quux quux>&quot;";
        String expectedOutput = "Hello &amp; World &entityDef;x1F0DD; &entityDef;x1F0DC;! &lt;foo bar&gt; &lt;baz quux&gt;&amp;#x022;";
        String output = Entities.escape(input, outputSettings);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeWithBaseOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings(Entities.EscapeMode.base);
        String input = "Hello & World 🌎! <foo bar> <baz quux>&#x1234;</baz> <quux quux>&quot;";
        String expectedOutput = "Hello &amp;amp; World &#xA0;! &lt;foo bar&gt; &lt;baz quux&gt;&amp;amp;#x26;#x022;";
        String output = Entities.escape(input, outputSettings);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeWithAsciiOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings(Entities.EscapeMode.ascii);
        String input = "Hello & World 🌎! <foo bar> <baz quux>&#x1234;</baz> <quux quux>&quot;";
        String expectedOutput = "Hello &amp;amp;amp; World &#xA0;! &lt;foo bar&gt; &lt;baz quux&gt;&amp;amp;amp;#x26;#x022;";
        String output = Entities.escape(input, outputSettings);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        String input = "Hello &amp; World &entityDef;x1F0DD; &amp;lt;foo bar&gt; &amp;#x26;#x022;";
        String expectedOutput = "Hello & World 🌎! <foo bar>";
        String output = Entities.unescape(input, false);
        assertEquals(expectedOutput, output);

        String strictInput = "Hello &amp; World 🌎! <foo bar>&#x26;&#x022;";
        String strictOutput = "Hello & World 🌎! <foo bar>" + System.lineSeparator() + "&amp;";
        String strictExpectedOutput = "Hello & World 🌎! <foo bar>" + System.lineSeparator() + "&amp;amp;";
        String strictOutput2 = Entities.unescape(strictInput, true);
        assertEquals(strictExpectedOutput, strictOutput2);
    }
}