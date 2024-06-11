package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        Map<String, Character> full = entities.Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String key = entry.getKey();
            Character value = entry.getValue();
            assertTrue("Named entity " + key + " should exist", entities.Entities.isNamedEntity(key));
            assertTrue("Named entity " + key + " should map to character " + value,
                    entities.Entities.getCharacterByName(key).equals(value));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        Map<Character, String> baseByVal = entities.Entities.baseByVal;
        for (Map.Entry<Character, String> entry : baseByVal.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            assertTrue("Base named entity " + value + " should exist", entities.Entities.isBaseNamedEntity(value));
            assertTrue("Base named entity " + value + " should map to character " + key,
                    entities.Entities.getCharacterByName(value).equals(key));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        Map<Character, String> full = entities.Entities.full;
        Map<Character, String> baseByVal = entities.Entities.baseByVal;
        Map<Character, String> xhtmlByVal = entities.Entities.xhtmlByVal;

        for (Map.Entry<Character, String> entry : full.entrySet()) {
            Character key = entry.getValue();
            String name = entry.getKey();
            assertEquals("Named entity " + name + " should map to character " + key, key,
                    entities.Entities.getCharacterByName(name));
        }

        for (Map.Entry<Character, String> entry : baseByVal.entrySet()) {
            Character key = entry.getValue();
            String name = entry.getKey();
            assertEquals("Base named entity " + name + " should map to character " + key, key,
                    entities.Entities.getCharacterByName(name));
        }

        assertEquals("lt", entities.Entities.getCharacterByName("lt"));
        assertEquals("gt", entities.Entities.getCharacterByName("gt"));
        assertEquals("amp", entities.Entities.getCharacterByName("amp"));
        assertEquals("quot", entities.Entities.getCharacterByName("quot"));
        assertEquals("nbsp", entities.Entities.getCharacterByName("nbsp"));

        assertNull("Unknown named entity should return null", entities.Entities.getCharacterByName("unknown"));

        // Test XHTML specific entities
        for (Map.Entry<Character, String> entry : xhtmlByVal.entrySet()) {
            Character key = entry.getValue();
            String name = entry.getKey();
            assertEquals("XHTML specific named entity " + name + " should map to character " + key, key,
                    entities.Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();

        String input = "& < > \" ' \n\t\r\f &amp; &lt; &gt; &quot; &apos; &nbsp; &iexcl; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot;";
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);
        String expectedOutput = "&amp; &amp;lt; &amp;gt; &amp;quot; &amp;apos; &amp;#160; &iexcl; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot;";

        String output = entities.escape(input, out);
        assertEquals("Input should be escaped to output", expectedOutput, output);

        // Test different output modes
        out.escapeMode(Entities.EscapeMode.base);
        output = entities.escape(input, out);
        assertNotEquals("Output mode changed, output should not be equal to XHTML mode output", expectedOutput, output);

        out.escapeMode(Entities.EscapeMode.extended);
        output = entities.escape(input, out);
        assertNotEquals("Output mode changed, output should not be equal to XHTML mode output", expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();

        String input = "&amp; &lt; &gt; &quot; &apos; &nbsp; &iexcl; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot;";
        String expectedOutput = "& < > \" ' \n\t\r\f &amp; &lt; &gt; &quot; &apos; &nbsp; &iexcl; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot;";

        String output = entities.unescape(input);
        assertEquals("Input should be unescaped to output", expectedOutput, output);
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();

        final CoreCharset utf = CoreCharset.utf;
        final CharsetEncoder encoder = Character.encodingForName("UTF-8");

        for (char c = 0; c < 256; c++) {
            if (Character.isISOControl(c)) continue;

            boolean canEncode = entities.canEncode(utf, c, encoder);
            boolean expected = (c < 128);
            assertTrue("Character " + (int) c + " should be encodable as " + (canEncode == expected), canEncode == expected);
        }

        // Special cases: surrogate pairs
        char c1 = 0xD800;
        char c2 = 0xDC00;
        boolean canEncode = entities.canEncode(utf, c1, encoder);
        assertTrue("Character " + (int) c1 + " should not be encodable as " + (!canEncode), !canEncode);

        canEncode = entities.canEncode(utf, c2, encoder);
        assertTrue("Character " + (int) c2 + " should not be encodable as " + (!canEncode), !canEncode);

        canEncode = entities.canEncode(utf, c1 + c2, encoder);
        assertTrue("Surrogate pair " + (int) (c1) + (int) (c2) + " should be encodable as " + (canEncode), canEncode);
    }
}