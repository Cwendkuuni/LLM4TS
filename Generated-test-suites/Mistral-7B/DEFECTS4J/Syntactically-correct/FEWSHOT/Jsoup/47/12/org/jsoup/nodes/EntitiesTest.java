package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> entities = Entities.full;
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue(Entities.isNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> entities = Entities.base;
        for (String name : Arrays.asList("lt", "gt", "quot", "amp")) {
            assertTrue(Entities.isBaseNamedEntity(name));
            assertEquals(Entities.getCharacterByName(name), Entities.getCharacterByName(Entities.xhtmlByVal.get(name)));
        }
        assertFalse(Entities.isBaseNamedEntity("crazy"));
    }

    @Test
    public void testGetCharacterByName() {
        for (EscapeMode mode : Entities.EscapeMode.values()) {
            Map<Character, String> map = mode.getMap();
            for (Map.Entry<Character, String> entry : map.entrySet()) {
                Character character = entry.getKey();
                String name = entry.getValue();
                assertEquals(character, Entities.getCharacterByName(name));
            }
        }
    }

    @Test
    public void testEscape() {
        String input = "<>&\"'";
        Document.OutputSettings out = new Document.OutputSettings();
        StringBuilder expected = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c))
                continue;
            String escaped = Entities.escape(Character.toString(c), out, false, false, false);
            expected.append(escaped);
        }

        String output = Entities.escape(input, out);
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testUnescape() {
        String input = "&lt;foo&gt;<bar&quot; &amp; baz&amp;";
        String output = Entities.unescape(input);
        assertEquals("<foo><bar \" baz>", output);
    }

    @Test
    public void testCanEncode() {
        for (CoreCharset charset : CoreCharset.values()) {
            CharsetEncoder encoder = Character.encodingForName(charset.name()).newEncoder();
            for (char c = (char) 0; c <= 0x7F; c++) {
                if (charset == CoreCharset.ascii && c > 0x1F && c != 0x7F)
                    continue;
                assertTrue(Entities.canEncode(charset, c, encoder));
                if (charset != CoreCharset.ascii) {
                    CharSequence cs = Character.toString(c);
                    assertTrue(encoder.canEncode(cs));
                }
            }
            for (char c = 0x80; c <= 0xFFFF; c++) {
                assertFalse(Entities.canEncode(charset, c, encoder));
            }
        }
    }
}