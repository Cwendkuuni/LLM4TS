package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Entities;
import org.jsoup.parser.Parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isNamedEntity("lt"));
        assertFalse(entities.isNamedEntity("gt")); // different entity
        assertFalse(entities.isNamedEntity("invalid")); // not a named entity
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isBaseNamedEntity("amp"));
        assertTrue(entities.isBaseNamedEntity("lt"));
        assertFalse(entities.isBaseNamedEntity("gt")); // different entity
        assertFalse(entities.isBaseNamedEntity("invalid")); // not a base named entity
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        assertEquals('<', entities.getCharacterByName("lt"));
        assertEquals('&', entities.getCharacterByName("amp"));
        assertNull(entities.getCharacterByName("invalid")); // not a named entity
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNamedEntityWithNull() {
        Entities entities = new Entities();
        entities.isNamedEntity(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharacterByNameWithNull() {
        Entities entities = new Entities();
        entities.getCharacterByName(null);
    }

    @Test
    public void testEscapeMethodWithString() {
        Entities entities = new Entities();
        String string = "Hello <world>! & this & that & <&> & &quot; & ' & ¥";
        String expected = "Hello &lt;world&gt;! &amp; &amp; &amp; &amp; &quot; &apos; & #yen;";
        String result = entities.escape(string, null);
        assertEquals(expected, result);
    }

    @Test
    public void testEscapeMethodWithDocumentOutputSettings() {
        Entities entities = new Entities();
        Document.OutputSettings settings = new Document.OutputSettings();
        String string = "Hello <world>! & this & that & <&> & &quot; & ' & ¥";
        String expected = "Hello &lt;world&gt;! &amp; &amp; &amp; &amp; &quot; &apos; & #x{59};";
        String result = entities.escape(null, string, settings, false, false, false);
        assertEquals(expected, result);
    }

    @Test
    public void testUnescapeMethodWithString() {
        Entities entities = new Entities();
        String string = "Hello &lt;world&gt;! &amp; &amp; &amp; &amp; &quot; &apos; & #x{59};";
        String expected = "Hello <world>! & this & that & <&> & &quot; & ' & ¥";
        String result = entities.unescape(string, false);
        assertEquals(expected, result);
    }

    @Test
    public void testUnescapeMethodWithBoolean() {
        Entities entities = new Entities();
        String string = "Hello &lt;world&gt;! &amp; &amp; &amp; &amp; &quot; &apos; & #x{59};";
        String expected = "Hello <world>! &amp; &amp; &amp; &quot; &apos; & ¥";
        String result = entities.unescape(string, true);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeMethodWithNullString() {
        Entities entities = new Entities();
        entities.unescape(null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeMethodWithNullBoolean() {
        Entities entities = new Entities();
        entities.unescape("Hello &lt;world&gt;! &amp; &amp; &amp; &amp; &quot; &apos; & #x{59};", null);
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();
        // Testing with ASCII characters
        assertTrue(entities.canEncode(Entities.CoreCharset.ascii, 'A', entities.getEncoder()));
        assertTrue(entities.canEncode(Entities.CoreCharset.ascii, 'B', entities.getEncoder()));
        // Testing with non-ASCII characters
        assertTrue(entities.canEncode(Entities.CoreCharset.utf, 'é', entities.getEncoder()));
        // Testing with invalid input
        assertFalse(entities.canEncode(Entities.CoreCharset.ascii, '\uD800', entities.getEncoder()));
        assertFalse(entities.canEncode(Entities.CoreCharset.utf, '\uDFFF', entities.getEncoder()));
    }

    @Test
    public void testLoadEntities() {
        Entities entities = new Entities();
        Map<String, Character> loadedEntities = entities.loadEntities("entities-base.properties");
        assertEquals(4, loadedEntities.size());
        assertTrue(loadedEntities.containsKey("amp"));
        assertTrue(loadedEntities.containsKey("lt"));
        assertTrue(loadedEntities.containsKey("gt"));
        assertTrue(loadedEntities.containsKey("quot"));
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadEntitiesWithNullFilename() {
        Entities entities = new Entities();
        entities.loadEntities(null);
    }

    @Test
    public void testToCharacterKey() {
        Entities entities = new Entities();
        Map<String, Character> entitiesMap = new HashMap<>();
        entitiesMap.put("amp", '&');
        entitiesMap.put("lt", '<');
        Map<Character, String> expectedMap = entities.toCharacterKey(entitiesMap);
        assertEquals(entitiesMap.size(), expectedMap.size());
        assertTrue(expectedMap.containsKey('&'));
        assertTrue(expectMap.containsKey('<'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToCharacterKeyWithNullInput() {
        Entities entities = new Entities();
        entities.toCharacterKey(null);
    }
}