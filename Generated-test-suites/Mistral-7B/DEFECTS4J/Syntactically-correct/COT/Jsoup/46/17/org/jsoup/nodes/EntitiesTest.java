import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import java.util.Map;
import java.util.HashMap;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Named entity " + name + " should be known", Entities.isNamedEntity(name));
            assertEquals("Named entity " + name + " should map to character " + character, character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> base = Entities.base;
        for (Map.Entry<String, Character> entry : base.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Base named entity " + name + " should be known", Entities.isBaseNamedEntity(name));
            assertEquals("Base named entity " + name + " should map to character " + character, character, Entities.getCharacterByName(name));
        }

        // check for entities not in base set
        assertFalse("lt should not be a base named entity", Entities.isBaseNamedEntity("lt"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals("Named entity " + name + " should map to character " + character, character, Entities.getCharacterByName(name));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNamedEntity() {
        assertFalse("Invalid named entity should not be known", Entities.isNamedEntity("invalid"));
        try {
            Entities.getCharacterByName("invalid");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEscapeWithDefaultSettings() {
        String input = "&test& < > \" ' &amp; &lt; &gt; &quot;";
        String expectedOutput = "&amp;amp;&amp;lt;&amp;gt;&amp;quot;&amp;#032; &amp;#038; &amp;#03C; &amp;#03E; &amp;quot;";
        String output = Entities.escape(input, null);
        assertEquals("Escaped output should match expected output", expectedOutput, output);
    }

    @Test
    public void testEscapeWithXhtmlSettings() {
        String input = "&test& < > \" ' &amp; &lt; &gt; &quot;";
        String expectedOutput = "&amp; &lt; &gt; &quot;";
        String output = Entities.escape(input, Entities.EscapeMode.xhtml);
        assertEquals("Escaped output should match expected output", expectedOutput, output);
    }

    @Test
    public void testEscapeWithCustomSettings() {
        String input = "&test& < > \" ' &amp; &lt; &gt; &quot;";
        String expectedOutput = "&amp;amp; &amp;lt; &amp;gt; &amp;quot; &amp;#032; &amp;#038; &amp;#03C; &amp;#03E; &amp;#034;";
        String output = Entities.escape(input, new Entities.EscapeMode("custom", new HashMap<Character, String>() {{
            put('<', "&lt;");
            put('>', "&gt;");
            put('&', "&amp;");
            put('"', "&quot;");
            put('\'', "&apos;");
        }}));
        assertEquals("Escaped output should match expected output", expectedOutput, output);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        String input = null;
        try {
            Entities.escape(input, null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testUnescapeWithDefaultSettings() {
        String input = "&amp;amp;&amp;lt;&amp;gt;&amp;quot;&#032; &amp;#038; &amp;#03C; &amp;#03E; &amp;quot;";
        String expectedOutput = "&test& < > \" ' &amp; &lt; &gt; &quot;";
        String output = Entities.unescape(input);
        assertEquals("Unescaped output should match expected output", expectedOutput, output);
    }

    @Test
    public void testUnescapeWithStrictSettings() {
        String input = "&amp;amp;&amp;lt;&amp;gt;&amp;quot;&#032; &amp;#038; &amp;#03C; &amp;#03E; &amp;quot;";
        String expectedOutput = "&test&< >\"'&amp; &lt;&gt;&quot;";
        String output = Entities.unescape(input, true);
        assertEquals("Unescaped output should match expected output", expectedOutput, output);
    }

    @Test
    public void testUnescapeWithInvalidEntities() {
        String input = "&invalid &amp;invalid &lt;invalid &gt;invalid &quot;invalid &apos;invalid &amp;#invalid";
        String expectedOutput = "&invalid &amp; #00007F &lt;invalid &gt;invalid &quot;invalid &apos; &amp;#FFFD";
        String output = Entities.unescape(input);
        assertEquals("Unescaped output should match expected output", expectedOutput, output);
    }
}