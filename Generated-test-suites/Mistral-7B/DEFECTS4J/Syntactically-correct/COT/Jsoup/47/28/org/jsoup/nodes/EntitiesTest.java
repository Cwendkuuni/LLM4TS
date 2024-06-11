package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
        Map<Character, String> xhtmlByVal = Entities.EscapeMode.xhtml.getMap();
        Map<Character, String> baseByVal = Entities.EscapeMode.base.getMap();
        Map<Character, String> extendedByVal = Entities.EscapeMode.extended.getMap();

        Character lt = Entities.getCharacterByName("lt");
        assertEquals('<', lt);

        Character gt = Entities.getCharacterByName("gt");
        assertEquals('>', gt);

        Character amp = Entities.getCharacterByName("amp");
        assertEquals('&', amp);

        Character quot = Entities.getCharacterByName("quot");
        assertEquals('"', quot);

        Character ltXhtml = xhtmlByVal.get(lt);
        assertEquals("lt", ltXhtml);

        Character gtXhtml = xhtmlByVal.get(gt);
        assertEquals("gt", gtXhtml);

        Character ampXhtml = xhtmlByVal.get(amp);
        assertEquals("amp", ampXhtml);

        Character quotXhtml = xhtmlByVal.get(quot);
        assertEquals("quot", quotXhtml);

        Character ltBase = baseByVal.get(lt);
        assertEquals("lt", ltBase);

        Character gtBase = baseByVal.get(gt);
        assertEquals("gt", gtBase);

        Character ampBase = baseByVal.get(amp);
        assertEquals("amp", ampBase);

        Character quotBase = baseByVal.get(quot);
        assertEquals("quot", quotBase);

        Character ltExtended = extendedByVal.get(lt);
        assertEquals("lt", ltExtended);

        Character gtExtended = extendedByVal.get(gt);
        assertEquals("gt", gtExtended);

        Character ampExtended = extendedByVal.get(amp);
        assertEquals("amp", ampExtended);

        Character quotExtended = extendedByVal.get(quot);
        assertEquals("quot", quotExtended);
    }

    @Test
    public void testEscape() {
        String input = "&lt;abc&gt;def&quot;";
        Map<Character, String> map = Entities.EscapeMode.base.getMap();

        StringBuilder expectedOutput = new StringBuilder();
       expectedOutput.append("&amp;lt;");
        expectedOutput.append("abc");
        expectedOutput.append("&amp;gt;");
        expectedOutput.append("def");
        expectedOutput.append("&amp;quot;");

        StringBuilder output = new StringBuilder();
        Entities.escape(output, input, null, false, false, false, map);

        assertEquals(expectedOutput.toString(), output.toString());
    }

    @Test
    public void testEscapeInAttribute() {
        String input = "<tag attr='&quot;value&amp;lt;>&gt;';>";
        Map<Character, String> map = Entities.EscapeMode.xhtml.getMap();

        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("<tag attr='&amp;quot;value&amp;lt;>&amp;gt;&amp;gt;'>");

        StringBuilder output = new StringBuilder();
        Entities.escape(output, input, null, true, false, false, map);

        assertEquals(expectedOutput.toString(), output.toString());
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;abc&amp;gt;def&amp;quot;";
        String output = Entities.unescape(input, false);

        assertEquals("<abcdef>", output);
    }

    @Test
    public void testCanEncode() {
        CoreCharset coreCharset = CoreCharset.utf;
        CharsetEncoder encoder = null; // not used here

        for (char c = 0; c < Character.MAX_VALUE; c++) {
            if (Character.isSupplementaryCodePoint(c)) continue;
            boolean canEncode = Entities.canEncode(coreCharset, c, encoder);
            assertTrue(canEncode);
        }

        char surrogate1 = '\uD800';
        char surrogate2 = '\uDC00';
        char supplementary = '\u1F610';

        CoreCharset ascii = CoreCharset.ascii;
        assertFalse(Entities.canEncode(ascii, surrogate1));
        assertFalse(Entities.canEncode(ascii, surrogate2));
        assertFalse(Entities.canEncode(ascii, supplementary));
    }
}