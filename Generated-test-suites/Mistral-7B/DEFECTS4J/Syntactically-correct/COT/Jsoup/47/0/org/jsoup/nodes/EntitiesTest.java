package org.jsoup.nodes;

import static org.junit.Assert.*;

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
        assertFalse(entities.isNamedEntity("foo"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isBaseNamedEntity("amp"));
        assertFalse(entities.isBaseNamedEntity("gt"));
        assertFalse(entities.isBaseNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        assertEquals('<', entities.getCharacterByName("lt"));
        assertEquals('&', entities.getCharacterByName("amp"));
        assertNull(entities.getCharacterByName("foo"));
    }

    @Test
    public void testEscapeBasic() {
        Entities entities = new Entities();
        String input = "& < > \"";
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("&amp; &lt; &gt; &quot;");

        String output = entities.escape(input, null);
        assertEquals(expectedOutput.toString(), output);
    }

    @Test
    public void testEscapeInAttribute() {
        Entities entities = new Entities();
        String input = "& < > \"";
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("&amp; &lt; &gt; &quot;");

        String output = entities.escape(input, null, true, false, false, false);
        assertEquals(input, output); // attributes don't need to be escaped in HTML
    }

    @Test
    public void testEscapeWithXHTML() {
        Entities entities = new Entities();
        Entities.EscapeMode xhtmlMode = Entities.EscapeMode.xhtml;

        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("&amp; &lt; &gt; &quot;");

        String output = entities.escape("& < > \"", xhtmlMode);
        assertEquals(expectedOutput.toString(), output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeWithNullInput() {
        Entities entities = new Entities();
        entities.escape(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeWithNullOutputSettings() {
        Entities entities = new Entities();
        entities.escape(null, null, null, true, false, false);
    }

    @Test
    public void testCanEncodeASCII() {
        Entities entities = new Entities();
        assertTrue(entities.canEncode(Entities.CoreCharset.ascii, 'A'));
        assertFalse(entities.canEncode(Entities.CoreCharset.ascii, 'ñ'));
    }

    @Test
    public void testCanEncodeUTF() {
        Entities entities = new Entities();
        assertTrue(entities.canEncode(Entities.CoreCharset.utf, 'ñ'));
        assertTrue(entities.canEncode(Entities.CoreCharset.utf, '😃'));
    }

    @Test
    public void testCanEncodeWithFallback() {
        Entities entities = new Entities();
        CharsetEncoder fallbackEncoder = Charset.forName("UTF-8").newEncoder();
        assertTrue(entities.canEncode(Entities.CoreCharset.fallback, 'ñ', fallbackEncoder));
        assertTrue(entities.canEncode(Entities.CoreCharset.fallback, '😃', fallbackEncoder));
    }
}