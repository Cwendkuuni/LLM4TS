package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("quot"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;&gt;&amp;&quot;", Entities.escape("<>&\"", new Document.OutputSettings()));
    }

    @Test
    public void testEscape_inAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        assertEquals("<>&\"", Entities.escape("<>&\"", outputSettings));
    }

    @Test
    public void testEscape_normaliseWhite_stripLeadingWhite() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.base);
        StringBuilder sb = new StringBuilder();
        Entities.escape(sb, "     Test", outputSettings, false, true, true);
        assertEquals(" Test", sb.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("<>&\"", Entities.unescape("&lt;&gt;&amp;&quot;"));
    }

    @Test
    public void testUnescape_strict() {
        assertEquals("<>&\"", Entities.unescape("&lt;&gt;&amp;&quot;", true));
    }

    @Test
    public void testEscape_extensive() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals("&quot;", Entities.escape("\"", outputSettings));
        assertEquals("&apos;", Entities.escape("'", outputSettings));
        assertEquals("&nbsp;", Entities.escape(" ", outputSettings));
    }

    @Test
    public void testEscape_extensive_inAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals("\"", Entities.escape("\"", outputSettings));
        assertEquals("'", Entities.escape("'", outputSettings));
        assertEquals(" ", Entities.escape(" ", outputSettings));
    }

    @Test
    public void testEscape_extensive_normaliseWhite_stripLeadingWhite() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        StringBuilder sb = new StringBuilder();
        Entities.escape(sb, "     Test", outputSettings, false, true, true);
        assertEquals("     Test", sb.toString());
    }
    
    @Test
    public void testEscape_xhtml() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        assertEquals("&#x26;", Entities.escape("&", outputSettings));
        assertEquals("&#x3C;", Entities.escape("<", outputSettings));
        assertEquals("&#x3E;", Entities.escape(">", outputSettings));
        assertEquals("&quot;", Entities.escape("\"", outputSettings));
    }

    @Test
    public void testEscapeMode_getMap() {
        assertEquals(Entities.EscapeMode.xhtml.getMap(), Entities.EscapeMode.xhtml.getMap());
        assertEquals(Entities.EscapeMode.base.getMap(), Entities.EscapeMode.base.getMap());
        assertEquals(Entities.EscapeMode.extended.getMap(), Entities.EscapeMode.extended.getMap());
    }
    
}