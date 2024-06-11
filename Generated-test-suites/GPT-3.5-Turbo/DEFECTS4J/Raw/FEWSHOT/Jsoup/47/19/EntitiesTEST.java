package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;

import org.junit.Test;

public class EntitiesTest {
    private static final Map<String, Character> full;
    private static final Map<Character, String> xhtmlByVal;
    private static final Map<String, Character> base;
    private static final Map<Character, String> baseByVal;
    private static final Map<Character, String> fullByVal;
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf((char) 0x0003C), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("foo"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        StringBuilder expected = new StringBuilder("foo");
        StringBuilder actual = new StringBuilder();
        Entities.escape(actual, "foo", out, false, false, false);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testUnescape() {
        String expected = "foo";
        String actual = Entities.unescape("foo");
        assertEquals(expected, actual);
    }

    @Test
    public void testUnescapeStrict() {
        String expected = "foo";
        String actual = Entities.unescape("foo", true);
        assertEquals(expected, actual);
    }

    private enum CoreCharset {
        ascii, utf, fallback;

        private static CoreCharset byName(String name) {
            if (name.equals("US-ASCII"))
                return ascii;
            if (name.startsWith("UTF-")) // covers UTF-8, UTF-16, et al
                return utf;
            return fallback;
        }
    }

    private static boolean canEncode(final CoreCharset charset, final char c, final CharsetEncoder fallback) {
        switch (charset) {
            case ascii:
                return c < 0x80;
            case utf:
                return true;
            default:
                return fallback.canEncode(c);
        }
    }

    private static Map<String, Character> loadEntities(String filename) {
        Properties properties = new Properties();
        Map<String, Character> entities = new HashMap<String, Character>();
        try {
            InputStream in = Entities.class.getResourceAsStream(filename);
            properties.load(in);
            in.close();
        } catch (IOException e) {
            throw new MissingResourceException("Error loading entities resource: " + e.getMessage(), "Entities", filename);
        }

        for (Map.Entry entry: properties.entrySet()) {
            Character val = Character.valueOf((char) Integer.parseInt((String) entry.getValue(), 16));
            String name = (String) entry.getKey();
            entities.put(name, val);
        }
        return entities;
    }

    private static Map<Character, String> toCharacterKey(Map<String, Character> inMap) {
        Map<Character, String> outMap = new HashMap<Character, String>();
        for (Map.Entry<String, Character> entry: inMap.entrySet()) {
            Character character = entry.getValue();
            String name = entry.getKey();

            if (outMap.containsKey(character)) {
                if (name.toLowerCase().equals(name))
                    outMap.put(character, name);
            } else {
                outMap.put(character, name);
            }
        }
        return outMap;
    }
}