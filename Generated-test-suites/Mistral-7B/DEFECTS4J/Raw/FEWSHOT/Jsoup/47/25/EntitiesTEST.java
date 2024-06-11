package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

        assertFalse(Entities.isBaseNamedEntity("amp-entities-restricted"));
        assertFalse(Entities.isBaseNamedEntity("quot-entities-restricted"));
        assertFalse(Entities.isBaseNamedEntity("lt-entities-restricted"));
        assertFalse(Entities.isBaseNamedEntity("gt-entities-restricted"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals(character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);

        String string = "&lt;foo&amp;bar&quot;quux&gt;";
        assertEquals("&lt;foo&amp;amp;amp;bar&amp;quot;quux&amp;gt;", Entities.escape(string, out));

        out.escapeMode(Entities.EscapeMode.base);
        assertEquals("&lt;foo&amp;amp;lt;bar&amp;amp;quot;quux&amp;amp;gt;", Entities.escape(string, out));

        out.escapeMode(Entities.EscapeMode.extended);
        assertEquals("&lt;foo&amp;amp;#x60;&amp;amp;quot;quux&amp;amp;#x3E;", Entities.escape(string, out));

        String string2 = "foo &lt; bar &amp;  quux &gt;";
        assertEquals("foo &amp;lt; bar &amp;amp;amp; amp; &amp;quot;quux&amp;gt;", Entities.escape(string2, out));

        String string3 = "foo &lt; bar &quot; quux &gt;";
        assertEquals("foo &amp;lt; bar &amp;quot;&amp;quot; quux &amp;gt;", Entities.escape(string3, out));
    }

    @Test
    public void testUnescape() {
        String string = "&lt;foo&amp;amp;bar&amp;quot;quux&amp;gt;";
        assertEquals("<foo&amp;amp;bar\"quux>", Entities.unescape(string));

        String string2 = "&lt;foo&amp;amp;&#x60;bar&amp;quot;quux&amp;gt;";
        assertEquals("<foo&quot;bar\"quux>", Entities.unescape(string2, false));
        assertEquals("<foo&quot;bar&quot;quux>", Entities.unescape(string2, true));

        String string3 = "&lt;foo&amp;amp;#x3C;bar&amp;quot;quux&amp;gt;";
        assertEquals("<foo<bar\"quux>", Entities.unescape(string3));
    }

    @Test
    public void testCanEncode() {
        for (CoreCharset charset : CoreCharset.values()) {
            for (char c = 0; c <= 0x7F; c++) { // US-ASCII
                assertTrue(Entities.canEncode(charset, c, null));
            }

            for (char c = 0x80; c <= 0xFF; c++) { // US-ASCII extended
                if (charset == CoreCharset.ascii) {
                    assertFalse(Entities.canEncode(charset, c, null));
                } else {
                    assertTrue(Entities.canEncode(charset, c, null));
                }
            }

            for (char c = 0; c <= 0xFFFF; c++) { // UTF-16
                assertTrue(Entities.canEncode(charset.utf, c, null));
            }

            for (char firstChar : new char[]{'€', '£'}) { // high surrogate
                for (char secondChar : new char[]{'Å', 'ẞ'}) { // low surrogate
                    char supplementary = Character.toCodePoint(firstChar, secondChar);
                    assertTrue(Entities.canEncode(charset.utf, firstChar, null));
                    assertTrue(Entities.canEncode(charset.utf, secondChar, null));
                    assertTrue(Entities.canEncode(charset.utf, supplementary, null));
                }
            }
        }
    }
}