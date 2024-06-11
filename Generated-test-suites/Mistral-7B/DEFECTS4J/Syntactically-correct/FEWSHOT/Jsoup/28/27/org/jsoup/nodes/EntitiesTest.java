package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
            assertTrue("Named entity " + name + " should be known", Entities.isNamedEntity(name));
            assertEquals("Named entity " + name + " should map to " + character, character, Entities.getCharacterByName(name));
        }

        assertFalse("lt should not be a named entity", Entities.isNamedEntity("lt"));
        assertFalse("gt should not be a named entity", Entities.isNamedEntity("gt"));
    }

    @Test
    public void testEscapeAndUnescape() {
        String testString = "Hello & World! & < > & \" ' & &#x27; & &#123; & &#125; & &#160; & &#x201C; & &#x201D; & &#153; & &#169;";
        String expected = "Hello &amp; World! &amp; &lt; &gt; &amp; &quot; &apos; &amp; #x27; &amp; #123; &amp; #125; &amp; #160; &amp; #x201C; &amp; #x201D; &amp; #153; &amp; #169;";

        assertEquals("String should escape correctly", expected, Entities.escape(testString, null, Entities.EscapeMode.extended));

        String unescaped = Entities.unescape(expected, false);
        assertEquals("String should unescape correctly", testString, unescaped);
    }

    @Test
    public void testEscapeAndUnescapeWithXHTMLMode() {
        String testString = "Hello & World! & < > & \" ' & &#x27; & &#123; & &#125; & &#160; & &#x201C; & &#x201D; & &#153; & &#169;";
        String expected = "Hello &amp; World! &amp; &lt; &amp; &gt; &amp; &quot; &apos; &amp; #x27; &amp; #123; &amp; #125; &amp; #160; &amp; &quot; &amp; &quot; &amp; #153; &amp; #169;";

        assertEquals("String should escape correctly in XHTML mode", expected, Entities.escape(testString, null, Entities.EscapeMode.xhtml));

        String unescaped = Entities.unescape(expected, false);
        assertEquals("String should unescape correctly", testString, unescaped);
    }

    @Test
    public void testEscapeAndUnescapeWithBaseMode() {
        String testString = "Hello & World! & < > & \" ' & &#x27; & &#123; & &#125; & &#160; & &#x201C; & &#x201D; & &#153; & &#169;";
        String expected = "Hello &amp; World! &amp; &lt; &gt; &amp; &quot; &apos; &amp; #x27; &amp; #123; &amp; #125; &amp; #160; &amp; &quot; &amp; &quot; &amp; #153; &amp; #169;";

        assertEquals("String should escape correctly in base mode", expected, Entities.escape(testString, null, Entities.EscapeMode.base));

        String unescaped = Entities.unescape(expected, false);
        assertEquals("String should unescape correctly", testString, unescaped);
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.base;
        assertEquals("Entities should have expected size", 233, entities.size());

        Map<String, Character> entitiesFromFile = Entities.loadEntities("entities-base.properties");
        assertEquals("Entities from file should have expected size", 233, entitiesFromFile.size());

        Map<Character, String> mappedEntities = Entities.baseByVal;
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            Character character = entry.getValue();
            String name = entry.getKey();
            assertEquals("Entity name for character " + character + " should be " + name, name, mappedEntities.get(character));
        }
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> entities = Entities.full;
        Map<Character, String> mappedEntities = Entities.toCharacterKey(entities);

        for (Map.Entry<Character, String> entry : mappedEntities.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue();
            assertEquals("Key for entity " + name + " should be " + character, character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> entities = Entities.full;
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals("Character for entity " + name + " should be " + character, character, Entities.getCharacterByName(name));
        }
    }
}