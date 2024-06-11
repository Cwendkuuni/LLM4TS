package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Entities;
import org.jsoup.helper.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        String[] names = {"lt", "amp", "quot", "gt", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp", "nbsp"};

        for (String name : names) {
            boolean result = entities.isNamedEntity(name);
            if (name.equals("nbsp")) {
                assertTrue(result);
                continue;
            }
            if (name.length() > 0) {
                assertTrue("Expected named entity '" + name + "' to be known", result);
            } else {
                assertFalse("Expected named entity to be unknown", result);
            }
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        String[] names = {"lt", "amp", "quot", "gt", "nbsp"};

        for (String name : names) {
            boolean result = entities.isBaseNamedEntity(name);
            if (name.equals("nbsp")) {
                assertFalse("Expected base named entity 'nbsp' to not be known", result);
                continue;
            }
            assertTrue("Expected base named entity '" + name + "' to be known", result);
        }
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        String[] names = {"lt", "amp", "quot", "gt", "nbsp"};
        Character[] expected = {'<', '&', '\"', '>', (char) 0xA0};

        for (int i = 0; i < names.length; i++) {
            Character result = entities.getCharacterByName(names[i]);
            assertEquals("Expected named entity '" + names[i] + "' to have character value '" + expected[i] + "'", expected[i], result);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNamedEntityWithNullName() {
        Entities entities = new Entities();
        entities.isNamedEntity(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharacterByNameWithNullName() {
        Entities entities = new Entities();
        entities.getCharacterByName(null);
    }

    @Test
    public void testEscapeWithNullString() {
        Entities entities = new Entities();
        StringBuilder actual = new StringBuilder();
        entities.escape(actual, null, null, false, false, false);
        assertEquals("Expected an empty string", "", actual.toString());
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        StringBuilder actual = new StringBuilder();
        String input = "&<>'\"";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        entities.escape(actual, input, outputSettings, false, false, false);
        String expected = "&amp;&lt;&gt;&amp;quot;";
        assertEquals("Expected escaped string to be '" + expected + "'", expected, actual.toString());
    }

    @Test
    public void testEscapeInAttribute() {
        Entities entities = new Entities();
        StringBuilder actual = new StringBuilder();
        String input = "<script type='text/javascript'>";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        entities.escape(actual, input, outputSettings, true, false, false);
        String expected = "<script type='text/javascript'>&lt;script&gt;";
        assertEquals("Expected escaped string to be '" + expected + "'", expected, actual.toString());
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String input = "&amp;&lt;&gt;&amp;quot;";
        String output = entities.unescape(input);
        assertEquals("Expected unescaped string to be '&<>\"'", input, output);
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();
        CoreCharset coreCharset = CoreCharset.utf;
        CharsetEncoder fallback = Character.encodingForName("UTF-8");
        for (char c = 0; c <= 127; c++) {
            boolean result = entities.canEncode(coreCharset, c, fallback);
            if (c < 32 || (c > 126 && c != 127)) {
                assertTrue("Expected character '" + c + "' to be encodable", result);
            } else {
                assertFalse("Expected character '" + c + "' to be unencodable", result);
            }
        }
    }

    @Test
    public void testLoadEntities() {
        Entities entities = new Entities();
        Map<String, Character> expected = new HashMap<>();
        expected.put("lt", '<');
        expected.put("gt", '>');
        expected.put("amp", '&');
        expected.put("apos", '\'');
        expected.put("quot", '"');
        Map<String, Character> result = entities.loadEntities("entities-base.properties");
        assertEquals("Expected loaded entities to be equal", expected, result);
    }

    @Test
    public void testToString() {
        Entities entities = new Entities();
        String input = "&lt;script&gt;";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.html);
        StringBuilder actual = new StringBuilder();
        entities.escape(actual, input, outputSettings, false, false, false);
        String expected = "&lt;script&gt;";
        assertEquals("Expected toString to return original string when escapeMode is HTML", expected, actual.toString());

        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        actual.setLength(0);
        entities.escape(actual, input, outputSettings, false, false, false);
        expected = "&lt;script&gt;";
        assertEquals("Expected toString to return original string when escapeMode is XHTML", expected, actual.toString());

        outputSettings.escapeMode(Entities.EscapeMode.extended);
        actual.setLength(0);
        entities.escape(actual, input, outputSettings, false, false, false);
        expected = "&#60;script&#62;";
        assertEquals("Expected toString to return escaped string when escapeMode is EXTENDED", expected, actual.toString());
    }

    @Test
    public void testUnescapeWithNullString() {
        Entities entities = new Entities();
        String actual = entities.unescape(null);
        assertEquals("Expected null string", null, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithEmptyString() {
        Entities entities = new Entities();
        entities.unescape("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithStringContainingInvalidSequence() {
        Entities entities = new Entities();
        String input = "&foobar;";
        entities.unescape(input);
    }

    @Test
    public void testUnescapeWithStringContainingEntities() {
        Entities entities = new Entities();
        String input = "&amp;lt;script&amp;gt;";
        String output = entities.unescape(input);
        assertEquals("Expected unescaped string to be '&<script>'", "&<script>", output);
    }
}