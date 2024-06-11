package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.full;
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String name = entry.getKey();
            char character = entry.getValue();
            assertTrue(Entities.isNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsNamedEntityWithInvalidName() {
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
        assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> fullEntities = Entities.full;
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String name = entry.getKey();
            char character = entry.getValue();
            assertEquals(character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testGetCharacterByNameWithInvalidName() {
        assertNull(Entities.getCharacterByName(null));
        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName("foo"));
    }

    @Test
    public void testEscape() {
        String string = "&#x1F601;&#x1F602;&lt;&gt;&amp;&quot;&apos;";
        EscapeMode escapeMode = Entities.EscapeMode.extended;
        String escaped = Entities.escape(string, null, escapeMode);
        assertEquals("&#128513;&#128514;&lt;&gt;&amp;&quot;&apos;", escaped);

        escapeMode = Entities.EscapeMode.base;
        escaped = Entities.escape(string, null, escapeMode);
        assertEquals("&#128513;&#128514;&amp;&lt;&gt;&quot;&apos;", escaped);

        escapeMode = Entities.EscapeMode.xhtml;
        escaped = Entities.escape(string, null, escapeMode);
        assertEquals("&#128513;&#128514;&lt;&gt;&amp;", escaped);
    }

    @Test
    public void testEscapeWithNullInput() {
        String nullString = null;
        EscapeMode escapeMode = Entities.EscapeMode.extended;
        String escaped = Entities.escape(nullString, null, escapeMode);
        assertNull(escaped);
    }

    @Test
    public void testEscapeWithEmptyInput() {
        String emptyString = "";
        EscapeMode escapeMode = Entities.EscapeMode.extended;
        String escaped = Entities.escape(emptyString, null, escapeMode);
        assertEquals("", escaped);
    }

    @Test
    public void testUnescape() {
        String string = "&#128513;&#128514;&lt;&gt;&amp;&quot;&apos;";
        String unescaped = Entities.unescape(string);
        assertEquals("🔵🔶< >&\"'", unescaped);
    }

    @Test
    public void testUnescapeWithNullInput() {
        String nullString = null;
        String unescaped = Entities.unescape(nullString);
        assertNull(unescaped);
    }

    @Test
    public void testUnescapeWithEmptyInput() {
        String emptyString = "";
        String unescaped = Entities.unescape(emptyString);
        assertEquals("", unescaped);
    }

    @Test
    public void testUnescapeWithInvalidInput() {
        String string = "&#12345;";
        String unescaped = Entities.unescape(string);
        assertNull(unescaped);
    }

    @Test
    public void testUnescapeWithInvalidInputStrict() {
        String string = "&#12345;";
        String unescaped = Entities.unescape(string, true);
        assertNull(unescaped);
    }

    @Test
    public void testLoadEntities() {
        Properties properties = new Properties();
        try {
            InputStream in = Entities.class.getResourceAsStream("/entities-base.properties");
            properties.load(in);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assertEquals(Entities.base.size(), entities.size());
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            String name = entry.getKey();
            char character = entry.getValue();
            assertEquals(character, Entities.base.get(name));
        }
    }

    @Test
    public void testLoadEntitiesWithInvalidFilename() {
        assertThrows(MissingResourceException.class, () -> Entities.loadEntities("entities-invalid.properties"));
    }

    private void assertThrows(Class<? extends Throwable> expectedType, Runnable runnable) {
        try {
            runnable.run();
            fail("Expected throw of type " + expectedType);
        } catch (Throwable throwable) {
            if (expectedType.isInstance(throwable)) {
                return;
            }
            throw new AssertionError("Unexpected throw: " + throwable);
        }
    }

    @Test
    public void testToCharacterKey() {
        Properties properties = new Properties();
        Map<String, Character> entities = new HashMap<>();
        properties.put("A", (char) 0x0041);
        properties.put("B", (char) 0x0042);
        try {
            InputStream in = Entities.class.getResourceAsStream("/entities-test.properties");
            properties.load(in);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Map<Character, String> outEntities = Entities.toCharacterKey(properties);
        assertEquals(entities.size(), outEntities.size());
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            Character character = entry.getValue();
            String name = entry.getKey();
            assertEquals(name, outEntities.get(character));
        }
    }

    @Test
    public void testToCharacterKeyWithEmptyProperties() {
        Properties properties = new Properties();
        Map<Character, String> outEntities = Entities.toCharacterKey(properties);
        assertEquals(0, outEntities.size());
    }
}