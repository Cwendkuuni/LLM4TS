package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.*;
import java.util.regex.Pattern;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullMap = Entities.full;
        for (Character c : fullMap.values()) {
            String name = Entities.getCharacterByName(c.toString()).toString();
            assertTrue(Entities.isNamedEntity(name));
        }
    }

    @Test
    public void testIsNamedEntityFalse() {
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity("abc"));
        assertFalse(Entities.isNamedEntity("123"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('>', Entities.getCharacterByName("gt"));
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('"', Entities.getCharacterByName("quot"));
        assertEquals("'", Entities.getCharacterByName("apos"));
    }

    @Test
    public void testEscape() {
        String input = "&amp; < > &quot; ' &apos; &lt; &gt;";
        String expected = "&amp;amp; &lt; &gt; &amp;quot; &amp;apos; &amp;lt; &amp;gt;";
        assertEquals(expected, Entities.escape(input, null, Entities.EscapeMode.extended));
    }

    @Test
    public void testUnescape() {
        String input = "&amp;amp; &lt; &gt; &amp;quot; &amp;apos; &amp;lt; &amp;gt;";
        String expected = "&amp; < > &quot; ' &apos; < >";
        assertEquals(expected, Entities.unescape(input));
    }

    @Test
    public void testUnescapeWithStrict() {
        String input = "&amp;amp; &lt; &gt; &amp;quot; &amp;apos; &amp;lt; &amp;gt;";
        String expected = "&amp;amp; &lt; &gt; &quot; &apos; &lt; &gt;";
        assertEquals(expected, Entities.unescape(input, true));
    }

    @Test
    public void testUnescapeWithInvalid() {
        String input = "&invalid;";
        String expected = "&invalid;";
        assertEquals(expected, Entities.unescape(input));
    }

    @Test
    public void testLoadEntities() {
        Properties properties = new Properties();
        try {
            InputStream in = Entities.class.getResourceAsStream("/entities-base.properties");
            properties.load(in);
            in.close();
        } catch (IOException e) {
            fail("Error loading entities resource: " + e.getMessage());
        }

        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assertEquals(Entities.base.size(), entities.size());
        for (Map.Entry<String, Character> entry : Entities.base.entrySet()) {
            Character character = entry.getValue();
            String name = entry.getKey();
            assertEquals(name, Entities.getCharacterByName(character.toString()).toString());
            assertEquals(character, entities.get(name));
        }
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = new HashMap<>();
        inMap.put("quot", '\"');
        inMap.put("amp", '&');
        inMap.put("apos", "'");
        inMap.put("lt", '<');
        inMap.put("gt", '>');
        Map<Character, String> outMap = Entities.toCharacterKey(inMap);

        assertEquals(5, outMap.size());
        assertEquals("quot", outMap.get('\"'));
        assertEquals("amp", outMap.get('&'));
        assertEquals("apos", outMap.get("'"));
        assertEquals("lt", outMap.get('<'));
        assertEquals("gt", outMap.get('>'));
    }
}