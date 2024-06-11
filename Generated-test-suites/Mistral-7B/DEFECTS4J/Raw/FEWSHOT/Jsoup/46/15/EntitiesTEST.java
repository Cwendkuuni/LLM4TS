package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> entities = Entities.full;
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Named entity '" + name + "' not found in Entities.full.", Entities.isNamedEntity(name));
            assertEquals("Named entity '" + name + "' should map to character " + (int) character + ".", character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> entities = Entities.base;
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Named entity '" + name + "' not found in Entities.base.", Entities.isBaseNamedEntity(name));
            assertEquals("Named entity '" + name + "' should map to character " + (int) character + ".", character, Entities.getCharacterByName(name));
        }

        String notNamedEntity = "notNamedEntity";
        assertFalse("'" + notNamedEntity + "' is a base named entity.", Entities.isBaseNamedEntity(notNamedEntity));
        assertNull("Named entity '" + notNamedEntity + "' should return null.", Entities.getCharacterByName(notNamedEntity));
    }

    @Test
    public void testEscapeAndUnescape() throws IOException {
        String inputString = "&amp;<foo>&quot;bar&gt;<baz&gt;quux&quot;";

        String xhtmlEscaped = Entities.escape(inputString, Document.OutputSettings.xhtml());
        assertEquals("&amp;amp; &lt;foo&gt;&amp;quot;bar&gt; &lt;baz&gt;quux&amp;quot;", xhtmlEscaped);

        String baseEscaped = Entities.escape(inputString, Document.OutputSettings.base());
        assertEquals("&amp;amp;<foo>&quot;bar&gt;&lt;baz&gt;quux&quot;", baseEscaped);

        String extendedEscaped = Entities.escape(inputString, Document.OutputSettings.extended());
        assertEquals("&amp;amp;<foo>&quot;bar&gt;&lt;baz&gt;quux&quot;", extendedEscaped);

        String unescaped = Entities.unescape(xhtmlEscaped);
        assertEquals(inputString, unescaped);

        // Test for missing ';' at the end of entity
        String missingSemicolon = "&amp";
        assertNotEquals(inputString, Entities.unescape(missingSemicolon));
        assertEquals("&amp;", Entities.unescape(missingSemicolon, false));
        assertEquals(inputString, Entities.unescape(missingSemicolon, true));
    }

    @Test
    public void testCanEncode() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        for (CoreCharset charset : CoreCharset.values()) {
            for (char character = 0; character < 0xFFFF; character++) {
                boolean canEncode = Entities.canEncode(charset, character, encoder);
                if (charset == CoreCharset.ascii) {
                    assertTrue("Character " + (int) character + " can't be encoded in ASCII.", canEncode || (character < 0x80));
                } else {
                    assertTrue("Character " + (int) character + " can't be encoded in " + charset.name() + ".", canEncode);
                }
            }
        }
    }
}