package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Parser;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("foo"));
        assertFalse(Entities.isNamedEntity("bar"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("apos"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("foo"));
        assertFalse(Entities.isBaseNamedEntity("bar"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('\"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("foo"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings()));
        assertEquals("&gt;", Entities.escape(">", new Document.OutputSettings()));
        assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings()));
        assertEquals("&apos;", Entities.escape("'", new Document.OutputSettings()));
        assertEquals("&quot;", Entities.escape("\"", new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("'", Entities.unescape("&apos;"));
        assertEquals("\"", Entities.unescape("&quot;"));
    }
    
    @Test
    public void testEscape_withNormaliseWhite() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.normaliseWhite(true);
        assertEquals(" ", Entities.escape(" ", settings));
        assertEquals(" ", Entities.escape("  ", settings));
        assertEquals("  ", Entities.escape("   ", settings));
        assertEquals("   ", Entities.escape("    ", settings));
    }
    
    @Test
    public void testEscape_stripLeadingWhite() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.stripLeadingWhite(true);
        assertEquals("&nbsp;", Entities.escape(" ", settings));
        assertEquals("&nbsp;", Entities.escape("  ", settings));
        assertEquals("&nbsp; ", Entities.escape("   ", settings));
        assertEquals("&nbsp;   ", Entities.escape("    ", settings));
    }
    
    @Test
    public void testEscape_inAttribute() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.prettyPrint(true);
        settings.charset("UTF-8");
        settings.escapeMode(Entities.EscapeMode.xhtml);
        assertEquals("&#x61;&#x62;&#x63;", Entities.escape("abc", settings));
        assertEquals("&lt;&#x61;&apos;&#x62;&apos;&#x63;&gt;", Entities.escape("<a'b'c>", settings));
        assertEquals("\"&lt;&#x61;&#x62;&#x63;&gt;\"", Entities.escape("\"<abc>\"", settings));
        assertEquals("&lt;&#x61;&#x62;&#x63;&gt;", Entities.escape("<abc>", settings));
        assertEquals("&lt;&gt;&amp;&quot;", Entities.escape("<>&\"", settings));
    }
    
    @Test
    public void testUnescape_strict() {
        assertTrue(Entities.unescape("Test", true).isEmpty());
        assertTrue(Entities.unescape("This is a test", true).isEmpty());
        assertTrue(Entities.unescape("This is a test ", true).isEmpty());
        assertEquals("&lt;", Entities.unescape("&lt;", true));
        assertEquals("&gt;", Entities.unescape("&gt;", true));
        assertEquals("&amp;", Entities.unescape("&amp;", true));
        assertEquals("&apos;", Entities.unescape("&apos;", true));
        assertEquals("&quot;", Entities.unescape("&quot;", true));
        assertEquals("&lt", Entities.unescape("&lt", true));
        assertEquals("&gt", Entities.unescape("&gt", true));
        assertEquals("&amp", Entities.unescape("&amp", true));
        assertEquals("&apos", Entities.unescape("&apos", true));
        assertEquals("&quot", Entities.unescape("&quot", true));
    }
    
    @Test
    public void testUnescape_notStrict() {
        assertEquals("Test", Entities.unescape("Test", false));
        assertEquals("This is a test", Entities.unescape("This is a test", false));
        assertEquals("This is a test ", Entities.unescape("This is a test ", false));
        assertEquals("<", Entities.unescape("&lt;", false));
        assertEquals(">", Entities.unescape("&gt;", false));
        assertEquals("&", Entities.unescape("&amp;", false));
        assertEquals("'", Entities.unescape("&apos;", false));
        assertEquals("\"", Entities.unescape("&quot;", false));
        assertEquals("<", Entities.unescape("&lt", false));
        assertEquals(">", Entities.unescape("&gt", false));
        assertEquals("&", Entities.unescape("&amp", false));
        assertEquals("'", Entities.unescape("&apos", false));
        assertEquals("\"", Entities.unescape("&quot", false));
    }
}