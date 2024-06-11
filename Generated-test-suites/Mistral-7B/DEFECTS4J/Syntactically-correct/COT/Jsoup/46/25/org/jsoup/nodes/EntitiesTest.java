package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Entities;
import org.jsoup.parser.Parser;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        Map<String, Character> full = entities.getFullMap();

        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue(entities.isNamedEntity(name));
            assertEquals(character, entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        Map<String, Character> base = entities.getBaseMap();

        for (Map.Entry<String, Character> entry : base.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue(entities.isBaseNamedEntity(name));
            assertEquals(character, entities.getCharacterByName(name));
        }

        String notNamedEntity = "notNamedEntity";
        assertFalse(entities.isNamedEntity(notNamedEntity));
        assertFalse(entities.isBaseNamedEntity(notNamedEntity));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();

        assertEquals('<', entities.getCharacterByName("lt"));
        assertEquals('&', entities.getCharacterByName("amp"));
        assertEquals('>', entities.getCharacterByName("gt"));
        assertEquals('"', entities.getCharacterByName("quot"));

        String notNamedEntity = "notNamedEntity";
        assertNull(entities.getCharacterByName(notNamedEntity));
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();

        String input = "&lt;abc&gt;def&quot;<>&amp;";
        Document.OutputSettings out = new Document.OutputSettings();
        String expected = "&amp;lt;abc&amp;gt;def&amp;quot;&amp;lt;&amp;gt;&amp;amp;";
        String output = entities.escape(input, out);

        assertEquals(expected, output);
    }

    @Test
    public void testEscapeWithXHTML() {
        Entities entities = new Entities();
        Document.OutputSettings xhtmlOut = new Document.OutputSettings(Entities.EscapeMode.xhtml);
        String input = "&lt;abc&gt;def&quot;<>&amp;";
        String expected = "&lt;abc&gt;def&quot;<>&amp;";
        String output = entities.escape(input, xhtmlOut);

        assertEquals(expected, output);
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();

        String input = "&amp;lt;abc&amp;gt;def&amp;quot;&amp;lt;&amp;gt;&amp;amp;";
        String output = entities.unescape(input);

        assertEquals("&lt;abc&gt;def&quot;<>&amp;", output);
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();
        CoreCharset ascii = Entities.CoreCharset.ascii;
        CoreCharset utf = Entities.CoreCharset.utf;

        for (char c = 0; c < 128; c++) {
            if (entities.canEncode(ascii, c)) {
                assertTrue(entities.canEncode(utf, c));
            } else {
                assertFalse(entities.canEncode(utf, c));
            }
        }

        char nonASCII = '\u00E9';
        assertFalse(entities.canEncode(Entities.CoreCharset.ascii, nonASCII));
        assertTrue(entities.canEncode(Entities.CoreCharset.utf, nonASCII));
    }

}