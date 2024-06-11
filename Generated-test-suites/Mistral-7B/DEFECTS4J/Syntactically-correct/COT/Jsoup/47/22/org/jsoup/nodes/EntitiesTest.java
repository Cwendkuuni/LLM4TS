package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.parser.Parser;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        String lt = "lt";
        assertTrue(entities.isNamedEntity(lt));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        String lt = "lt";
        assertFalse(entities.isBaseNamedEntity(lt));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        String lt = "lt";
        Character character = entities.getCharacterByName(lt);
        assertEquals('<', character.charValue());
    }

    @Test
    public void testEscapeMethodBasic() {
        Entities entities = new Entities();
        String input = "& < > \"";
        Document.OutputSettings out = new Document.OutputSettings();
        String expectedOutput = "&amp; &lt; &gt; &quot;";
        String output = entities.escape(input, out);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeMethodEdgeCases() {
        Entities entities = new Entities();

        // Test with an empty string
        String input = "";
        Document.OutputSettings out = new Document.OutputSettings();
        String expectedOutput = "";
        String output = entities.escape(input, out);
        assertEquals(expectedOutput, output);

        // Test with a single character that can be encoded
        char c = 'A';
        input = Character.toString(c);
        out = new Document.OutputSettings();
        expectedOutput = Character.toString(c);
        output = entities.escape(input, out);
        assertEquals(expectedOutput, output);

        // Test with a character that requires encoding
        char nonAsciiChar = 0x00E1; // Latin small letter a with grave
        input = Character.toString(nonAsciiChar);
        out = new Document.OutputSettings();
        expectedOutput = "&#x00E1;";
        output = entities.escape(input, out);
        assertEquals(expectedOutput, output);

        // Test with a character that needs special treatment (surrogate pairs)
        char highSurrogate = 'D';
        char lowSurrogate = 'd';
        String inputWithSurrogatePair = Character.toString(highSurrogate) + Character.toString(lowSurrogate);
        out = new Document.OutputSettings();
        expectedOutput = Character.toString(Character.toCodePoint(highSurrogate, lowSurrogate));
        output = entities.escape(inputWithSurrogatePair, out);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodInvalidArgument() {
        Entities entities = new Entities();
        String input = null;
        Document.OutputSettings out = new Document.OutputSettings();
        entities.escape(input, out);
    }

    @Test
    public void testUnescapeMethodBasic() {
        Entities entities = new Entities();
        String input = "&amp; &lt; &gt; &quot;";
        String expectedOutput = "& < > \"";
        String output = entities.unescape(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescapeMethodEdgeCases() {
        Entities entities = new Entities();

        // Test with an empty string
        String input = "";
        String expectedOutput = "";
        String output = entities.unescape(input);
        assertEquals(expectedOutput, output);

        // Test with a character that can be decoded
        char c = 'A';
        input = "&#x41;";
        expectedOutput = Character.toString(c);
        output = entities.unescape(input, false);
        assertEquals(expectedOutput, output);

        // Test with a character that requires decoding
        char nonAsciiChar = 0x00E1; // Latin small letter a with grave
        input = "&#x00E1;";
        expectedOutput = Character.toString(nonAsciiChar);
        output = entities.unescape(input, false);
        assertEquals(expectedOutput, output);

        // Test with a character that needs special treatment (surrogate pairs)
        char highSurrogate = 'D';
        char lowSurrogate = 'd';
        String inputWithSurrogatePair = "&#x" + Integer.toHexString(Character.codePointAt(highSurrogate, 0)) + "&#x" + Integer.toHexString(Character.codePointAt(lowSurrogate, 0)) + "";
        expectedOutput = Character.toString(Character.toCodePoint(highSurrogate, lowSurrogate));
        output = entities.unescape(inputWithSurrogatePair, false);
        assertEquals(expectedOutput, output);

        // Test with a character that needs strict decoding
        char ampersand = '&';
        input = "&amp;";
        expectedOutput = Character.toString(ampersand);
        output = entities.unescape(input, true);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCanEncodeMethodBasic() {
        Entities entities = new Entities();
        CoreCharset ascii = Entities.CoreCharset.ascii;
        char c = 'A';
        assertTrue(entities.canEncode(ascii, c, null));
    }

    @Test
    public void testCanEncodeMethodEdgeCases() {
        Entities entities = new Entities();

        // Test with a character outside ASCII range
        CoreCharset ascii = Entities.CoreCharset.ascii;
        char nonAsciiChar = 0x00E1; // Latin small letter a with grave
        assertFalse(entities.canEncode(ascii, nonAsciiChar, null));

        // Test with a character inside UTF-8 range
        CoreCharset utf = Entities.CoreCharset.utf;
        char utfChar = '€';
        assertTrue(entities.canEncode(utf, utfChar, null));
    }
}