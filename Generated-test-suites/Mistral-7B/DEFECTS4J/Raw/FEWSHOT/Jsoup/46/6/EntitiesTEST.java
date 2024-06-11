package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

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
        Map<String, Character> base = Entities.base;
        for (Map.Entry<String, Character> entry : base.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue(Entities.isBaseNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }

        String notNamedEntity = "notNamedEntity";
        assertFalse(Entities.isBaseNamedEntity(notNamedEntity));
        assertNull(Entities.getCharacterByName(notNamedEntity));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals(character, Entities.getCharacterByName(name));
        }

        String notNamedEntity = "notNamedEntity";
        assertNull(Entities.getCharacterByName(notNamedEntity));
    }

    @Test
    public void testEscapeModeValues() {
        Arrays.stream(Entities.EscapeMode.values())
                .forEach(value -> {
                    Map<Character, String> map = value.getMap();
                    assertEquals(map.size(), value.name().length());
                });
    }

    @Test
    public void testEscape() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.extended);

        String input = "&amp;lt;foo &quot;bar&quot; &nbsp; &agrave; &auml; &eacute; &egrave; &iuml; &oelig; &scaron; &thorn; &yuml; &copy; &reg; &trade;";
        String expected = "&#x26amp;#x26lt;foo &#x22quo;bar&#x22quo; &#x2002; &#x00C0; &#x00C3; &#x00C9; &#x00D3; &#x00DD; &#x00DE; &#x00DC; &#x00F1; &#x00A9; &#x00AE; &#x2122;";
        String output = Entities.escape(input, outputSettings);
        assertEquals(expected, output);
    }

    @Test
    public void testEscape_xhtmlMode() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);

        String input = "&amp;lt;foo &quot;bar&quot; &nbsp; &agrave; &auml; &eacute; &egrave; &iuml; &oelig; &scaron; &thorn; &yuml; &copy; &reg; &trade;";
        String expected = "&amp;lt; &quot;bar&quot; &nbsp; #x00C0; #x00C3; #x00C9; #x00D3; #x00DD; #x00DE; #x00DC; #x00F1; &copy; &reg; &trade;";
        String output = Entities.escape(input, outputSettings);
        assertEquals(expected, output);
    }

    @Test
    public void testEscape_baseMode() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.base);

        String input = "&amp;lt;foo &quot;bar&quot; &nbsp; &agrave; &auml; &eacute; &egrave; &iuml; &oelig; &scaron; &thorn; &yuml; &copy; &reg; &trade;";
        String expected = "&amp;lt; &quot;bar&quot; &nbsp; &Agrave; &Auml; &Eacute; &Egrave; &Iuml; &Oelig; &Scaron; &Thorn; &Yuml; &copy; &reg; &trade;";
        String output = Entities.escape(input, outputSettings);
        assertEquals(expected, output);
    }

    @Test
    public void testEscape_asciiMode() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.ascii);

        String input = "&amp;lt;foo &quot;bar&quot; &nbsp; &agrave; &auml; &eacute; &egrave; &iuml; &oelig; &scaron; &thorn; &yuml; &copy; &reg; &trade;";
        String output = Entities.escape(input, outputSettings);
        assertTrue(output.matches(".*&amp;(lt|quot|nbsp|agrave|auml|eacute|egrave|iuml|oelig|scaron|thorn|yuml|copy|reg|trade).*"));
    }

    @Test
    public void testUnescape() {
        String input = "&#x26amp;#x26lt;foo &#x22quo;bar&#x22quo; &#x2002; &#x00C0; &#x00C3; &#x00C9; &#x00D3; &#x00DD; &#x00DE; &#x00DC; &#x00F1; &#x00A9; &#x00AE; &#x2122;";
        String expected = "&amp;lt;foo &quot;bar&quot; &nbsp; &#xC0; &#xC3; &#xC9; &#xD3; &#xDD; &#xDE; &#xDC; &#xF1; &copy; &reg; &trade;";
        String output = Entities.unescape(input);
        assertEquals(expected, output);
    }

    @Test
    public void testCanEncode() {
        CoreCharset ascii = CoreCharset.ascii;
        CoreCharset utf = CoreCharset.utf;

        for (char c = 0; c < Character.MAX_VALUE; c++) {
            boolean asciiCanEncode = Entities.canEncode(ascii, c, null);
            boolean utfCanEncode = Entities.canEncode(utf, c, null);

            if ((c >= 0x80 && c <= 0x9F) || (c >= 0xE0 && c <= 0xFF)) {
                // ASCII can only encode US-ASCII
                assertFalse(asciiCanEncode);
                assertTrue(utfCanEncode);
            } else {
                assertTrue(asciiCanEncode || utfCanEncode);
            }
        }
    }
}