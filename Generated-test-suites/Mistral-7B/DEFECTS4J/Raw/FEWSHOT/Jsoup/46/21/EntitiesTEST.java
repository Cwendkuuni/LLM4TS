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
            assertTrue("Named entity '" + name + "' not found in Entities.full", Entities.isNamedEntity(name));
            assertEquals("Named entity '" + name + "' has incorrect character value", character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> baseByVal = Entities.baseByVal;
        Map<String, Character> base = Entities.base;

        for (Map.Entry<Character, String> entry : baseByVal.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue();
            assertTrue("Base named entity '" + name + "' not found in Entities.base", Entities.isBaseNamedEntity(name));
            assertSame("Base named entity '" + name + "' has incorrect character value", character, Entities.getCharacterByName(name));
        }

        for (String name : Arrays.asList("amp", "lt", "gt", "quot")) {
            assertTrue("Base named entity '" + name + "' not found in Entities.baseByName", Entities.isBaseNamedEntity(name));
            assertEquals("Base named entity '" + name + "' has incorrect character value", Entities.getCharacterByName(name), base.get(name));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals("Named entity '" + name + "' has incorrect character value", character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();

        String input = "&lt;foo&gt;<bar> &amp; &quot;quoted&quot; &amp;amp; &lt;x&gt;";
        String expected = "&amp;lt;foo&amp;gt;&amp;lt;bar&amp;gt; &amp;amp; &amp;amp;amp;quot;quoted&amp;amp;amp;&amp;lt;x&amp;gt;";
        String output = Entities.escape(input, out);
        assertEquals("Escaped string does not match expected", expected, output);

        // Test with XHTML output
        out.escapeMode(Entities.EscapeMode.xhtml);
        output = Entities.escape(input, out);
        assertEquals("Escaped string does not match expected (XHTML mode)", "<foo>&gt;<bar> &amp; &quot;quoted&quot; &amp; &amp;quot;", output);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;foo&amp;gt;&amp;lt;bar&amp;gt; &amp;amp; &amp;amp;amp;quot;quoted&amp;amp;amp;";
        String output = Entities.unescape(input, false);
        assertEquals("Unescaped string does not match expected", "&lt;foo&gt;&lt;bar&gt; &amp; &amp;\"quoted\"&amp;", output);

        // Test with strict unescaping
        output = Entities.unescape(input, true);
        assertEquals("Unescaped string does not match expected (strict mode)", "&lt;foo&gt;&lt;bar&gt; &amp; &amp;\"quoted\"&amp;", output);
    }

    @Test
    public void testCanEncode() {
        CoreCharset coreCharset = CoreCharset.utf;
        CharsetEncoder encoder = null; // not used in test

        for (char c = 0; c <= 0x10FFFF; c++) {
            boolean canEncode = Entities.canEncode(coreCharset, c, encoder);
            if (Character.isSurrogate(c)) {
                assertTrue("Surrogate character '" + Integer.toHexString(Character.codePointAt(Character.toString(c).toCharArray())) + "' should not be encodable", !canEncode);
            } else {
                assertTrue("Character '" + c + "' should be encodable", canEncode);
            }
        }
    }
}