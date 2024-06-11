package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.nio.charset.CharsetEncoder;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        String input = "<div>Test</div>";
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);
        String expectedOutput = "&lt;div&gt;Test&lt;/div&gt;";

        String output = Entities.escape(input, out);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        String input = "&lt;div&gt;Test&lt;/div&gt;";
        String expectedOutput = "<div>Test</div>";

        String output = Entities.unescape(input);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescapeStrict() {
        String input = "&lt;div&gt;Test&lt;/div&gt;";
        String expectedOutput = "<div>Test</div>";

        String output = Entities.unescape(input, true);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeInAttribute() {
        String input = "<div class=\"&lt;test&gt;\">Test</div>";
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);
        String expectedOutput = "<div class=\"&amp;lt;test&amp;gt;\">Test</div>";

        String output = Entities.escape(input, out);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeNormaliseWhite() {
        String input = "<div>\tTest\n</div>";
        Document.OutputSettings out = new Document.OutputSettings().prettyPrint(false);
        String expectedOutput = "<div>Test</div>";

        String output = Entities.escape(input, out);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeStripLeadingWhite() {
        String input = "<div> \tTest\n</div>";
        Document.OutputSettings out = new Document.OutputSettings().prettyPrint(false);
        String expectedOutput = "<div>Test</div>";

        String output = Entities.escape(input, out);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCanEncode() {
        CoreCharset charset = CoreCharset.utf;
        CharsetEncoder fallback = CharsetEncoder.defaultCharsetEncoder();
        assertTrue(Entities.canEncode(charset, 'a', fallback));
        assertFalse(Entities.canEncode(charset, '€', fallback));
    }

    @Test
    public void testLoadEntities() {
        String filename = "entities-base.properties";
        Map<String, Character> entities = Entities.loadEntities(filename);

        assertEquals(252, entities.size());
        assertTrue(entities.containsKey("lt"));
        assertTrue(entities.containsValue('<'));
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = Entities.loadEntities("entities-full.properties");
        Map<Character, String> outMap = Entities.toCharacterKey(inMap);

        assertEquals(252, outMap.size());
        assertTrue(outMap.containsKey('<'));
        assertTrue(outMap.containsValue("lt"));
    }
}