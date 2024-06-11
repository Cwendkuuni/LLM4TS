package org.jsoup.nodes;

import org.junit.Test;

import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf((char) 0x00027), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf((char) 0x0003E), Entities.getCharacterByName("gt"));
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.encoder(CharsetEncoder.DEFAULT);
        out.escapeMode(Entities.EscapeMode.xhtml);
        assertEquals("hello &lt;world&gt; &amp;", Entities.escape("hello <world> &", out));
    }

    @Test
    public void testUnescape() {
        assertEquals("hello <world> &", Entities.unescape("hello &lt;world&gt; &"));
        assertEquals("&#8364;", Entities.unescape("&euro;"));
    }

    @Test
    public void testUnescapeStrict() {
        assertEquals("hello &lt;world&gt; &", Entities.unescape("hello &lt;world&gt; &", true));
        assertEquals("&lt;missing semi", Entities.unescape("&lt;missing semi", true));
    }

    @Test
    public void testEscapeModeXhtml() {
        Map<Character, String> xhtmlByVal = new HashMap<>();
        xhtmlByVal.put((char) 0x00022, "quot");
        xhtmlByVal.put((char) 0x00026, "amp");
        xhtmlByVal.put((char) 0x00027, "apos");
        xhtmlByVal.put((char) 0x0003C, "lt");
        xhtmlByVal.put((char) 0x0003E, "gt");

        assertEquals(xhtmlByVal, Entities.EscapeMode.xhtml.getMap());
    }

    @Test
    public void testEscapeModeBase() {
        Map<Character, String> baseByVal = new HashMap<>();
        baseByVal.put((char) 0x00022, "quot");
        baseByVal.put((char) 0x00026, "amp");
        baseByVal.put((char) 0x00027, "apos");
        baseByVal.put((char) 0x0003C, "lt");
        baseByVal.put((char) 0x0003E, "gt");

        assertEquals(baseByVal, Entities.EscapeMode.base.getMap());
    }

    @Test
    public void testEscapeModeExtended() {
        assertEquals(Entities.fullByVal, Entities.EscapeMode.extended.getMap());
    }
}