package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Named entity \"" + name + "\" should be recognized", Entities.isNamedEntity(name));
            assertEquals("Named entity \"" + name + "\" maps to character " + character, character, Entities.getCharacterByName(name));
        }

        assertFalse("Invalid named entity should not be recognized", Entities.isNamedEntity(""));
        assertFalse("Invalid named entity should not be recognized", Entities.isNamedEntity(null));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> xhtmlByVal = Entities.xhtmlByVal;
        for (Map.Entry<Character, String> entry : xhtmlByVal.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue();
            assertTrue("Base named entity \"" + name + "\" should be recognized", Entities.isBaseNamedEntity(name));
            assertEquals("Base named entity \"" + name + "\" maps to character " + character, character, Entities.getCharacterByName(name));
        }

        assertFalse("Invalid named entity should not be recognized", Entities.isBaseNamedEntity(""));
        assertFalse("Invalid named entity should not be recognized", Entities.isBaseNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;
        Character lt = full.get('&lt;');
        Character gt = full.get('&gt;');
        Character amp = full.get('&amp;');
        Character quot = full.get('&quot;');

        assertEquals("&lt; should map to <", lt, '<');
        assertEquals("&gt; should map to >", gt, '>');
        assertEquals("&amp; should map to &", amp, '&');
        assertEquals("&quot; should map to \"", quot, '\"');

        assertNull("Invalid named entity should not map to any character", Entities.getCharacterByName(""));
        assertNull("Invalid named entity should not map to any character", Entities.getCharacterByName(null));
    }

    @Test
    public void testEscape() {
        String input = "foo &lt;bar&gt; baz &amp; quux &quot;quux&quot;";

        assertEquals("foo &amp;lt;bar&amp;gt; baz &amp;amp; quox &amp;quot;quox&quot;", Entities.escape(input, Document.OutputSettings.xhtml()));
        assertEquals("foo <bar> baz &amp; quox \"quox\"", Entities.escape(input, Document.OutputSettings.base()));
        assertEquals("foo <bar> baz &amp;quox \"quox\"", Entities.escape(input, Document.OutputSettings.extended()));

        assertNull(Entities.escape(null, Document.OutputSettings.base()));
    }

    @Test
    public void testUnescape() {
        String input = "foo &amp;lt;bar&amp;gt; baz &amp;amp; quux &amp;quot;quox&quot;";

        assertEquals("foo <bar> baz & quox \"quox\"", Entities.unescape(input));
        assertEquals("foo <bar> baz & quox \"quox\"", Entities.unescape(input, false));
    }

    @Test
    public void testCanEncode() {
        CoreCharset utf = CoreCharset.utf;
        for (char character = 0x0; character <= 0x10FFFF; character++) {
            boolean canEncode = Entities.canEncode(utf, character, utf.fallback());
            String string = Character.isUnicodeIdentifierStart(character) ? Character.toString(character) : String.valueOf((int) character);
            if (canEncode) {
                System.out.print(string + " ");
            } else {
                assertEquals("Character " + character + " cannot be encoded using UTF encoding", false, canEncode);
            }
        }
    }
}