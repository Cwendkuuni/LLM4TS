package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        for (Map.Entry<Character, String> entry : Entities.full.entrySet()) {
            char key = entry.getKey();
            String value = entry.getValue().toString();
            assertTrue("Named Entity " + key + " should be recognized", Entities.isNamedEntity(value));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        for (Map.Entry<Character, String> entry : Entities.base.entrySet()) {
            char key = entry.getKey();
            String value = entry.getValue().toString();
            assertTrue("Base Named Entity " + key + " should be recognized", Entities.isBaseNamedEntity(value));
        }
    }

    @Test
    public void testGetCharacterByName() {
        for (Map.Entry<Character, String> entry : Entities.full.entrySet()) {
            char key = entry.getKey();
            Character value = entry.getValue();
            assertEquals("Named Entity " + key + " should be mapped to " + value, value, Entities.getCharacterByName(Character.toString(key)));
        }
    }

    @Test
    public void testEscapeModeXhtml() {
        Map<Character, String> map = Entities.EscapeMode.xhtml.getMap();
        assertSame("xhtml should map lt to &lt;", "&lt;", map.get((char) 0x003C));
        assertSame("xhtml should map gt to &gt;", "&gt;", map.get((char) 0x003E));
        assertSame("xhtml should map amp to &amp;", "&amp;", map.get((char) 0x0026));
        assertSame("xhtml should map quot to &quot;", "&quot;", map.get((char) 0x0022));

        assertNull("xhtml should not map any other character", map.get((char) 0x007F));
    }

    @Test
    public void testEscapeModeBase() {
        Map<Character, String> map = Entities.EscapeMode.base.getMap();

        // Check standard entities
        for (Map.Entry<Character, String> entry : Entities.xhtmlByVal.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            assertSame("base should map " + value + " to " + key, key, map.get(key));
        }

        // Check other entities
        assertSame("base should map &eacute; to &#233;", "&#233;", map.get((char) 0xC3));
        assertSame("base should map &Agrave; to &#192;", "&#192;", map.get((char) 0xC0));
        assertSame("base should map &#x26; to &amp;", "&amp;", map.get((char) 0x26));
        assertSame("base should map &#xA0; to &nbsp;", "&nbsp;", map.get((char) 0xA0));
        assertSame("base should map &#x3C; to &lt;", "&lt;", map.get((char) 0x3C));
        assertSame("base should map &#x3E; to &gt;", "&gt;", map.get((char) 0x3E));
        assertSame("base should map &#x22; to &quot;", "&quot;", map.get((char) 0x22));
    }

    @Test
    public void testEscapeModeExtended() {
        Map<Character, String> map = Entities.EscapeMode.extended.getMap();

        // Check standard entities
        for (Map.Entry<Character, String> entry : Entities.full.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue().toString();
            assertSame("extended should map " + value + " to " + key, key, map.get(key));
        }

        // Check other entities
        assertSame("extended should map &reg; to &#174;", "&#174;", map.get((char) 0xAE));
        assertSame("extended should map &shy; to &#x00AD;", "&#x00AD;", map.get((char) 0xAD));
        assertSame("extended should map &#x2018; to &lsquo;", "&lsquo;", map.get((char) 0x2018));
        assertSame("extended should map &#x2019; to &rsquo;", "&rsquo;", map.get((char) 0x2019));
        assertSame("extended should map &#x201C; to &ldquo;", "&ldquo;", map.get((char) 0x201C));
        assertSame("extended should map &#x201D; to &rdquo;", "&rdquo;", map.get((char) 0x201D));
        assertSame("extended should map &#x201E; to &bdquo;", "&bdquo;", map.get((char) 0x201E));
        assertSame("extended should map &#x2032; to &permil;", "&permil;", map.get((char) 0x2032));
        assertSame("extended should map &#x203F; to &dagger;", "&dagger;", map.get((char) 0x203F));
        assertSame("extended should map &#x2044; to &permil;", "&permil;", map.get((char) 0x2044));
        assertSame("extended should map &#x20AC; to &euro;", "&euro;", map.get((char) 0x20AC));
        assertSame("extended should map &#x2122; to &trade;", "&trade;", map.get((char) 0x2122));
    }

    @Test
    public void testEscape() {
        String string = "&lt;foo&gt;bar&amp;baz&quot;quux&nbsp; &nbsp; &nbsp;";
        StringBuilder escapedStringBuilder = new StringBuilder(256);
        Entities.escape(escapedStringBuilder, string, null, false, false, false);
        assertEquals("Original: " + string + "\nEscaped: " + escapedStringBuilder, "<foo>&gt;bar&amp;baz&quot;quux&nbsp; &nbsp; &nbsp;</foo>", escapedStringBuilder.toString());
    }

    @Test
    public void testUnescape() {
        String string = "<foo>&gt;bar&amp;baz&quot;quux&nbsp; &nbsp; &nbsp;</foo>";
        String unescapedString = Entities.unescape(string);
        assertEquals("Original: " + string + "\nUnescaped: " + unescapedString, "&lt;foo&gt;bar&amp;baz&quot;quux&nbsp; &nbsp; &nbsp;</foo>", unescapedString);
    }

    @Test
    public void testCanEncode() {
        // Test for ASCII characters
        for (char c = 0; c < 127; c++) {
            assertTrue("Can encode " + c, Entities.canEncode(Entities.CoreCharset.ascii, c, null));
        }

        // Test for non-ASCII characters
        for (char c = 0x100; c < 0x10FFFF; c++) {
            assertTrue("Can encode " + Character.toChars(c)[0], Entities.canEncode(Entities.CoreCharset.utf, c, null));
        }
    }
}