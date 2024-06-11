package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Parser;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isNamedEntity("lt"));
        assertFalse(entities.isNamedEntity("gt"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isBaseNamedEntity("amp"));
        assertFalse(entities.isBaseNamedEntity("lt"));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        assertEquals('<', entities.getCharacterByName("lt"));
        assertEquals('&', entities.getCharacterByName("amp"));
    }

    @Test
    public void testEscapeMethod_BasicFunctionality() {
        Entities entities = new Entities();
        String input = "Hello <world> & this is a test!";
        String expectedOutput = "Hello &lt;world&gt; &amp; this is a test!";
        String output = entities.escape(input, null);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeMethod_EdgeCases() {
        Entities entities = new Entities();

        // Test escape mode
        String input = "&amp; &lt; &gt; &quot;";
        String expectedOutput = "&amp; &amp; &amp; &amp;";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setEscapeMode(Entities.EscapeMode.xhtml);
        String output = entities.escape(input, outputSettings);
        assertEquals(expectedOutput, output);

        // Test encoder
        input = "😃";
        String encoding = "UTF-8";
        CharsetEncoder encoder = Charset.forName(encoding).newEncoder();
        expectedOutput = "&#x1F600;";
        output = entities.escape(input, null, encoder);
        assertEquals(expectedOutput, output);

        // Test surrogate pairs
        input = "\uD840\uDC00\uD840\uDC34";
        expectedOutput = "&#x10000&#x10FF34;";
        output = entities.escape(input, null);
        assertEquals(expectedOutput, output);

        // Test whitespace handling
        input = "   Hello   World   ";
        String normaliseWhite = true;
        outputSettings = new Document.OutputSettings();
        outputSettings.setNormaliseWhiteSpace(normaliseWhite);
        expectedOutput = " Hello   World   ";
        output = entities.escape(input, outputSettings);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethod_IllegalArgumentException() {
        Entities entities = new Entities();
        String invalidArgument = null;
        entities.escape(invalidArgument, null);
    }

    @Test
    public void testUnescapeMethod_BasicFunctionality() {
        Entities entities = new Entities();
        String input = "&lt;Hello&gt; World &amp; this is a test!";
        String expectedOutput = "<Hello> World & this is a test!";
        String output = entities.unescape(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescapeMethod_EdgeCases() {
        Entities entities = new Entities();

        // Test escaped Unicode characters
        String input = "&#x1F600;";
        String expectedOutput = "😃";
        String output = entities.unescape(input);
        assertEquals(expectedOutput, output);

        // Test entities in different encodings
        String encoding = "UTF-8";
        CharsetEncoder encoder = Charset.forName(encoding).newEncoder();
        input = "&#x10000&#x10FF34;";
        expectedOutput = "\uD840\uDC00\uD840\uDC34";
        output = entities.unescape(input, encoder);
        assertEquals(expectedOutput, output);

        // Test strict unescaping
        input = "&lt;Hello&gt; World &amp; this is a test!";
        boolean strict = true;
        output = entities.unescape(input, strict);
        assertEquals("<Hello> World &amp; this is a test!", output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeMethod_IllegalArgumentException() {
        Entities entities = new Entities();
        String invalidArgument = null;
        entities.unescape(invalidArgument);
    }
}