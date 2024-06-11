package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Entities;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for Entities class.
 */
public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        String namedEntity = "lt";
        assertTrue(entities.isNamedEntity(namedEntity));

        String nonNamedEntity = "abc";
        assertFalse(entities.isNamedEntity(nonNamedEntity));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        String baseNamedEntity = "amp";
        assertTrue(entities.isBaseNamedEntity(baseNamedEntity));

        String nonBaseNamedEntity = "xyz";
        assertFalse(entities.isBaseNamedEntity(nonBaseNamedEntity));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        String namedEntity = "lt";
        Character character = entities.getCharacterByName(namedEntity);
        assertEquals('<', character.charValue());

        String nonExistentNamedEntity = "def";
        assertNull(entities.getCharacterByName(nonExistentNamedEntity));
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        String input = "& < > \"";
        String expectedOutput = "&amp; &lt; &gt; &quot;";

        StringBuilder output = new StringBuilder();
        entities.escape(output, input, null, false, false, false);
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String input = "&amp; &lt; &gt; &quot;";
        String expectedOutput = "& < > \"";

        String output = entities.unescape(input, false);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithNullInput() {
        Entities entities = new Entities();
        String nullInput = null;
        entities.unescape(nullInput, false);
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();
        char asciiChar = 'A';
        CoreCharset asciiCharset = Entities.CoreCharset.ascii;
        assertTrue(entities.canEncode(asciiCharset, asciiChar, null));

        char nonAsciiChar = '\u00E1';
        CoreCharset utfCharset = Entities.CoreCharset.utf;
        assertFalse(entities.canEncode(utfCharset, nonAsciiChar, null));
    }

    @Test
    public void testIsNamedEntityWithUppercaseName() {
        Entities entities = new Entities();
        String namedEntity = "LT";
        assertFalse(entities.isNamedEntity(namedEntity));
    }

    @Test
    public void testUnescapeWithEmptyString() {
        Entities entities = new Entities();
        String input = "";
        String expectedOutput = "";

        String output = entities.unescape(input, false);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescapeWithEmptyStringAndStrict() {
        Entities entities = new Entities();
        String input = "";

        String output = entities.unescape(input, true);
        assertTrue(output.isEmpty());
    }

    @Test
    public void testEscapeWithMultipleCharacters() {
        Entities entities = new Entities();
        String input = "&<>";
        String expectedOutput = "&amp; &lt; &gt;";

        StringBuilder output = new StringBuilder();
        entities.escape(output, input, null, false, false, false);
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testEscapeWithStringContainingSurrogatePairs() {
        Entities entities = new Entities();
        String input = "𠮷𠮹";
        String expectedOutput = "&#x20BB7;&#x20BB8;";

        StringBuilder output = new StringBuilder();
        entities.escape(output, input, null, false, false, false);
        assertEquals(expectedOutput, output.toString());
    }
}