package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.helper.StringUtil;

import java.util.Map;
import java.util.Properties;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        for (Map.Entry<Character, String> entry : Entities.full.entrySet()) {
            assertTrue(Entities.isNamedEntity(entry.getValue()));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        for (Map.Entry<Character, String> entry : Entities.base.entrySet()) {
            assertTrue(Entities.isBaseNamedEntity(entry.getValue()));
        }

        assertFalse(Entities.isBaseNamedEntity("lt")); // not in base set
    }

    @Test
    public void testGetCharacterByName() {
        for (Map.Entry<Character, String> entry : Entities.full.entrySet()) {
            assertEquals(entry.getKey(), Entities.getCharacterByName(entry.getValue()));
        }

        assertNull(Entities.getCharacterByName("unknown")); // unknown entity
    }

    @Test
    public void testEscape() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.base);

        String input = "&lt;foo bar=&quot;baz&amp;&quot;&gt;";
        String expected = "&amp;lt;foo bar=&amp;#x22;baz&amp;amp;&#x22;gt;";
        String output = Entities.escape(input, outputSettings);

        assertEquals(expected, output);

        outputSettings.escapeMode(Entities.EscapeMode.xhtml);

        input = "&lt;foo bar=&quot;baz&amp;&quot;&gt;";
        expected = "&lt;foo bar=&quot;baz&quot;&gt;";
        output = Entities.escape(input, outputSettings);

        assertEquals(expected, output);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;foo bar=&amp;#x22;baz&amp;amp;&#x22;gt;";
        String expected = "&lt;foo bar=\"baz\"&gt;";

        String output = Entities.unescape(input);

        assertEquals(expected, output);
    }

    @Test
    public void testCanEncode() {
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, 'a'));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, '☕'));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.ascii, 'A'));
        assertEquals(false, Entities.canEncode(Entities.CoreCharset.ascii, '€'));
    }
}