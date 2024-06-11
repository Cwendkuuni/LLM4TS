package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Map;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.encoder(CharsetEncoder.forName("UTF-8"));
        out.escapeMode(EscapeMode.xhtml);
        
        assertEquals("&quot;", Entities.escape("\"", out));
        assertEquals("&amp;", Entities.escape("&", out));
        assertEquals("&apos;", Entities.escape("'", out));
        assertEquals("&lt;", Entities.escape("<", out));
        assertEquals("&gt;", Entities.escape(">", out));
        assertEquals("abc", Entities.escape("abc", out));
    }

    @Test
    public void testEscapeWithCharsetEncoderAndEscapeMode() {
        CharsetEncoder encoder = CharsetEncoder.forName("UTF-8");
        Map<Character, String> map = Entities.EscapeMode.base.getMap();
        
        assertEquals(";&lt;&gt;&amp;&quot;&apos;", Entities.escape("<>&\"'", encoder, EscapeMode.base));
        assertEquals("abc", Entities.escape("abc", encoder, EscapeMode.base));
        assertEquals(";&lt;&gt;&amp;&quot;&apos;", Entities.escape("<>&\"'", encoder, EscapeMode.extended));
        assertEquals("abc", Entities.escape("abc", encoder, EscapeMode.extended));
    }

    @Test
    public void testUnescape() {
        assertEquals("test", Entities.unescape("test"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("'", Entities.unescape("&apos;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&#38;&#38;", Entities.unescape("&&amp;&AMP;"));
        assertEquals("&#0045;", Entities.unescape("&#45;"));
        assertEquals("&#x0045;", Entities.unescape("&#x45;"));
        assertEquals("&#X0045;", Entities.unescape("&#X0045;"));
        assertEquals("&#0x45;", Entities.unescape("&#0x45;"));
        assertEquals("&#X03A9;", Entities.unescape("&#X03A9;"));
    }

    @Test
    public void testUnescapeWithStrict() {
        assertEquals("test", Entities.unescape("test", true));
        assertEquals("\"", Entities.unescape("&quot;", true));
        assertEquals("&", Entities.unescape("&amp;", true));
        assertEquals("'", Entities.unescape("&apos;", true));
        assertEquals("<", Entities.unescape("&lt;", true));
        assertEquals(">", Entities.unescape("&gt;", true));
        assertEquals(";", Entities.unescape(";&AMP", true));
        assertEquals(";&lt;&gt;&amp;&quot;&apos;", Entities.unescape(";&lt;&gt;&amp;&quot;&apos;", true));
    }

    @Test
    public void testToCharacterKey() {
        Map<Character, String> map = Entities.toCharacterKey(Entities.base);
        assertEquals("lt", map.get('<'));
        assertEquals("amp", map.get('&'));
    }
}