package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Before
    public void setUp() {
        // setup code here
    }

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("dummy"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('\"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("dummy"));
    }

    @Test
    public void testEscape() {
        String string = "<p>Hello, World!</p>";
        String expected = "&lt;p&gt;Hello, World!&lt;/p&gt;";
        String escaped = Entities.escape(string, Document.OutputSettings.html());
        assertEquals(expected, escaped);
    }

    @Test
    public void testUnescape() {
        String string = "&lt;p&gt;Hello, World!&lt;/p&gt;";
        String expected = "<p>Hello, World!</p>";
        String unescaped = Entities.unescape(string);
        assertEquals(expected, unescaped);
    }

    @Test
    public void testUnescapeStrict() {
        String string = "&lt;p&gt;Hello, World!&lt;/p&gt;";
        String expected = "&lt;p&gt;Hello, World!&lt;/p&gt;";
        String unescaped = Entities.unescape(string, true);
        assertEquals(expected, unescaped);
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assertNotNull(entities);
        assertEquals(200, entities.size());
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = Entities.loadEntities("entities-base.properties");
        Map<Character, String> outMap = Entities.toCharacterKey(inMap);
        assertNotNull(outMap);
        assertEquals(200, outMap.size());
    }

}