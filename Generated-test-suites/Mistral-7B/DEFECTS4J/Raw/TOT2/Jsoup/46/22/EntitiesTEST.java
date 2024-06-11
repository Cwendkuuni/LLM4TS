import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<Character, String> full = Entities.full;
        for (Map.Entry<Character, String> entry : full.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            assertTrue(Entities.isNamedEntity(value));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> base = Entities.base;
        for (Map.Entry<String, Character> entry : base.entrySet()) {
            String key = entry.getKey();
            Character value = entry.getValue();
            assertTrue(Entities.isBaseNamedEntity(key));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, String> full = Entities.full;
        for (Map.Entry<Character, String> entry : full.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            assertEquals(key, Entities.getCharacterByName(value));
        }
    }

    @Test
    public void testEscapeMethodBasicUsage() {
        String input = "Hello & World!";
        String output = Entities.escape(input, null);
        String expectedOutput = "Hello &amp; World!";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeMethodXhtml() {
        String input = "Hello & World!";
        Document.OutputSettings settings = new Document.OutputSettings(Entities.Entities.extended);
        String output = Entities.escape(input, settings);
        String expectedOutput = "Hello &amp;amp; World!";
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodNullInput() {
        String input = null;
        Document.OutputSettings settings = new Document.OutputSettings(Entities.Entities.extended);
        Entities.escape(input, settings);
    }

    @Test(expected = NullPointerException.class)
    public void testEscapeMethodNullSettings() {
        String input = "Hello & World!";
        Document.OutputSettings settings = null;
        try {
            Entities.escape(input, settings);
        } catch (IllegalArgumentException e) {
            throw new AssertionError("Expected NullPointerException");
        }
    }

    @Test
    public void testUnescapeMethodBasicUsage() {
        String input = "Hello &amp; World!";
        String output = Entities.unescape(input, false);
        String expectedOutput = "Hello & World!";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescapeMethodStrict() {
        String input = "Hello &amp; World!";
        String output = Entities.unescape(input, true);
        String expectedOutput = "Hello &amp;amp; World!";
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeMethodNullInput() {
        String input = null;
        boolean strict = false;
        try {
            Entities.unescape(input, strict);
        } catch (NullPointerException e) {
            throw new AssertionError("Expected IllegalArgumentException");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeMethodNullStrict() {
        String input = "Hello &amp; World!";
        boolean strict = null;
        try {
            Entities.unescape(input, strict);
        } catch (IllegalArgumentException e) {
            throw new AssertionError("Expected IllegalArgumentException");
        }
    }
}