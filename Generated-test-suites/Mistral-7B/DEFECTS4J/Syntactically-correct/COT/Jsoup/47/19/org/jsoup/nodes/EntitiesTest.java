import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import java.util.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        String[] names = {"lt", "amp", "quot"};
        for (String name : names) {
            assertTrue(entities.isNamedEntity(name));
        }
        String unknownName = "unknown";
        assertFalse(entities.isNamedEntity(unknownName));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        String[] names = {"lt", "amp", "quot"};
        for (String name : names) {
            assertTrue(entities.isBaseNamedEntity(name));
        }
        String unknownName = "gt";
        assertFalse(entities.isBaseNamedEntity(unknownName));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        String[] names = {"lt", "amp", "quot"};
        Character[] expected = {'<', '&', '\''};
        for (int i = 0; i < names.length; i++) {
            Character actual = entities.getCharacterByName(names[i]);
            assertEquals(expected[i], actual);
        }
    }

    @Test
    public void testEscapeMethodWithDefaultSettings() {
        Entities entities = new Entities();
        String input = "&lt;<hello&amp;world>&quot;";
        String expectedOutput = "&amp;lt;&amp;lt;<amp>&amp;amp;world;&amp;quot;";
        String output = entities.escape(input, null);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeMethodWithXHTMLSettings() {
        Entities entities = new Entities();
        String input = "&lt;<hello&amp;world>&quot;";
        String expectedOutput = "&lt;&lt;<amp>&amp;amp;world;&quot;";
        Document.OutputSettings xhtmlSettings = new Document.OutputSettings();
        xhtmlSettings.setEscapeMode(Entities.EscapeMode.xhtml);
        String output = entities.escape(input, xhtmlSettings);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodWithNullInput() {
        Entities entities = new Entities();
        String input = null;
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        entities.escape(input, outputSettings);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = NullPointerException.class)
    public void testEscapeMethodWithNullOutputSettings() {
        Entities entities = new Entities();
        String input = "test";
        Document.OutputSettings outputSettings = null;
        try {
            entities.escape(input, outputSettings);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException, expected NullPointerException");
        }
        Document.OutputSettings validOutputSettings = new Document.OutputSettings();
        entities.escape(input, null);

        outputSettings = new Document.OutputSettings();
        entities.escape(input, outputSettings);

        fail("Expected NullPointerException");
    }

    @Test
    public void testCanEncodeMethod() {
        Entities entities = new Entities();
        CoreCharset ascii = Entities.CoreCharset.ascii;
        CoreCharset utf = Entities.CoreCharset.utf;
        CharsetEncoder fallback = null;

        // Test with ASCII characters
        for (char c = 0; c < Character.MIN_VALUE; c++) {
            assertTrue(entities.canEncode(ascii, c, fallback));
        }

        // Test with non-ASCII characters
        char nonAsciiChar = 0x00E1; // Á
        assertFalse(entities.canEncode(ascii, nonAsciiChar, fallback));
        assertTrue(entities.canEncode(utf, nonAsciiChar, fallback));
    }

    @Test
    public void testLoadEntitiesMethod() {
        Entities entities = new Entities();
        Map<String, Character> expectedEntities = entities.full;
        Map<String, Character> loadedEntities = entities.loadEntities("entities-full.properties");
        assertEquals(expectedEntities.size(), loadedEntities.size());
        for (Map.Entry<String, Character> entry : expectedEntities.entrySet()) {
            Character character = entry.getValue();
            String name = entry.getKey();
            Character loadedCharacter = loadedEntities.get(name);
            assertEquals(character, loadedCharacter);
        }
    }
}