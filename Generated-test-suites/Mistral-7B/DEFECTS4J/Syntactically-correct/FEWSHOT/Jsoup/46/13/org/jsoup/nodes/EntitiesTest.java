package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> entities = Entities.full;
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Named entity " + name + " not recognized", Entities.isNamedEntity(name));
            assertEquals("Named entity " + name + " maps to " + character, character, Entities.getCharacterByName(name));
        }

        assertFalse("Unknown named entity should not be recognized", Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> entities = Entities.base;
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Base named entity " + name + " not recognized", Entities.isBaseNamedEntity(name));
            assertEquals("Base named entity " + name + " maps to " + character, character, Entities.getCharacterByName(name));
        }

        assertFalse("Unknown base named entity should not be recognized", Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> entities = Entities.full;
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals("Named entity " + name + " maps to " + character, character, Entities.getCharacterByName(name));
        }

        assertNull("Unknown named entity should return null", Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        String input = "&lt;foo &quot;bar&quot; &amp;amp; &x27; &x61; &x00A9; &nbsp; &amp;#128; &amp;#x1F44D; &amp;#x1F602;";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setEscapeMode(Entities.EscapeMode.base);
        String expectedOutput = "&amp;lt;foo &amp;quot;bar&amp;quot; &amp;amp; &#39; &#115; &#169; &nbsp; &#128; &#x1F44D; &#x1F602;";
        String output = Entities.escape(input, outputSettings);
        assertEquals("Escaped output should match expected output", expectedOutput, output);

        outputSettings.setEscapeMode(Entities.EscapeMode.xhtml);
        String xhtmlOutput = Entities.escape(input, outputSettings);
        assertEquals("XHTML escaped output should only include &quot;, &lt;, &gt;, and &amp;",
                "&amp;lt;foo&amp;quot;bar&amp;quot;&amp;amp;&#39; &#115; &#169;", xhtmlOutput);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;foo &amp;quot;bar&amp;quot; &amp;amp; &amp;#39; &#115; &#169; &nbsp; &amp;#128; &amp;#x1F44D; &amp;#x1F602;";
        String output = Entities.unescape(input);
        assertEquals("Unescaped output should match input", "&lt;foo \"bar\" &amp;amp; ' &#115; &#169; ", output);
    }

    @Test
    public void testCanEncode() {
        CoreCharset coreCharset = CoreCharset.utf;
        for (char c = 0x00; c <= 0x7F; c++) {
            assertTrue("ASCII character " + (int) c + " should be encodable",
                    Entities.canEncode(coreCharset, c, null));
        }

        for (char c = 0x80; c <= 0xFFFF; c++) {
            if (Character.isISOControl(c)) {
                continue;
            }
            assertTrue("UTF-16 character " + (int) c + " should be encodable",
                    Entities.canEncode(coreCharset, c, null));
        }

        char surrogateHigh = '\uD800';
        char surrogateLow = '\uDC00';
        char supplementary = '\u1F602';

        // surrogate pairs are not encodable as UTF-8
        assertFalse("Surrogate pair " + surrogateHigh + surrogateLow + " should not be encodable",
                Entities.canEncode(CoreCharset.utf, surrogateHigh, null));
        assertFalse("Surrogate pair " + surrogateHigh + surrogateLow + " should not be encodable",
                Entities.canEncode(CoreCharset.utf, surrogateLow, null));

        // supplementary characters are not encodable as UTF-8 by default
        assertFalse("Supplementary character " + supplementary + " should not be encodable",
                Entities.canEncode(CoreCharset.utf, supplementary, null));

        // but they can be encodable with a specific encoder
        CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder();
        assertTrue("Supplementary character " + supplementary + " should be encodable with US-ASCII encoder",
                Entities.canEncode(CoreCharset.ascii, supplementary, asciiEncoder));
    }
}