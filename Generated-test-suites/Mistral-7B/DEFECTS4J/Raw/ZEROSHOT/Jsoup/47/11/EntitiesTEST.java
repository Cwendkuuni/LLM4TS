package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        Map<Character, String> fullEntities = entities.getFull();

        for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            assertTrue(entities.isNamedEntity(value));
        }

        assertFalse(entities.isNamedEntity(""));
        assertFalse(entities.isNamedEntity(null));
        assertFalse(entities.isNamedEntity("ltx"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        Map<Character, String> baseEntities = entities.getBaseByVal();

        for (Map.Entry<Character, String> entry : baseEntities.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            assertTrue(entities.isBaseNamedEntity(value));
        }

        assertFalse(entities.isBaseNamedEntity(""));
        assertFalse(entities.isBaseNamedEntity(null));
        assertFalse(entities.isBaseNamedEntity("gt"));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        Map<Character, String> fullEntities = entities.getFullByVal();

        for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
            Character key = entry.getKey();
            Character value = entities.getCharacterByName(entry.getValue());
            assertEquals(key, value);
        }

        assertNull(entities.getCharacterByName(""));
        assertNull(entities.getCharacterByName(null));
        assertNull(entities.getCharacterByName("ltx"));
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        String input = "This is a &lt;test&gt; with &quot;quotes&quot;, &amp;amp; and &lt;special&gt; characters.";
        String expectedOutput = "This is a &#x3C;test&#x3E; with &#x22;quotes&#x22;, &#x26;amp; and &#x3C;special&#x3E; characters.";

        String output = entities.escape(input, outputSettings);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeWithXHTML() {
        Entities entities = new Entities();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setEscapeMode(Entities.EscapeMode.XHTML);

        String input = "This is a &lt;test&gt; with &quot;quotes&quot;, &amp;amp; and &lt;special&gt; characters.";
        String expectedOutput = "This is a &lt;test&gt; with &quot;quotes&quot;, &amp;amp; and &lt;special&gt; characters.";

        String output = entities.escape(input, outputSettings);
        assertEquals(expectedOutput, output);

        String inputWithInvalidEntity = "This is a &foo;test&gt; with &quot;quotes&quot;, &amp;amp; and &lt;special&gt; characters.";
        String expectedOutputWithInvalidEntity = "This is a &foo;test&gt; with &quot;quotes&quot;, &amp;amp; and &lt;special&gt; characters.";

        String outputWithInvalidEntity = entities.escape(inputWithInvalidEntity, outputSettings);
        assertEquals(expectedOutputWithInvalidEntity, outputWithInvalidEntity);
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();

        String inputEscaped = "This is a &#x3C;test&#x3E; with &#x22;quotes&#x22;, &#x26;amp; and &#x3C;special&#x3E; characters.";
        String expectedOutput = "This is a <test> with \"quotes\", &amp; and <special> characters.";

        String output = entities.unescape(inputEscaped);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();

        CharsetEncoder encoder = java.nio.charset.StandardCharsets.UTF_8.newEncoder();
        Map<Character, String> utfEntities = entities.getFull();

        for (Map.Entry<Character, String> entry : utfEntities.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            assertTrue(entities.canEncode(entities.CoreCharset.utf, key, encoder));
        }

        Character invalidCharacter = '\uD800';
        assertFalse(entities.canEncode(entities.CoreCharset.utf, invalidCharacter, encoder));
    }

    @Test
    public void testLoadEntities() {
        Entities entities = new Entities();

        Map<String, Character> entitiesFromFile = entities.loadEntities("entities-full.properties");
        Map<Character, String> entitiesBackToFile = entities.toCharacterKey(entitiesFromFile);

        assertEquals(entities.getFull().size(), entitiesFromFile.size());
        for (Map.Entry<Character, String> entry : entities.getFull().entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            assertEquals(value, entitiesBackToFile.get(key));
        }
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingEntitiesFile() {
        Entities entities = new Entities();
        entities.loadEntities("non-existent-file.properties");
    }
}