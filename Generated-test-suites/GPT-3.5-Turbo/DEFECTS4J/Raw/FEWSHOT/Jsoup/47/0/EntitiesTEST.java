
package org.jsoup.nodes;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("test"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertFalse(Entities.isBaseNamedEntity("test"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(null, Entities.getCharacterByName("test"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
        assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
        assertEquals("&quot;", Entities.escape("\"", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
    }

    @Test
    public void testEscape_noEscape() {
        assertEquals("<", Entities.escape("<", new Document.OutputSettings(Entities.EscapeMode.base)));
        assertEquals("&", Entities.escape("&", new Document.OutputSettings(Entities.EscapeMode.base)));
        assertEquals("\"", Entities.escape("\"", new Document.OutputSettings(Entities.EscapeMode.base)));
    }

    @Test
    public void testEscape_extended() {
        assertEquals("&#x000A0;", Entities.escape("&#x000A0;", new Document.OutputSettings(Entities.EscapeMode.extended)));
    }

    @Test
    public void testEscape_inAttribute() {
        assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
        assertEquals("&gt;", Entities.escape(">", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
        assertEquals("&quot;", Entities.escape("\"", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
    }

    @Test
    public void testEscape_normaliseWhite() {
        assertEquals(" a ", Entities.escape(" a ", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
        assertEquals("  a ", Entities.escape("  a ", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
        assertEquals(" a  ", Entities.escape(" a  ", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
        assertEquals(" a b ", Entities.escape(" a b ", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
    }

    @Test
    public void testEscape_stripLeadingWhite() {
        assertEquals("a ", Entities.escape("a ", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
        assertEquals(" a ", Entities.escape(" a ", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
        assertEquals("a  ", Entities.escape("a  ", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
        assertEquals("a b ", Entities.escape("a b ", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\"", Entities.unescape("&quot;"));
    }

    @Test
    public void testUnescape_strict() {
        assertEquals("<", Entities.unescape("&lt;", true));
        assertEquals(">", Entities.unescape("&gt;", true));
        assertEquals("&", Entities.unescape("&amp;", true));
        assertEquals("\"", Entities.unescape("&quot;", true));
        assertEquals("&#xe9;", Entities.unescape("&#xe9;", true));
    }

    @Test
    public void testUnescape_nonStrict() {
        assertEquals("<", Entities.unescape("&lt;", false));
        assertEquals(">", Entities.unescape("&gt;", false));
        assertEquals("&", Entities.unescape("&amp;", false));
        assertEquals("\"", Entities.unescape("&quot;", false));
        assertEquals("é", Entities.unescape("&#xe9;", false));
    }

    @Test
    public void testCanEncode_ascii() {
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, 'A', null));
        assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, 'Á', null));
    }

    @Test
    public void testCanEncode_utf() {
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'A', null));
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'Á', null));
    }

    @Test
    public void testCanEncode_fallback() {
        assertTrue(Entities.canEncode(Entities.CoreCharset.fallback, 'A', null));
        assertTrue(Entities.canEncode(Entities.CoreCharset.fallback, 'Á', null));
    }

    @Test
    public void testXhtmlByVal() {
        Map<Character, String> xhtmlByVal = Entities.EscapeMode.xhtml.getMap();
        assertEquals("&lt;", xhtmlByVal.get('<'));
        assertEquals("&amp;", xhtmlByVal.get('&'));
        assertEquals("&quot;", xhtmlByVal.get('"'));
    }

}
