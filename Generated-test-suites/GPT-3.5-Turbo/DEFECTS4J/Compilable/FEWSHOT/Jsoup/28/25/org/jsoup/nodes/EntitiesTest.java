
package org.jsoup.nodes;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("foo"));
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("foo"));
        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscape() {
        String string1 = "<test>";
        String string2 = "Testing \"escape\"";
        assertEquals("&lt;test&gt;", Entities.escape(string1, null));
        assertEquals("Testing &quot;escape&quot;", Entities.escape(string2, null));
    }

    @Test
    public void testEscapeWithEncoder() {
        String string1 = "<test>";
        String string2 = "Testing \"escape\"";
        Map<Character, String> xhtmlMap = Entities.EscapeMode.xhtml.getMap();
        Map<Character, String> baseMap = Entities.EscapeMode.base.getMap();
        assertEquals("&lt;test&gt;", Entities.escape(string1, null, Entities.EscapeMode.xhtml));
        assertEquals("Testing &quot;escape&quot;", Entities.escape(string2, null, Entities.EscapeMode.xhtml));
        assertEquals("&lt;test&gt;", Entities.escape(string1, null, Entities.EscapeMode.base));
        assertEquals("Testing \"escape\"", Entities.escape(string2, null, Entities.EscapeMode.base));
        assertEquals("<test>", Entities.escape(string1, null, Entities.EscapeMode.extended));
        assertEquals("Testing \"escape\"", Entities.escape(string2, null, Entities.EscapeMode.extended));

        StringBuilder xhtmlAccum = new StringBuilder();
        StringBuilder baseAccum = new StringBuilder();
        StringBuilder extendedAccum = new StringBuilder();
        for (int i = 0; i < string1.length(); i++) {
            Character c = string1.charAt(i);
            if (xhtmlMap.containsKey(c))
                xhtmlAccum.append('&').append(xhtmlMap.get(c)).append(';');
            else
                xhtmlAccum.append(c);
            if (baseMap.containsKey(c))
                baseAccum.append('&').append(baseMap.get(c)).append(';');
            else
                baseAccum.append(c);
            extendedAccum.append(c);
        }

        assertEquals(xhtmlAccum.toString(), Entities.escape(string1, null, Entities.EscapeMode.xhtml));
        assertEquals(baseAccum.toString(), Entities.escape(string1, null, Entities.EscapeMode.base));
        assertEquals(extendedAccum.toString(), Entities.escape(string1, null, Entities.EscapeMode.extended));
    }

    @Test
    public void testUnescape() {
        String string1 = "&lt;test&gt;";
        String string2 = "Testing &quot;unescape&quot;";
        assertEquals("<test>", Entities.unescape(string1));
        assertEquals("Testing \"unescape\"", Entities.unescape(string2));
    }

    @Test
    public void testStrictUnescape() {
        String string1 = "&lt;test&gt;";
        String string2 = "Testing &quot;unescape&quot;";
        assertEquals("<test>", Entities.unescape(string1, true));
        assertEquals("Testing \"unescape\"", Entities.unescape(string2, true));
    }

    @Test
    public void testStrictUnescapeFalse() {
        String string1 = "&lt;test&gt;";
        String string2 = "Testing &quot;unescape&quot;";
        assertEquals("<test>", Entities.unescape(string1, false));
        assertEquals("Testing \"unescape\"", Entities.unescape(string2, false));
    }
}
