package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> entities = Entities.full;
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            String key = entry.getKey();
            Character value = entry.getValue();
            assertTrue(Entities.isNamedEntity(key));
            assertEquals(value, Entities.getCharacterByName(key));
        }

        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
        assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> entities = Entities.base;
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            String key = entry.getKey();
            Character value = entry.getValue();
            assertTrue(Entities.isBaseNamedEntity(key));
            assertEquals(value, Entities.getCharacterByName(key));
        }

        assertFalse(Entities.isBaseNamedEntity(""));
        assertFalse(Entities.isBaseNamedEntity(null));
        assertFalse(Entities.isBaseNamedEntity("foo"));

        Map<String, Character> xhtmlEntities = Entities.xhtmlByVal;
        for (Map.Entry<Character, String> entry : xhtmlEntities.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            assertFalse(Entities.isBaseNamedEntity(value));
            assertTrue(Entities.isNamedEntity(value));
            assertFalse(Entities.isBaseNamedEntity(""));
            assertFalse(Entities.isBaseNamedEntity(null));
            assertFalse(Entities.isBaseNamedEntity("foo"));
            assertFalse(Entities.isNamedEntity(key));
            assertEquals(key, Entities.getCharacterByName(value));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> entities = Entities.full;
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            String key = entry.getKey();
            Character value = entry.getValue();
            assertEquals(value, Entities.getCharacterByName(key));
        }

        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName(null));
        assertNull(Entities.getCharacterByName("foo"));

        Map<Character, String> xhtmlEntities = Entities.xhtmlByVal;
        for (Map.Entry<Character, String> entry : xhtmlEntities.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            assertEquals(key, Entities.getCharacterByName(value));
            assertNull(Entities.getCharacterByName(""));
            assertNull(Entities.getCharacterByName(null));
            assertNull(Entities.getCharacterByName("foo"));
        }
    }

    @Test
    public void testEscapeModeValues() {
        Set<EscapeMode> escapeModes = Collections.enumSet(Entities.EscapeMode.class);
        assertEquals(3, escapeModes.size());
        for (EscapeMode escapeMode : escapeModes) {
            assertFalse(escapeMode.equals(null));
            assertTrue(escapeMode instanceof EscapeMode);
            assertEquals(escapeMode.getClass(), EscapeMode.class);
            assertEquals(escapeMode.name(), escapeMode.toString());
        }

        Map<Character, String> xhtmlEntities = Entities.xhtmlByVal;
        assertEquals(4, xhtmlEntities.size());
    }

    @Test
    public void testGetMapByEscapeMode() {
        assertEquals(Entities.EscapeMode.xhtml.getMap(), Entities.XHTML);
        assertEquals(Entities.EscapeMode.base.getMap(), Entities.BASE);
        assertEquals(Entities.EscapeMode.extended.getMap(), Entities.FULL);
    }

    @Test
    public void testEscape() {
        String input = "<foo &bar> quo\"t >";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setEscapeMode(Entities.EscapeMode.base);
        String expected = "&lt;foo &amp;amp;bar&gt; quo&quot;t &gt;";
        String output = Entities.escape(input, outputSettings);
        assertEquals(expected, output);

        outputSettings.setEscapeMode(Entities.EscapeMode.xhtml);
        expected = "&lt;foo&gt; &amp; &lt;bar&gt; &quot;quo&quot;t &gt;";
        output = Entities.escape(input, outputSettings);
        assertEquals(expected, output);

        outputSettings.setEscapeMode(Entities.EscapeMode.extended);
        expected = "&#x3C;foo&#x26;&#x26;&#x3C;bar&#x22;quo&#x22;t&#x3E;";
        output = Entities.escape(input, outputSettings);
        assertEquals(expected, output);

        outputSettings.setEscapeMode(null);
        expected = "<foo &bar> quo\"t >";
        output = Entities.escape(input, outputSettings);
        assertEquals(expected, output);
    }

    @Test
    public void testUnescape() {
        String input = "&lt;foo&amp;bar&quot;quo&apos;t&gt;";
        String output = Entities.unescape(input);
        assertEquals("<foo bar \"quo't\">", output);

        input = "&lt;foo&amp;bar&quot;quo&apos;t&gt;";
        output = Entities.unescape(input, true);
        assertEquals("<foo&bar>quo't", output);

        input = "&lt;foo&amp;bar&quot;quo&apos;t&gt;";
        output = Entities.unescape(input, false); // strict
        assertEquals("<foo bar \"quo't\">", output);
    }

    @Test
    public void testCanEncode() {
        CharsetEncoder encoder = CharacterEncodingBuilder.forName("UTF-8").newEncoder();
        for (CoreCharset coreCharset : CoreCharset.values()) {
            CharSequence sequence = "";
            switch (coreCharset) {
                case ascii:
                    sequence = "\u0000\u0001\u0007\u0008\u000B\u000C\u000E\u000F\n\r\t\u000B\b\f\u000C\n\r";
                    break;
                case utf:
                    sequence = "\u0000\u007F\uD800\uDBFF\uD801\uDFFF";
                    break;
                case fallback:
                    sequence = "\uD800\uDBFF\uD801\uDFFF";
                    break;
            }
            for (int i = 0; i < sequence.length(); ++i) {
                char character = sequence.charAt(i);
                if (Entities.canEncode(CoreCharset.ascii, character, encoder)) {
                    assertTrue(encoder.canEncode(Character.toString(character)));
                } else {
                    assertFalse(encoder.canEncode(Character.toString(character)));
                }
            }
        }
    }
}