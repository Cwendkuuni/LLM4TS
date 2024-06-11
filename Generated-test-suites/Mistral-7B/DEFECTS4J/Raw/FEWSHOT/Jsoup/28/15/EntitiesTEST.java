package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
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
            Character value = entry.getValue();
            assertTrue("Named entity " + name + " not found", Entities.isNamedEntity(name));
            assertEquals("Named entity " + name + " has wrong value", value, Entities.getCharacterByName(name));
        }

        assertFalse("Unknown named entity", Entities.isNamedEntity("nonexistent"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> fullEntities = Entities.full;
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String name = entry.getKey();
            Character value = entry.getValue();
            assertEquals("Named entity " + name + " has wrong value", value, Entities.getCharacterByName(name));
        }

        assertNull("Unexpected named entity value", Entities.getCharacterByName("nonexistent"));
    }

    @Test
    public void testEscape() {
        String source = "&lt; &gt; &amp; &apos; &quot; &entityRef1; &entityRef2; &#x20ac; &#10; &#x1F345;";

        for (Entities.EscapeMode mode : Entities.EscapeMode.values()) {
            StringBuilder expected = new StringBuilder();
            Map<Character, String> map = mode.getMap();

            for (int i = 0; i < source.length(); i++) {
                char c = source.charAt(i);
                if (map.containsKey(c)) {
                    expected.append("&").append(map.get(c)).append(';');
                } else {
                    expected.append(c);
                }
            }

            String result = Entities.escape(source, null, mode);
            assertEquals("Escape mode " + mode + " produces wrong output for input " + source, expected.toString(), result);
        }
    }

    @Test
    public void testUnescape() {
        String source = "&lt; &gt; &amp; &apos; &quot; &#x20ac; &#10; &#x1F345; &lt;!DOCTYPE html&gt; &amp;lt;script&gt;alert(&#x61;&#x6C;&#x65;&#x72;&#x73;&#x41;&#x75;&#x74;&#x54;&#x68;&#x65;&#x2032;&#x2026;&#x2191;&#x2193;&#x2192;&#x2190;&#x2212;&#x00B0;&#xA0;";

        String result = Entities.unescape(source);
        assertEquals("Input " + source + " unescaped to " + result, "<!DOCTYPE html><script>alert(abc...<>♦‾⁴⁰ )", result);
    }

    @Test
    public void testXhtmlEntities() {
        Map<Character, String> xhtmlMap = Entities.EscapeMode.xhtml.getMap();
        assertEquals("xhtml does not contain quot", xhtmlMap.get("quot"), "&quot;");
        assertEquals("xhtml does not contain amp", xhtmlMap.get("amp"), "&amp;");
        assertEquals("xhtml does not contain apos", xhtmlMap.get("apos"), "&apos;");
        assertEquals("xhtml does not contain lt", xhtmlMap.get("lt"), "&lt;");
        assertEquals("xhtml does not contain gt", xhtmlMap.get("gt"), "&gt;");

        String source = "&quot; &amp; &apos; &lt; &gt;";
        StringBuilder expected = new StringBuilder();
        Map<Character, String> xhtmlMapExpected = new HashMap<Character, String>(xhtmlMap);
        xhtmlMapExpected.remove("amp");

        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            if (xhtmlMapExpected.containsKey(c)) {
                expected.append(xhtmlMapExpected.get(c));
            } else {
                expected.append(c);
            }
        }

        String result = Entities.escape(source, null, Entities.EscapeMode.xhtml);
        assertEquals("xhtml entities produce wrong output for input " + source, expected.toString(), result);
    }

    @Test
    public void testLoadEntities() {
        Properties properties = new Properties();
        try {
            InputStream stream = Entities.class.getResourceAsStream("/entities-base.properties");
            properties.load(stream);
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load entities file", e);
        }

        Map<String, Character> entities = Entities.loadEntities("/entities-base.properties");
        assertEquals("entities-base.properties should contain &apos; entity", Character.valueOf('\''), entities.get("apos"));
        assertEquals("entities-base.properties should contain &lt; entity", Character.valueOf('<'), entities.get("lt"));
        assertEquals("entities-base.properties should contain &gt; entity", Character.valueOf('>'), entities.get("gt"));
        assertEquals("entities-base.properties should contain &quot; entity", Character.valueOf('"'), entities.get("quot"));
        assertEquals("entities-base.properties should contain &amp; entity", Character.valueOf('&'), entities.get("amp"));

        Map<Character, String> reverseEntities = Entities.toCharacterKey(entities);
        assertEquals("entities-base.properties should contain 'apos' entity named &apos;", "apos", reverseEntities.get(Character.valueOf('\'')));
        assertEquals("entities-base.properties should contain 'lt' entity named &lt;", "lt", reverseEntities.get(Character.valueOf('<')));
        assertEquals("entities-base.properties should contain 'gt' entity named &gt;", "gt", reverseEntities.get(Character.valueOf('>')));
        assertEquals("entities-base.properties should contain 'quot' entity named &quot;", "quot", reverseEntities.get(Character.valueOf('"')));
        assertEquals("entities-base.properties should contain 'amp' entity named &amp;", "amp", reverseEntities.get(Character.valueOf('&')));
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> entities = new HashMap<String, Character>() {{
            put("apos", '\'');
            put("lt", '<');
            put("gt", '>');
            put("quot", '"');
            put("amp", '&');
        }};

        Map<Character, String> reverseEntities = Entities.toCharacterKey(entities);
        assertEquals("reverseEntities should contain 'apos' entity named &apos;", "apos", reverseEntities.get(Character.valueOf('\'')));
        assertEquals("reverseEntities should contain 'lt' entity named &lt;", "lt", reverseEntities.get(Character.valueOf('<')));
        assertEquals("reverseEntities should contain 'gt' entity named &gt;", "gt", reverseEntities.get(Character.valueOf('>')));
        assertEquals("reverseEntities should contain 'quot' entity named &quot;", "quot", reverseEntities.get(Character.valueOf('"')));
        assertEquals("reverseEntities should contain 'amp' entity named &amp;", "amp", reverseEntities.get(Character.valueOf('&')));

        Map<Character, String> reverseEntities2 = Entities.toCharacterKey(entities);
        assertSame("reverseEntities and reverseEntities2 should be the same", reverseEntities, reverseEntities2);
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            fail(message);
        }
    }
}