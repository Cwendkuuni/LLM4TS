package org.jsoup.nodes.test;

import org.jsoup.nodes.*;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Before;
import org.junit.Test;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class EntitiesTest {
    private Entities.EscapeMode escapeMode;
    private Entities entities;

    @Before
    public void setup() {
        escapeMode = Entities.EscapeMode.xhtml;
        entities = new Entities();
    }

    @Test
    public void testIsNamedEntity() {
        assertTrue(entities.isNamedEntity("lt"));
        assertTrue(entities.isNamedEntity("gt"));
        assertTrue(entities.isNamedEntity("amp"));
        assertTrue(entities.isNamedEntity("apos"));
        assertTrue(entities.isNamedEntity("quot"));

        assertFalse(entities.isNamedEntity("fake"));
        assertFalse(entities.isNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('\"'), entities.getCharacterByName("quot"));

        assertNull(entities.getCharacterByName("fake"));
        assertNull(entities.getCharacterByName(""));
    }

    @Test
    public void testEscape() {
        String string = "The cat & dog <jump>!";
        String expected = "The cat &amp; dog &lt;jump&gt;!";
        String result = entities.escape(string, escapeMode);
        assertEquals(expected, result);
    }

    @Test
    public void testEscapeWithCharsetEncoder() {
        String string = "The cat & dog <jump>!";
        String expected = "The cat &amp; dog &lt;jump&gt;!";
        CharsetEncoder encoder = Charset.defaultCharset().newEncoder();
        String result = entities.escape(string, encoder, escapeMode);
        assertEquals(expected, result);
    }

    @Test
    public void testEscapeWithNullEncoder() {
        String string = "The cat & dog <jump>!";
        String expected = "The cat &amp; dog &lt;jump&gt;!";
        CharsetEncoder encoder = null;
        String result = entities.escape(string, encoder, escapeMode);
        assertEquals(expected, result);
    }

    @Test
    public void testUnescape() {
        String string = "The cat &amp; dog &lt;jump&gt;!";
        String expected = "The cat & dog <jump>!";
        String result = entities.unescape(string);
        assertEquals(expected, result);
    }

    @Test
    public void testUnescapeWithStrictFalse() {
        String string = "The cat &amp dog &lt;jump&gt;!";
        String expected = "The cat & dog <jump>!";
        String result = entities.unescape(string, false);
        assertEquals(expected, result);
    }

    @Test
    public void testUnescapeWithStrictTrue() {
        String string = "The cat &amp; dog &lt;jump&gt!";
        String expected = "The cat & dog <jump>!";
        String result = entities.unescape(string, true);
        assertEquals(expected, result);
    }

}