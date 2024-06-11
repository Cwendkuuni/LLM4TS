package org.jsoup.nodes;

import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("abc"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("nbsp"));
        assertFalse(Entities.isBaseNamedEntity("abc"));
        assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("abc"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings()));
        assertEquals("&gt;", Entities.escape(">", new Document.OutputSettings()));
        assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings()));
        assertEquals("&quot;", Entities.escape("\"", new Document.OutputSettings()));
        assertEquals("&nbsp;", Entities.escape(" ", new Document.OutputSettings()));
    }

    @Test
    public void testEscape_inAttribute() {
        Document.OutputSettings settings = new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml);
        assertEquals("<", Entities.escape("<", settings));
        assertEquals(">", Entities.escape(">", settings));
        assertEquals("&", Entities.escape("&", settings));
        assertEquals("\"", Entities.escape("\"", settings));
        assertEquals(" ", Entities.escape(" ", settings));
    }

    @Test
    public void testEscape_normaliseWhite() {
        Document.OutputSettings settings = new Document.OutputSettings().normaliseWhite(true);
        assertEquals("&lt;", Entities.escape("<", settings));
        assertEquals("&gt;", Entities.escape(">", settings));
        assertEquals("&amp;", Entities.escape("&", settings));
        assertEquals("&quot;", Entities.escape("\"", settings));
        assertEquals(" ", Entities.escape(" ", settings));
    }

    @Test
    public void testEscape_stripLeadingWhite() {
        Document.OutputSettings settings = new Document.OutputSettings().stripLeadingWhite(true);
        assertEquals("&lt;", Entities.escape("<", settings));
        assertEquals("&gt;", Entities.escape(">", settings));
        assertEquals("&amp;", Entities.escape("&", settings));
        assertEquals("&quot;", Entities.escape("\"", settings));
        assertEquals("", Entities.escape(" ", settings));
    }

    @Test
    public void testEscape_allParams() {
        Document.OutputSettings settings = new Document.OutputSettings()
                .escapeMode(Entities.EscapeMode.xhtml)
                .normaliseWhite(true)
                .stripLeadingWhite(true);
        assertEquals("<", Entities.escape("<", settings));
        assertEquals(">", Entities.escape(">", settings));
        assertEquals("&", Entities.escape("&", settings));
        assertEquals("\"", Entities.escape("\"", settings));
        assertEquals("", Entities.escape(" ", settings));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("<>", Entities.unescape("&lt;&gt;"));
    }

    @Test
    public void testUnescape_strict() {
        assertEquals("<", Entities.unescape("&lt;", true));
        assertEquals(">", Entities.unescape("&gt;", true));
        assertEquals("&", Entities.unescape("&amp;", true));
        assertEquals("\"", Entities.unescape("&quot;", true));
        assertEquals("<>", Entities.unescape("&lt;&gt;", true));
    }

    @Test
    public void testUnescape_notStrict() {
        assertEquals("<", Entities.unescape("&lt;", false));
        assertEquals(">", Entities.unescape("&gt;", false));
        assertEquals("&", Entities.unescape("&amp;", false));
        assertEquals("\"", Entities.unescape("&quot;", false));
        assertEquals("<>", Entities.unescape("&lt;&gt;", false));
    }
}