package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;

import java.util.Map;
import java.util.HashMap;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf((char) 0x00022), Entities.getCharacterByName("quot"));
        assertEquals(Character.valueOf((char) 0x00026), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf((char) 0x00027), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf((char) 0x0003C), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf((char) 0x0003E), Entities.getCharacterByName("gt"));
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        String string = "<div>&</div>";
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(EscapeMode.xhtml);

        assertEquals("&lt;div&gt;&amp;&lt;/div&gt;", Entities.escape(string, out));
    }

    @Test
    public void testUnescape() {
        String string = "&lt;div&gt;&amp;&lt;/div&gt;";
        assertEquals("<div>&</div>", Entities.unescape(string));
    }

    @Test
    public void testEscapeAndUnescape() {
        String string = "<div>&</div>";
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(EscapeMode.xhtml);

        String escaped = Entities.escape(string, out);
        String unescaped = Entities.unescape(escaped);

        assertEquals(string, unescaped);
    }

    @Test
    public void testEscapeWithCharsetEncoder() {
        String string = "Some text & special characters: © ®";
        CharsetEncoder encoder = CharsetEncoder.defaultCharset().newEncoder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(EscapeMode.extended);

        String escaped = Entities.escape(string, encoder, out);
        String unescaped = Entities.unescape(escaped);

        assertEquals(string, unescaped);
    }

    @Test
    public void testUnescapeStrict() {
        String string = "&lt;div&gt;&amp;&lt;/div&gt;";
        assertEquals("<div>&</div>", Entities.unescape(string, true));
    }
}