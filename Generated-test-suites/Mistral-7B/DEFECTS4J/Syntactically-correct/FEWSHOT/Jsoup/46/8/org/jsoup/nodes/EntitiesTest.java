package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;

        // Test known named entities
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            assertTrue(Entities.isNamedEntity(entry.getKey()));
        }

        // Test unknown named entities
        for (String name : Arrays.asList("abc", "123")) {
            assertFalse(Entities.isNamedEntity(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> base = Entities.base;

        // Test known named entities in base entity set
        for (Map.Entry<String, Character> entry : base.entrySet()) {
            assertTrue(Entities.isBaseNamedEntity(entry.getKey()));
        }

        // Test unknown named entities
        for (String name : Arrays.asList("amp", "sub")) {
            assertFalse(Entities.isBaseNamedEntity(name));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;

        // Test known named entities
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            assertEquals(entry.getValue(), Entities.getCharacterByName(entry.getKey()));
        }

        // Test unknown named entities
        assertNull(Entities.getCharacterByName("abc"));
    }

    @Test
    public void testEscapeModeXhtml() {
        Character[] xhtmlEntities = new Character[] {'&', '<', '>', '"'};
        Map<Character, String> xhtmlMap = Entities.EscapeMode.xhtml.getMap();

        for (Character c : xhtmlEntities) {
            assertEquals(xhtmlMap.get(c), Entities.escape(c.toString(), new Document.OutputSettings(Entities.EscapeMode.xhtml, StandardCharsets.UTF_8, null)).toString());
        }
    }

    @Test
    public void testEscapeModeBase() {
        Character[] baseEntities = new Character[] {'&', '<', '>', '"'};
        Character lt = '<';
        Character amp = '&';

        // Test known named entities
        for (Map.Entry<String, Character> entry : Entities.base.entrySet()) {
            Character c = entry.getValue();
            if (c != lt && c != amp && c != '>') {
                assertEquals(c.toString(), Entities.escape(entry.getKey(), new Document.OutputSettings(Entities.EscapeMode.base, StandardCharsets.UTF_8, null)).toString());
            }
        }

        // Test known named entities (lt and amp) with different escape modes
        assertEquals(lt.toString(), Entities.escape(lt.toString(), new Document.OutputSettings(Entities.EscapeMode.xhtml, StandardCharsets.UTF_8, null)).toString());
        assertEquals("&lt;", Entities.escape(lt.toString(), new Document.OutputSettings(Entities.EscapeMode.base, StandardCharsets.UTF_8, null)).toString());

        assertEquals(amp.toString(), Entities.escape(amp.toString(), new Document.OutputSettings(Entities.EscapeMode.xhtml, StandardCharsets.UTF_8, null)).toString());
        assertEquals("&amp;", Entities.escape(amp.toString(), new Document.OutputSettings(Entities.EscapeMode.base, StandardCharsets.UTF_8, null)).toString());

        // Test other characters
        assertEquals("\\u00A0", Entities.escape("\u00A0", new Document.OutputSettings(Entities.EscapeMode.base, StandardCharsets.UTF_8, null)).toString());
        assertEquals("\\u0060\\u0026", Entities.escape("\u0060\u0026", new Document.OutputSettings(Entities.EscapeMode.base, StandardCharsets.UTF_8, null)).toString());
    }

    @Test
    public void testEscapeModeExtended() {
        Character[] extendedEntities = new Character[] {'&', '<', '>', '"'};
        Character lt = '<';
        Character amp = '&';
        Character quot = '"';
        Character sub = '\u0022'; // double quot as an example of a character not present in base entity set

        // Test known named entities
        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            Character c = entry.getValue();
            if (c != lt && c != amp && c != '>' && c != quot) {
                assertEquals(c.toString(), Entities.escape(entry.getKey(), new Document.OutputSettings(Entities.EscapeMode.extended, StandardCharsets.UTF_8, null)).toString());
            }
        }

        // Test known named entities (lt, amp, quot) with different escape modes
        assertEquals(lt.toString(), Entities.escape(lt.toString(), new Document.OutputSettings(Entities.EscapeMode.xhtml, StandardCharsets.UTF_8, null)).toString());
        assertEquals("&lt;", Entities.escape(lt.toString(), new Document.OutputSettings(Entities.EscapeMode.base, StandardCharsets.UTF_8, null)).toString());
        assertEquals(lt.toString(), Entities.escape(lt.toString(), new Document.OutputSettings(Entities.EscapeMode.extended, StandardCharsets.UTF_8, null)).toString());

        assertEquals(amp.toString(), Entities.escape(amp.toString(), new Document.OutputSettings(Entities.EscapeMode.xhtml, StandardCharsets.UTF_8, null)).toString());
        assertEquals("&amp;", Entities.escape(amp.toString(), new Document.OutputSettings(Entities.EscapeMode.base, StandardCharsets.UTF_8, null)).toString());
        assertEquals(amp.toString(), Entities.escape(amp.toString(), new Document.OutputSettings(Entities.EscapeMode.extended, StandardCharsets.UTF_8, null)).toString());

        assertEquals(quot.toString(), Entities.escape(quot.toString(), new Document.OutputSettings(Entities.EscapeMode.xhtml, StandardCharsets.UTF_8, null)).toString());
        assertEquals("&quot;", Entities.escape(quot.toString(), new Document.OutputSettings(Entities.EscapeMode.base, StandardCharsets.UTF_8, null)).toString());
        assertEquals(quot.toString(), Entities.escape(quot.toString(), new Document.OutputSettings(Entities.EscapeMode.extended, StandardCharsets.UTF_8, null)).toString());

        // Test other characters
        assertEquals("\\u00A0", Entities.escape("\u00A0", new Document.OutputSettings(Entities.EscapeMode.extended, StandardCharsets.UTF_8, null)).toString());
        assertEquals("\\u0060\\u0026", Entities.escape("\u0060\u0026", new Document.OutputSettings(Entities.EscapeMode.extended, StandardCharsets.UTF_8, null)).toString());
        assertEquals("\\u0022", Entities.escape(sub.toString(), new Document.OutputSettings(Entities.EscapeMode.extended, StandardCharsets.UTF_8, null)).toString());
    }

    @Test
    public void testUnescape() {
        String input = "&lt;foo&gt;&quot;bar&amp;&#x00A0;world";
        String expected = "<foo\"bar&amp; world>";

        assertEquals(expected, Entities.unescape(input, false));
    }

    @Test
    public void testUnescapeStrict() {
        String input = "&lt;foo&gt;&quot;bar&amp;&#x00A0;world";
        String expected = "<foo\u003E\"bar&amp; \u00A0world>";

        assertEquals(expected, Entities.unescape(input, true));
    }

    @Test
    public void testCanEncode() {
        Map<Character, CoreCharset> coreCharsets = new HashMap<>();
        coreCharsets.put(Entities.CoreCharset.ascii, 'A');
        coreCharsets.put(Entities.CoreCharset.utf, 'U');
        coreCharsets.put(Entities.CoreCharset.fallback, 'F');

        for (Map.Entry<Character, CoreCharset> entry : coreCharsets.entrySet()) {
            for (int i = 0; i < 256; i++) {
                Character c = (char) i;
                CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
                boolean result = Entities.canEncode(entry.getValue(), c, encoder);

                // ASCII characters can be encoded in ASCII and UTF-8
                if (c < 0x80) {
                    assertTrue(result);
                }

                // Non-ASCII characters can only be encoded in UTF-8
                else {
                    if (!entry.getValue().equals(Entities.CoreCharset.utf)) {
                        assertFalse(result);
                    }
                }
            }
        }
    }
}