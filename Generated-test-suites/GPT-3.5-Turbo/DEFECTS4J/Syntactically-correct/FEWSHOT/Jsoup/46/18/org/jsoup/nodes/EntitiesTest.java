package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.Map;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("Aacute"));
        assertTrue(Entities.isNamedEntity("Euml"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("Aacute"));
        assertFalse(Entities.isBaseNamedEntity("Euml"));
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        Charater ampChar = Character.valueOf('&');
        Charater ltChar = Character.valueOf('<');
        Charater gtChar = Character.valueOf('>');
        Charater quotChar = Character.valueOf('"');
        Charater aacuteChar = Character.valueOf((char) 0x00C1);

        assertEquals(ampChar, Entities.getCharacterByName("amp"));
        assertEquals(ltChar, Entities.getCharacterByName("lt"));
        assertEquals(gtChar, Entities.getCharacterByName("gt"));
        assertEquals(quotChar, Entities.getCharacterByName("quot"));
        assertEquals(aacuteChar, Entities.getCharacterByName("Aacute"));
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);

        assertEquals("&lt;", Entities.escape("<", out));
        assertEquals("&gt;", Entities.escape(">", out));
        assertEquals("&amp;", Entities.escape("&", out));
        assertEquals("&quot;", Entities.escape("\"", out));
        assertEquals("&nbsp;", Entities.escape("\u00A0", out));

        assertEquals("&lt;&gt;&amp;&quot;", Entities.escape("<>&\"", out));
    }

    @Test
    public void testUnescape() {
        assertEquals("<>", Entities.unescape("&lt;&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("\u00A0", Entities.unescape("&nbsp;"));
    }

    @Test
    public void testUnescapeStrict() {
        assertEquals("<>", Entities.unescape("&#x3C;&#x3E;", true));
        assertEquals("<>", Entities.unescape("&#60;&#62;", true));

        assertEquals("a", Entities.unescape("&#97;"));
        assertEquals("a", Entities.unescape("&#097;"));
        assertEquals("a", Entities.unescape("&#x61;"));
        assertEquals("a", Entities.unescape("&#x061;"));

        assertEquals("hello", Entities.unescape("hello"));
        assertEquals("hello", Entities.unescape("hello;", true));
        assertEquals("hello;", Entities.unescape("hello;", false));
    }
}