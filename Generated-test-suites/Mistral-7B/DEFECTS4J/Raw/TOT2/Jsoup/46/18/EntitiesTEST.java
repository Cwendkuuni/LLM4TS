import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Named entity '" + name + "' should be known", Entities.isNamedEntity(name));
            assertEquals("Named entity '" + name + "' should map to character " + character, character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> base = Entities.base;
        for (Map.Entry<String, Character> entry : base.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Named entity '" + name + "' should be in base set", Entities.isBaseNamedEntity(name));
            assertEquals("Named entity '" + name + "' should map to character " + character, character, Entities.getCharacterByName(name));
        }

        assertFalse("Named entity 'foo' should not be in base set", Entities.isBaseNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, String> fullByVal = Entities.fullByVal;
        for (Map.Entry<Character, String> entry : fullByVal.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue();
            assertEquals("Character '" + character + "' should map to named entity '" + name + "'", name, Entities.getCharacterByName(name.toString()));
        }
    }

    @Test
    public void testEscape() {
        String input = "Hello & World! &lt; &gt; \" < > &amp;";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        StringBuilder expected = new StringBuilder();
        for (char c : input.toCharArray()) {
            switch (c) {
                case '&':
                    expected.append("&amp;");
                    break;
                case '<':
                    if (outputSettings.escapeMode() != Entities.EscapeMode.xhtml) {
                        expected.append("&lt;");
                    } else {
                        expected.append(c);
                    }
                    break;
                case '>':
                    if (outputSettings.escapeMode() != Entities.EscapeMode.xhtml) {
                        expected.append("&gt;");
                    } else {
                        expected.append(c);
                    }
                    break;
                case '"':
                    expected.append("&quot;");
                    break;
                default:
                    expected.append(c);
            }
        }

        String output = Entities.escape(input, outputSettings);
        assertEquals("Escaped string should be '" + expected + "'", expected.toString(), output);
    }

    @Test
    public void testEscapeEdgeCases() {
        String input = "& & < > \" <> &amp; &lt; &gt; &quot;";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setEscapeMode(Entities.EscapeMode.xhtml);

        String output = Entities.escape(input, outputSettings);
        assertEquals("Escaped XHTML string should be '" + ("&amp;&amp; &lt;&lt; < > &quot;&quot;") + "'", "&amp;&amp; &lt;&lt; < > &quot;&quot;", output);

        outputSettings.setEscapeMode(Entities.EscapeMode.base);
        output = Entities.escape(input, outputSettings);
        assertEquals("Escaped HTML string should be '" + ("&amp;amp; &lt;lt; &lt; &gt; &quot; &quot;") + "'", "&amp;amp; &lt;lt; &lt; &gt; &quot; &quot;", output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeInvalidSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String input = "Hello & World! &lt; &gt; \" < > &amp;";
        Entities.escape(input, outputSettings);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testUnescape() {
        String input = "&amp; &lt; &gt; &quot; &amp; &lt; &gt; &quot;";
        String output = Entities.unescape(input, false);
        assertEquals("Unescaped string should be '& & < > " + "\" & < > \"'", "& & < > \"", output);
    }

    @Test
    public void testUnescapeStrict() {
        String input = "&amp; &lt; &gt; &quot; &amp; &lt; &gt; &quot;";
        String output = Entities.unescape(input, true);
        assertEquals("Unescaped strict string should be '&amp; &amp;lt; &amp;lt; &amp;gt; &amp;quot; &amp;quot;'", "&amp;amp;&amp;lt;&amp;lt; &amp;lt;&amp;gt; &amp;quot;&amp;quot;", output);
    }

    @Test
    public void testCanEncode() {
        final CoreCharset ascii = CoreCharset.ascii;
        final CharsetEncoder encoder = Charset.forName("US-ASCII").newEncoder();

        for (char c = 0; c < 0x100; c++) {
            if (Entities.canEncode(ascii, c, encoder)) {
                assertTrue("Should be able to encode character '" + c + "'", encoder.canEncode(String.valueOf(c)));
            } else {
                assertFalse("Should not be able to encode character '" + c + "'", encoder.canEncode(String.valueOf(c)));
            }
        }
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = new HashMap<>();
        Map<String, Character> loadedEntities = Entities.loadEntities("entities-test.properties");
        entities.putAll(loadedEntities);

        String key;
        for (Map.Entry<Map.Entry<String, Character>, Integer> entry : Arrays.asList(entities.entrySet().toArray()).subList(0, 5)) {
            Map.Entry<String, Character> expected = entry.getKey();
            Map.Entry<String, Character> loaded = entry.getValue();
            key = expected.getKey();
            assertEquals("Loaded entity '" + key + "' should match expected entity '" + key + "'", expected.getValue(), loaded.getValue());
        }
    }

    @Test
    public void testToCharacterKey() {
        Map<Character, String> entities = new HashMap<>();
        Map<Character, String> keyedEntities = Entities.toCharacterKey(Entities.full);
        entities.putAll(keyedEntities);

        Character key;
        for (Map.Entry<Map.Entry<Character, String>, Integer> entry : Arrays.asList(entities.entrySet().toArray()).subList(0, 5)) {
            Map.Entry<Character, String> expected = entry.getKey();
            Map.Entry<Character, String> loaded = entry.getValue();
            key = expected.getKey();
            assertEquals("Loaded entity '" + key + "' should match expected entity '" + key + "'", expected.getValue(), loaded.getValue());
        }
    }
}