package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Entities;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("invalidEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("gt"));
        assertFalse(Entities.isBaseNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('"', Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("invalidEntity"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        String input = "&lt;foo&gt;<quoted>&amp;amp;&quot;";

        String escaped = Entities.escape(input, out);
        assertEquals("&amp;lt;foo&amp;gt;&amp;quot; &amp;amp; &amp;quot;", escaped);

        out.setEscapeMode(Entities.EscapeMode.xhtml);
        escaped = Entities.escape(input, out);
        assertEquals("&lt;foo&gt;&quot; &amp;amp; &quot;", escaped);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;foo&amp;gt;&quot; &amp;amp; &quot;";

        String unescaped = Entities.unescape(input, false);
        assertEquals("<foo>", unescaped);

        unescaped = Entities.unescape(input, true);
        assertEquals("&lt;foo&gt;&quot; &amp;amp; &quot;", unescaped);
    }

    @Test
    public void testCanEncode() {
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, 'a'));
        assertEquals(true, Entities.canEncode(Entities.CoreCharset.utf, '\u00E4'));
        assertEquals(false, Entities.canEncode(Entities.CoreCharset.ascii, '\u00E4'));
    }
}