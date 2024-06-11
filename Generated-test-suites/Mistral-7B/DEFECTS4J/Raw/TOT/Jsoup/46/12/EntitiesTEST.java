package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isNamedEntity("lt"));
        assertTrue(entities.isNamedEntity("amp"));
        assertTrue(entities.isNamedEntity("quot"));
        assertFalse(entities.isNamedEntity("x"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isBaseNamedEntity("lt"));
        assertTrue(entities.isBaseNamedEntity("amp"));
        assertTrue(entities.isBaseNamedEntity("quot"));
        assertFalse(entities.isBaseNamedEntity("gt"));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        assertEquals('<', entities.getCharacterByName("lt"));
        assertEquals('&', entities.getCharacterByName("amp"));
        assertEquals('"', entities.getCharacterByName("quot"));
        assertNull(entities.getCharacterByName("x"));
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        String input = "& < > \"";
        Document.OutputSettings out = new Document.OutputSettings();
        String expected = "&amp; &lt; &gt; &quot;";
        assertEquals(expected, entities.escape(input, out));

        out = new Document.OutputSettings(Entities.EscapeMode.XHTML);
        expected = "&amp; &lt; &gt; &quot;";
        assertEquals(expected, entities.escape(input, out));

        out = new Document.OutputSettings(Entities.EscapeMode.EXTENDED);
        expected = "&amp;lt; &amp;gt; &amp;quot; &amp;#x26; &amp;#x3C; &amp;#x3E; &amp;#x22;";
        assertEquals(expected, entities.escape(input, out));
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String input = "&amp;lt; &amp;gt; &amp;quot; &amp;#x3C; &amp;#x3E; &amp;#x22;";
        assertEquals("< > \"<>", entities.unescape(input));
        assertEquals("< > \"<>", entities.unescape(input, true));
    }
}