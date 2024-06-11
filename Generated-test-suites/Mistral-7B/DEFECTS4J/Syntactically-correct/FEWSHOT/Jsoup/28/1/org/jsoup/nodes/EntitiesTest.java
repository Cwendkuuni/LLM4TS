package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String key = entry.getKey();
            Character value = entry.getValue();
            assertTrue(Entities.isNamedEntity(key));
            assertEquals(value, Entities.getCharacterByName(key));
        }

        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
        assertFalse(Entities.isNamedEntity("x"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String key = entry.getKey();
            Character value = entry.getValue();
            assertEquals(value, Entities.getCharacterByName(key));
        }

        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName(null));
        assertNull(Entities.getCharacterByName("x"));
    }

    @Test
    public void testEscape() {
        for (EscapeMode mode : EscapeMode.values()) {
            Map<Character, String> map = mode.getMap();
            String source = "&lt; &gt; &amp; &apos; &quot; &nbsp; &eacute; &hellip; &copy; &reg; &trade;";
            String expected = escape(source, mode);

            assertEquals(expected, Entities.escape(source, null, mode));
            assertEquals(expected, Entities.escape(source, null, mode.name()));
            assertEquals(expected, Entities.escape(source, null, Entities.EscapeMode.valueOf(mode.name())));
        }
    }

    @Test
    public void testUnescape() {
        String source = "&lt; &gt; &amp; &apos; &quot; &nbsp; &eacute; &hellip; &copy; &reg; &trade;";

        assertEquals("<< >&amp; '&apos;\" &nbsp;é &hellip; © ® ™", Entities.unescape(source));
        assertEquals("<< >&amp; '&apos;\" &nbsp;é &hellip; © ® ™", Entities.unescape(source, false));
        assertNotEquals("<< >&amp; '&apos;\" &nbsp;é &hellip; <html></html> © ® ™", Entities.unescape(source, true));
    }

    @Test
    public void testXhtmlByVal() {
        Map<Character, String> map = Entities.xhtmlByVal;
        assertEquals("quot", map.get('"'));
        assertEquals("amp", map.get('&'));
        assertEquals("apos", map.get('\''));
        assertEquals("lt", map.get('<'));
        assertEquals("gt", map.get('>'));

        assertFalse(map.containsKey(Character.MIN_VALUE));
        assertFalse(map.containsKey(Character.MAX_VALUE));
        assertFalse(map.containsKey('\n'));
        assertFalse(map.containsKey('\r'));
        assertFalse(map.containsKey('\t'));
        assertFalse(map.containsKey(' '));
        assertFalse(map.containsKey(Character.MIN_VALUE + 1));
        assertFalse(map.containsKey(Character.MAX_VALUE - 1));
    }

    @Test
    public void testLoadEntities() {
        Properties properties = new Properties();
        try {
            InputStream in = Entities.class.getResourceAsStream("entities-base.properties");
            properties.load(in);
            in.close();
        } catch (IOException e) {
            fail("Error loading entities resource: " + e.getMessage());
        }

        Map<String, Character> expected = new HashMap<String, Character>();
        expected.put("quot", Character.QUOTE);
        expected.put("apos", Character.QUOTE);
        expected.put("amp", '&');
        expected.put("lt", '<');
        expected.put("gt", '>');

        assertEquals(expected, Entities.loadEntities("entities-base.properties"));

        // Test invalid properties
        Properties invalidProperties = new Properties();
        try {
            InputStream in = Entities.class.getResourceAsStream("entities-invalid.properties");
            invalidProperties.load(in);
            in.close();
        } catch (IOException e) {
            // OK
        }

        assertEquals(Collections.emptyMap(), Entities.loadEntities("entities-invalid.properties"));

        // Test null properties
        Properties nullProperties = null;
        try {
            Entities.loadEntities(nullProperties);
            fail("Expected MissingResourceException");
        } catch (MissingResourceException e) {
            // OK
        }
    }

    private String escape(String string, EscapeMode escapeMode) {
        return Entities.escape(string, null, escapeMode);
    }
}