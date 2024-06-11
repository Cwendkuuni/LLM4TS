import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue(Entities.isNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> base = Entities.baseByVal;
        for (Map.Entry<Character, String> entry : base.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue();
            assertTrue(Entities.isBaseNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }

        // test for non-base named entities
        assertFalse(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("gt"));
    }

    @Test
    public void testEscapeMethodWithDefaultSettings() {
        String input = "&amp;<lt>Hello & World&gt; <quote>Hello & World<quote>";
        String expectedOutput = "&amp;amp; & &lt; &lt;lt&gt;Hello &amp; & World &gt; &gt;<quote> &amp; &amp;World &amp;quot;";

        String output = Entities.escape(input, null);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeMethodWithXHTMLSettings() {
        String input = "&amp;<lt>Hello & World&gt; <quote>Hello & World<quote>";
        String expectedOutput = "&amp;amp; & &lt; &lt;lt&gt;Hello &amp; & World &gt; &gt; &quot;";

        Document.OutputSettings xhtmlSettings = new Document.OutputSettings();
        xhtmlSettings.escapeMode(Entities.EscapeMode.xhtml);

        String output = Entities.escape(input, xhtmlSettings);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodWithInvalidEscapeMode() {
        String input = "&amp;<lt>Hello & World&gt; <quote>Hello & World<quote>";
        Document.OutputSettings invalidSettings = new Document.OutputSettings();
        invalidSettings.escapeMode("invalidMode");

        Entities.escape(input, invalidSettings);
    }

    @Test
    public void testUnescapeMethod() {
        String input = "&amp;amp; & &lt; &lt;lt&gt;Hello &amp; & World &gt; &gt; &quot;";
        String expectedOutput = "&amp;<lt>Hello &amp; World&gt; <quote>Hello &amp; World<quote>";

        String output = Entities.unescape(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCanEncodeMethod() {
        for (CoreCharset charset : CoreCharset.values()) {
            for (char c = 0; c < Character.MAX_VALUE; c++) {
                if (charset == CoreCharset.ascii) {
                    assertTrue(Entities.canEncode(charset, c, null));
                } else {
                    try {
                        InputStream inputStream = Entities.class.getResourceAsStream("/resources/encodings/" + charset.name() + ".properties");
                        CharsetEncoder encoder = Charset.forName(charset.name()).newEncoder();
                        assertTrue(Entities.canEncode(charset, c, encoder));
                        inputStream.close();
                    } catch (IOException e) {
                        fail("Failed to load encoding properties file: " + e.getMessage());
                    }
                }
            }
        }
    }
}