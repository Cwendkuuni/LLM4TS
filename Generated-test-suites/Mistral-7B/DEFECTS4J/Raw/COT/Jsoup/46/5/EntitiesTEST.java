package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {
    

    // Test all public methods in Entities class

    @Test
    public void testIsNamedEntity() {
        Map<Character, String> fullEntities = Entities.full;
        for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue().toString();
            assertTrue(Entities.isNamedEntity(value));
            assertTrue(Entities.isNamedEntity(Character.toString(key)));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> baseEntities = Entities.base;
        for (Map.Entry<Character, String> entry : baseEntities.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue().toString();
            assertTrue(Entities.isBaseNamedEntity(value));
            assertTrue(Entities.isBaseNamedEntity(Character.toString(key)));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, String> fullEntities = Entities.full;
        for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
            Character key = entry.getKey();
            Character expected = key;
            Character actual = Entities.getCharacterByName(Character.toString(key));
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testEscapeMethod() {
        String input = "&amp;<>&\"'&nbsp;&Auml;";
        Document.OutputSettings out = new Document.OutputSettings();

        String expected = "&amp;amp;&lt;&gt;&amp;gt;&quot;&#196;&#228;";
        String actual = Entities.escape(input, out);

        assertEquals(expected, actual);
    }

    @Test
    public void testUnescapeMethod() {
        String input = "&amp;amp;&lt;&gt;&amp;gt;&quot;&#196;&#228;";
        String actual = Entities.unescape(input, false);

        String expected = "&amp;<>&gt;&quot; ä";
        assertEquals(expected, actual);
    }

    @Test(expected = MissingResourceException.class)
    public void testLoadEntitiesWithMissingFile() {
        Entities.loadEntities("nonexistentfile.properties");
    }

    @Test
    public void testCanEncodeMethod() {
        CoreCharset charset = CoreCharset.ascii;
        char c = '\u00E1'; // the Latin Small Letter A with an acute

        boolean expected = true;
        boolean actual = Entities.canEncode(charset, c, null);

        assertEquals(expected, actual);
    }

    @Test
    public void testCanEncodeMethodWithFallback() {
        CoreCharset charset = CoreCharset.fallback;
        char c = '\u00E1'; // the Latin Small Letter A with an acute

        boolean expected = true;
        boolean actual = Entities.canEncode(charset, c, null);

        assertEquals(expected, actual);
    }

    @Test
    public void testCanEncodeMethodWithUnsupportedCharset() {
        CoreCharset charset = CoreCharset.ascii;
        char c = '\uD800'; // high surrogate

        boolean expected = false;
        boolean actual = Entities.canEncode(charset, c, null);

        assertEquals(expected, actual);
    }

    
}