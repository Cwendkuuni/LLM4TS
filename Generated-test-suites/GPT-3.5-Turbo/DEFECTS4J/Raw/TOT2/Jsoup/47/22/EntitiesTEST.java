package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        String input = "<div>&amp;</div>";
        String expectedOutput = "&lt;div&gt;&amp;amp;&lt;/div&gt;";
        assertEquals(expectedOutput, Entities.escape(input, null));
    }

    @Test
    public void testUnescape() {
        String input = "&lt;div&gt;&amp;amp;&lt;/div&gt;";
        String expectedOutput = "<div>&amp;</div>";
        assertEquals(expectedOutput, Entities.unescape(input));
    }

    @Test
    public void testCanEncode() {
        CoreCharset charset = CoreCharset.ascii;
        assertTrue(Entities.canEncode(charset, '<', null));
        assertFalse(Entities.canEncode(charset, 0x00FF, null));
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assertNotNull(entities);
        assertTrue(entities.containsKey("gt"));
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = new HashMap<>();
        inMap.put("abc", 'A');
        inMap.put("def", 'B');
        Map<Character, String> outMap = Entities.toCharacterKey(inMap);

        assertNotNull(outMap);
        assertEquals("abc", outMap.get('A'));
    }
}