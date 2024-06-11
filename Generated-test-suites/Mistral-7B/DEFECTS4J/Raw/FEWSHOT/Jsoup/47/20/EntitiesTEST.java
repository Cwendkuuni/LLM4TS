package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            assertTrue(Entities.isNamedEntity(entry.getKey()));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> base = Entities.base;
        for (Map.Entry<String, Character> entry : base.entrySet()) {
            assertTrue(Entities.isBaseNamedEntity(entry.getKey()));
        }

        // check entities not in base
        Character c1 = Entities.getCharacterByName("copysr");
        assertNull(c1);

        Character c2 = Entities.getCharacterByName("reg");
        assertNull(c2);
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            assertEquals(entry.getValue(), Entities.getCharacterByName(entry.getKey()));
        }
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        String input = "&lt;foo&gt;<bar>&quot;baz&quot; <quux&nbsp;>&amp;quux";
        String expected = "&amp;lt;foo&amp;gt;&#x003C;bar>&#x2002;quux&amp;amp;quux";
        String result = Entities.escape(input, out);
        assertEquals(expected, result);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;foo&amp;gt;&#x003C;bar>&#x2002;quux&amp;amp;quux";
        String expected = "&lt;foo&gt;<bar> quuxquux";
        String result = Entities.unescape(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCanEncode() {
        CoreCharset coreCharset = CoreCharset.utf;
        CharsetEncoder encoder = null; // not used here
        Map<Character, String> map = Entities.EscapeMode.extended.getMap();

        for (Character c : Arrays.asList('a', 'ß', 'µ')) {
            assertTrue(Entities.canEncode(coreCharset, c, encoder) || map.containsKey(c));
        }

        for (Character c : Arrays.asList('\uD835', '\uD836')) { // Unicode surrogates
            assertFalse(Entities.canEncode(coreCharset, c, encoder));
        }

        for (Character c : Arrays.asList('ö', 'ä')) { // Unicode characters outside ASCII
            assertFalse(Entities.canEncode(coreCharset, c, encoder));
        }
    }
}