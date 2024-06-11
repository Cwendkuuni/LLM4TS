package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.jsoup.parser.Parser;
import org.jsoup.nodes.Entities;

import java.nio.charset.CharsetEncoder;
import java.util.Map;

import org.junit.Test;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("foo"));
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("nbsp"));
        assertFalse(Entities.isBaseNamedEntity("foo"));
        assertFalse(Entities.isBaseNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals((Character) '<', Entities.getCharacterByName("lt"));
        assertEquals((Character) '>', Entities.getCharacterByName("gt"));
        assertEquals((Character) '&', Entities.getCharacterByName("amp"));
        assertEquals((Character) '"', Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("nbsp"));
        assertNull(Entities.getCharacterByName("foo"));
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscape() {
        String result = Entities.escape("<html>", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        assertEquals("&lt;html&gt;", result);

        result = Entities.escape("&nbsp;&nbsp;", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        assertEquals("&amp;nbsp;&amp;nbsp;", result);
    }

    @Test
    public void testUnescape() {
        String result = Entities.unescape("&lt;html&gt;");
        assertEquals("<html>", result);

        result = Entities.unescape("&amp;nbsp;&amp;nbsp;");
        assertEquals("&nbsp;&nbsp;", result);
    }

    @Test
    public void testEscape_and_unescape() {
        String text = "<html>&nbsp;&lt;&gt;&amp;&quot;</html>";

        String escapedText = Entities.escape(text, new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        String unescapedText = Entities.unescape(escapedText);

        assertEquals(text, unescapedText);
    }
    
    @Test
    public void testEscape_withOutputSettings() {
        String result = Entities.escape("<html>", new Document.OutputSettings().charset(CharsetEncoder.forName("UTF-8")).escapeMode(Entities.EscapeMode.xhtml));
        assertEquals("&lt;html&gt;", result);

        result = Entities.escape("<123>", new Document.OutputSettings().charset(CharsetEncoder.forName("UTF-8")).escapeMode(Entities.EscapeMode.xhtml));
        assertEquals("<123>", result);
    }

    @Test
    public void testEscape_withInAttribute() {
        String result = Entities.escape("<html>", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        assertEquals("&lt;html&gt;", result);

        result = Entities.escape("&nbsp;&nbsp;", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        assertEquals("&amp;nbsp;&amp;nbsp;", result);
    }

    @Test
    public void testEscape_withNormaliseWhite() {
        String result = Entities.escape("<html>", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        assertEquals("&lt;html&gt;", result);

        result = Entities.escape("&nbsp;&nbsp;", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        assertEquals("&amp;nbsp;&amp;nbsp;", result);
    }

    @Test
    public void testEscape_withStripLeadingWhite() {
        String result = Entities.escape("<html>", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        assertEquals("&lt;html&gt;", result);

        result = Entities.escape("&nbsp;&nbsp;", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        assertEquals("&amp;nbsp;&amp;nbsp;", result);
    }

    @Test
    public void testEscape_withInvalidEntity() {
        String result = Entities.escape("&invalid;", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        assertEquals("&invalid;", result);
    }

    @Test
    public void testEscape_withCodePoint() {
        String result = Entities.escape("&#65;", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        assertEquals("&amp;#65;", result);
    }

    @Test
    public void testEscape_withSupplementaryCharacter() {
        String result = Entities.escape("&#x1F602;", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        assertEquals("&amp;#x1F602;", result);
    }
}