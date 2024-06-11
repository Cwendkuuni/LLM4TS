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
            assertTrue("Named entity " + name + " is not recognized", Entities.isNamedEntity(name));
            assertEquals("Named entity " + name + " returns incorrect character", character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> base = Entities.base;
        for (Map.Entry<String, Character> entry : base.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Base named entity " + name + " is not recognized", Entities.isBaseNamedEntity(name));
            assertEquals("Base named entity " + name + " returns incorrect character", character, Entities.getCharacterByName(name));
        }

        String invalidName = "invalid";
        assertFalse("Invalid named entity " + invalidName + " is recognized", Entities.isNamedEntity(invalidName));
        assertFalse("Invalid named entity " + invalidName + " is recognized as base", Entities.isBaseNamedEntity(invalidName));
    }

    @Test
    public void testEscapeMethodWithDefaultSettings() {
        String input = "Hello <world>! &éçtéñg à éáé!";
        String expectedOutput = "Hello &lt;world&gt;! &amp;#xC3;#xB4;#xC3;#xA9;#xC3;#xA7; #xC3;#xA1;#xC3;#xA9;#xC3;#xA8;";
        String output = Entities.escape(input, Document.OutputSettings.defaultOutputSettings());
        assertEquals("Escaped output does not match expected output", expectedOutput, output);
    }

    @Test
    public void testEscapeMethodWithXHTMLSettings() {
        String input = "Hello <world>! &éçtéñg à éáé!";
        String expectedOutput = "Hello &lt;world&gt;! &amp;#xA0;#x26;#x22;#x3C3;#xA0;#xA2;#xC3;#xA1;#xC3;#xA9;#xC3;#xA8;";
        String output = Entities.escape(input, Document.OutputSettings.xhtmlOutputSettings());
        assertEquals("Escaped output with XHTML settings does not match expected output", expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodWithNullString() {
        Entities.escape(null, Document.OutputSettings.defaultOutputSettings());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodWithNullOutputSettings() {
        String input = "Hello <world>! &éçtéñg à éáé!";
        Entities.escape(input, null);
    }

    @Test
    public void testUnescapeMethodWithValidString() {
        String input = "Hello &lt;world&gt;! &amp;#xA0;#x26;#x22;#x3C3;#xA0;#xA2;#xC3;#xA1;#xC3;#xA9;#xC3;#xA8;";
        String expectedOutput = "Hello <world>! &lt;world&gt;!";
        String output = Entities.unescape(input, false);
        assertEquals("Unescaped output does not match expected output", expectedOutput, output);
    }

    @Test
    public void testUnescapeMethodWithStrictSettings() {
        String input = "Hello &lt;world&gt;! &amp;#xA0;#x26;#x22;#x3C3;#xA0;#xA2;#xC3;#xA1;#xC3;#xA9;#xC3;#xA8;";
        String expectedOutput = "Hello <world>! &lt;world&gt;!";
        String output = Entities.unescape(input, true);
        assertEquals("Unescaped output with strict settings does not match expected output", expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeMethodWithNullString() {
        Entities.unescape(null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeMethodWithNullBoolean() {
        Entities.unescape(null, false);
    }
}