package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Parser;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test file for Entities class.
 */
public class EntitiesTest {

    private static Map<String, Character> full;
    private static Map<Character, String> xhtmlByVal;
    private static Map<Character, String> base;
    private static Map<Character, String> baseByVal;
    private static Map<Character, String> fullByVal;

    @BeforeClass
    public static void setUpClass() {
        Entities.full = loadEntities("entities-full.properties");
        Entities.xhtmlByVal = new HashMap<>();
        Entities.base = loadEntities("entities-base.properties");
        Entities.baseByVal = toCharacterKey(Entities.base);
        Entities.fullByVal = toCharacterKey(Entities.full);

        // Add xhtml entities
        Arrays.stream(Entities.xhtmlArray).forEach(entity -> {
            Character c = Character.valueOf((char) ((Integer) secondElement(entity))
                    .intValue());
            Entities.xhtmlByVal.put(c, ((String) firstElement(entity)).trim());
        });
    }

    private static Object[] secondElement(Object[] array) {
        return array[1];
    }

    private static Object[] firstElement(Object[] array) {
        return array[0];
    }

    @Test
    public void testIsNamedEntity() {
        for (Map.Entry<Character, String> entity : Entities.full.entrySet()) {
            assertTrue(Entities.isNamedEntity(entity.getValue()));
        }

        for (Map.Entry<Character, String> entity : Entities.base.entrySet()) {
            assertTrue(Entities.isNamedEntity(entity.getValue()));
        }

        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity("abc"));
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testIsBaseNamedEntity() {
        for (Map.Entry<Character, String> entity : Entities.base.entrySet()) {
            assertTrue(Entities.isBaseNamedEntity(entity.getValue()));
        }

        for (Map.Entry<Character, String> entity : Entities.full.entrySet()) {
            if (!Entities.isBaseNamedEntity(entity.getValue())) {
                assertFalse(Entities.isBaseNamedEntity(entity.getValue()));
            }
        }

        assertFalse(Entities.isBaseNamedEntity(""));
        assertFalse(Entities.isBaseNamedEntity("abc"));
        assertFalse(Entities.isBaseNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        for (Map.Entry<Character, String> entity : Entities.full.entrySet()) {
            assertEquals(entity.getKey(), Entities.getCharacterByName(entity.getValue()));
        }

        for (Map.Entry<Character, String> entity : Entities.base.entrySet()) {
            assertEquals(entity.getKey(), Entities.getCharacterByName(entity.getValue()));
        }

        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName("abc"));
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscape() {
        StringBuilder accum = new StringBuilder();

        String input = "&lt;!DOCTYPE html&gt;&#32; &lt; &quot; &amp; &gt;";

        Entities.escape(accum, input, Document.OutputSettings.base(), false, false, false);

        String expected = "&amp;lt;!DOCTYPE html&gt;&#xA0; &amp;lt; &amp;quot; &amp;amp; &amp;gt;";
        assertEquals(expected, accum.toString());

        accum.setLength(0);

        Entities.escape(accum, input, Document.OutputSettings.xhtml(), false, false, false);

        expected = "&lt;!DOCTYPE html&gt;&#xA0; &amp;lt; &amp;quot; &amp;amp; &amp;gt;";
        assertEquals(expected, accum.toString());

        accum.setLength(0);

        Entities.escape(accum, input, Document.OutputSettings.extended(), false, false, false);

        expected = "&lt;!DOCTYPE html&gt;&#xA0; &lt; &quot; &amp; &gt;";
        assertEquals(expected, accum.toString());

        accum.setLength(0);

        Entities.escape(accum, "test", Document.OutputSettings.base(), true, true, true);

        assertEquals("test", accum.toString());
    }

    @Test
    public void testEscapeWithAttributes() {
        StringBuilder accum = new StringBuilder();

        String xmlInput = "<foo bar='&lt; &quot; &amp;&#123;' baz='&gt; &apos;' />";

        Entities.escape(accum, xmlInput, Document.OutputSettings.base(), true, false, false);

        String expected = "'<foo bar='&amp;lt;' baz='&amp;gt;' />'";
        assertEquals(expected, accum.toString());

        accum.setLength(0);

        Entities.escape(accum, xmlInput, Document.OutputSettings.xhtml(), true, false, false);

        expected = "'<foo bar='&lt;' baz='&gt;' />'";
        assertEquals(expected, accum.toString());

        accum.setLength(0);

        Entities.escape(accum, xmlInput, Document.OutputSettings.extended(), true, false, false);

        expected = "'<foo bar='&lt;' baz='&gt;' />'";
        assertEquals(expected, accum.toString());

        accum.setLength(0);

        Entities.escape(accum, xmlInput, Document.OutputSettings.extended(), true, true, false);

        expected = "<foo bar='&lt;' baz='&gt;' />";
        assertEquals(expected, accum.toString());

        accum.setLength(0);

        Entities.escape(accum, xmlInput, Document.OutputSettings.extended(), true, true, true);

        expected = "<foo bar='&#x26;' baz='&#x26;gt;' />";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testEscapeWithWhiteSpace() {
        StringBuilder accum = new StringBuilder();

        String xmlInput = "<foo bar=' &lt; &quot; &amp;&#123;' baz=' &gt; &apos; ' />";

        Entities.escape(accum, xmlInput, Document.OutputSettings.base(), true, true, true);

        String expected = "<foo bar=' &#xA0;&#xA0; &amp;lt;' baz=' &#xA0;&#xA0; &amp;gt;' />";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;!DOCTYPE html&gt;&#xA0; &amp;lt; &amp;quot; &amp;amp; &amp;gt;";

        String expected = "&lt;!DOCTYPE html&gt;&#xA0; < &quot; &amp; &gt;";
        String result = Entities.unescape(input);
        assertEquals(expected, result);

        input = "&#x26;lt;!DOCTYPE html&#xA0; &lt; &quot; &amp; &gt;";

        result = Entities.unescape(input);
        assertEquals(expected, result);

        input = "&apos;<foo bar='&amp;lt; &amp;quot; &amp;amp; &amp;gt;' />";

        result = Entities.unescape(input, true);
        assertEquals("'<foo bar='< &quot; &amp; &gt;' />'", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithNull() {
        Entities.unescape(null);
    }

    @Test
    public void testCanEncode() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();

        for (Character c : Arrays.asList('A', 'a', '\u00C3', '\uD800')) {
            assertTrue(Entities.canEncode(Entities.CoreCharset.utf, c, encoder));
        }

        for (Character c : Arrays.asList('\u0304', '\uD840')) {
            assertFalse(Entities.canEncode(Entities.CoreCharset.utf, c, encoder));
        }
    }

    @Test
    public void testLoadEntities() throws IOException {
        Map<String, Character> entities = Entities.loadEntities("entities-full.properties");

        for (Map.Entry<Character, String> entity : Entities.full.entrySet()) {
            assertTrue(entities.containsKey(entity.getValue()));
            assertEquals(entity.getKey(), entities.get(entity.getValue()));
        }

        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            if (!Entities.full.containsKey(entry.getValue())) {
                assertFalse(Entities.isNamedEntity(entry.getValue()));
                assertTrue(Entities.isBaseNamedEntity(entry.getValue()));
            }
        }
    }

    @Test
    public void testLoadEntitiesWithEmptyPropertiesFile() throws IOException {
        assertThrows(MissingResourceException.class, () -> Entities.loadEntities("nonexistent.properties"));
    }

    @Test
    public void testToCharacterKey() {
        Map<Character, String> entities = new HashMap<>();
        entities.put("foo", 'A');
        entities.put("bar", 'B');

        Map<Character, String> expected = new HashMap<>();
        expected.put('A', "foo");
        expected.put('B', "bar");

        Map<Character, String> result = Entities.toCharacterKey(entities);

        assertEquals(expected, result);

        Map<Character, String> entities2 = new HashMap<>();
        entities2.put('A', "foo");
        entities2.put('B', "bar");

        Map<Character, String> expected2 = new HashMap<>();
        expected2.put('A', "foo");
        expected2.put('B', "bar");

        Map<Character, String> result2 = Entities.toCharacterKey(entities2);

        assertEquals(expected2, result2);
    }
}