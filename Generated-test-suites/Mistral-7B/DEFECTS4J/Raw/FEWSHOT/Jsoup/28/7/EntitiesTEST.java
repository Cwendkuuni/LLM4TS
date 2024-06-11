package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

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
        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            String key = entry.getKey();
            Character value = entry.getValue();
            assertTrue("Named entity \"" + key + "\" should be recognized", Entities.isNamedEntity(key));
            assertEquals("Named entity \"" + key + "\" maps to character " + Integer.toHexString(value.charId()), value, Entities.getCharacterByName(key));
        }

        assertFalse("Unknown named entity should not be recognized", Entities.isNamedEntity("nonexistent"));
    }

    @Test
    public void testGetCharacterByName() {
        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            String key = entry.getKey();
            Character value = entry.getValue();
            assertEquals("Named entity \"" + key + "\" maps to character " + Integer.toHexString(value.charId()), value, Entities.getCharacterByName(key));
        }

        assertNull("Unknown named entity should return null", Entities.getCharacterByName("nonexistent"));
    }

    @Test
    public void testEscape() {
        String text = "This text contains &amp; <lt>angle&gt; < & quote & 'apos; > entities.";
        String xhtml = Entities.escape(text, null, Entities.EscapeMode.xhtml);
        String base = Entities.escape(text, null, Entities.EscapeMode.base);
        String extended = Entities.escape(text, null, Entities.EscapeMode.extended);

        assertEquals("xhtml output:", "<amp>&lt;angle&gt;</amp>&apos; &quot;", xhtml);
        assertEquals("base output:", "&amp;lt;angle&gt;&apos; &quot;", base);
        assertEquals("extended output:", "&amp;#60;angle&gt;&#39; &quot;", extended);
    }

    @Test
    public void testUnescape() {
        String xhtml = "<amp>&lt;angle&gt;</amp>&apos; &quot;";
        String base = "&amp;lt;angle&gt;&apos; &quot;";
        String extended = "&amp;#60;angle&gt;&#39; &quot;";

        assertEquals("xhtml input, xhtml output:", "&lt;angle&gt;&apos; \"", Entities.unescape(xhtml, true));
        assertEquals("xhtml input, base output:", "&lt;angle&gt;&apos; \"", Entities.unescape(xhtml, false));
        assertEquals("xhtml input, extended output:", "&lt;angle&gt;&apos; \"", Entities.unescape(extended, true));
        assertEquals("xhtml input, extended output:", "&lt;angle&gt;&apos; \"", Entities.unescape(extended, false));

        assertEquals("base input, xhtml output:", "&lt;angle&gt;&apos; \"", Entities.unescape(xhtml, true));
        assertEquals("base input, base output:", "&lt;angle&gt;&apos; \"", Entities.unescape(base, false));
        assertEquals("base input, extended output:", "&lt;angle&gt;&apos; \"", Entities.unescape(extended, true));
        assertNull("base input, no entities output:", Entities.unescape("", false));

        assertEquals("extended input, xhtml output:", "&lt;angle&gt;&apos; \"", Entities.unescape(xhtml, true));
        assertEquals("extended input, base output:", "&lt;angle&gt;&apos; \"", Entities.unescape(base, true));
        assertEquals("extended input, extended output:", "&lt;angle&gt;&apos; \"", Entities.unescape(extended, false));
        assertEquals("extended input, extended output (unicode):", "＜angle＞‘ ", Entities.unescape(extended, true), "unicode");
    }

    @Test
    public void testLoadEntities() throws IOException {
        Properties properties = new Properties();
        Map<String, Character> map = new HashMap<String, Character>();

        InputStream stream = Entities.class.getResourceAsStream("/entities-base.properties");
        properties.load(stream);
        stream.close();

        for (Map.Entry<String, Character> entry : Entities.base.entrySet()) {
            String key = entry.getKey();
            Character value = entry.getValue();
            assertEquals("base entity \"" + key + "\" maps to character " + Integer.toHexString(value.charId()), value, map.get(key));
            map.put(key, value);
        }

        assertEquals("base entity count:", map.size(), Entities.base.size());
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> properties = new HashMap<String, Character>();
        properties.put("a", 'a');
        properties.put("A", 'A');
        properties.put("0x61", 'a');
        properties.put("0X61", 'a');

        Map<Character, String> map = Entities.toCharacterKey(properties);

        assertEquals("map size:", map.size(), properties.size());
        for (Map.Entry<String, Character> entry : properties.entrySet()) {
            String key = entry.getKey();
            Character value = entry.getValue();
            Character mappedValue = map.get(value);
            assertEquals("Key \"{0}\" should map to value {1}", key, mappedValue);
            assertEquals("Value {0} should map to key \"{1}\"", value, key);
        }
    }

    private void assertTrue(boolean condition, String message) {
        if (!condition) {
            fail(message);
        }
    }
}