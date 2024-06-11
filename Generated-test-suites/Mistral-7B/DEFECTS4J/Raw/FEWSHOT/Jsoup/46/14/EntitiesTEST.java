package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
            assertTrue(Entities.isNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }

        Map<Character, String> base = Entities.baseByVal;
        for (Map.Entry<Character, String> entry : base.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue();
            assertTrue(Entities.isBaseNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }

        String invalidEntity = "invalidEntity";
        assertFalse(Entities.isNamedEntity(invalidEntity));
        assertNull(Entities.getCharacterByName(invalidEntity));
    }

    @Test
    public void testCanEncode() {
        CoreCharset ascii = CoreCharset.ascii;
        for (char c = 0; c < 128; c++) {
            assertTrue(Entities.canEncode(ascii, c, null));
        }

        CoreCharset utf = CoreCharset.utf;
        for (char c = 0; c < Character.MAX_VALUE; c++) {
            assertTrue(Entities.canEncode(utf, c, null));
        }

        CharsetEncoder fallback = Character.encodingForName("UTF-8");
        for (char c = Character.MIN_SUPPLEMENTARY_CODE_POINT; c <= Character.MAX_VALUE; c++) {
            if (Character.isSupplementaryCharacter(c)) {
                assertFalse(Entities.canEncode(utf, c, fallback));
            } else {
                assertTrue(Entities.canEncode(utf, c, fallback));
            }
        }
    }

    @Test
    public void testEscape() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String input = "&amp;<lt>foo<gt>bar&quot;";
        String expectedOutput = "&amp;amp; &lt;lt&gt; foo &gt;bar&quot; &quot;";
        assertEquals(expectedOutput, Entities.escape(input, outputSettings));

        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        expectedOutput = "&amp; &lt;foo&gt; &gt;bar&quot;";
        assertEquals(expectedOutput, Entities.escape(input, outputSettings));
    }

    @Test
    public void testUnescape() {
        String input = "&amp;amp; &lt;lt&gt; foo &gt;bar&quot; &quot;";
        assertEquals("&amp;<lt>foo<gt>bar&quot;", Entities.unescape(input));

        String strictInput = "&amp;amp; &lt;lt&gt; foo &gt;bar&quot; &quot; &";
        assertEquals("&amp;<lt>foo<gt>bar&quot;", Entities.unescape(strictInput, true));
    }
}