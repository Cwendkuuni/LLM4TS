package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.CoreCharset;
import org.jsoup.nodes.Entities.EscapeMode;

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
        assertTrue(entities.isBaseNamedEntity("lt"));
        assertFalse(entities.isBaseNamedEntity("gt"));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        assertEquals('<', entities.getCharacterByName("lt"));
    }

    @Test
    public void testEscapeBasic() {
        Entities entities = new Entities();
        String input = "This is a <test> with & some &amp; entities!";
        String output = entities.escape(input, null);
        String expectedOutput = "This is a &lt;test&gt; with &amp;amp; entities!";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeWithXHTML() {
        Entities entities = new Entities();
        String input = "This is a <test> with & some &amp; entities!";
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.setEscapeMode(Entities.EscapeMode.xhtml);
        String output = entities.escape(input, settings);
        String expectedOutput = "This is a &lt;test&gt; with &amp; entities!";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeWithUTF8() {
        Entities entities = new Entities();
        String input = "This is a åäö test";
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.setEncoder("UTF-8");
        String output = entities.escape(input, settings);
        String expectedOutput = "This is a &#x00E5;&#x00E4;&#x00F6; test";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescapeBasic() {
        Entities entities = new Entities();
        String input = "This is a &lt;test&gt; with &amp; entities!";
        String output = entities.unescape(input, false);
        String expectedOutput = "This is a <test> with & entities!";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescapeStrict() {
        Entities entities = new Entities();
        String input = "This is a &lt;test&gt; with &amp; entities!";
        String output = entities.unescape(input, true);
        String expectedOutput = "This is a <test>&amp;</test> with & entities!";
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanEncodeException() {
        Entities entities = new Entities();
        Document.OutputSettings settings = new Document.OutputSettings();
        CoreCharset charset = Entities.CoreCharset.ascii;
        char c = '\u00DE'; // 'Ü'
        assertFalse(entities.canEncode(charset, c, settings.getEncoder()));
    }

    @Test
    public void testCanEncodeASCII() {
        Entities entities = new Entities();
        Document.OutputSettings settings = new Document.OutputSettings();
        CoreCharset charset = Entities.CoreCharset.ascii;
        char c = 'A';
        assertTrue(entities.canEncode(charset, c, settings.getEncoder()));
    }

    @Test
    public void testCanEncodeUTF() {
        Entities entities = new Entities();
        Document.OutputSettings settings = new Document.OutputSettings();
        CoreCharset charset = Entities.CoreCharset.utf;
        char c = '\u00DE'; // 'Ü'
        assertTrue(entities.canEncode(charset, c, settings.getEncoder()));
    }
}