package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Entities;
import org.junit.Test;

import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertEquals(true, Entities.isNamedEntity("lt"));
        assertEquals(true, Entities.isNamedEntity("amp"));
        assertEquals(true, Entities.isNamedEntity("quot"));
        assertEquals(false, Entities.isNamedEntity("xxe")); // non-existent named entity

        // test base named entities
        assertEquals(true, Entities.isBaseNamedEntity("amp"));
        assertEquals(true, Entities.isBaseNamedEntity("quot"));
        assertEquals(false, Entities.isBaseNamedEntity("lt")); // not in base set
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(34, Entities.getCharacterByName("quot").codePointAt(0));
        assertEquals(60, Entities.getCharacterByName("lt").codePointAt(0));
        assertEquals(62, Entities.getCharacterByName("gt").codePointAt(0));
        assertEquals(38, Entities.getCharacterByName("amp").codePointAt(0));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        final String string = "&foo &lt;bar&amp; baz &quot;quux&quot;";
        StringBuilder accumulator = new StringBuilder();

        Entities.escape(accumulator, string, out, false, false, false);
        assertEquals("&amp;amp;amp;foo &amp;lt;amp;lt;bar&amp;amp;amp;amp;amp;quot;quux&amp;amp;amp;amp;quot;", accumulator.toString());

        out.escapeMode(Entities.EscapeMode.xhtml);
        accumulator.setLength(0);
        Entities.escape(accumulator, string, out, false, false, false);
        assertEquals("&amp;foo &amp;lt;bar&amp;quot;", accumulator.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("&foo &lt;bar&amp; baz &quot;quux&quot;", Entities.unescape("&amp;amp;amp;foo &amp;lt;amp;lt;bar&amp;amp;amp;amp;amp;quot;quux&amp;amp;amp;amp;quot;"));
        assertEquals("&foo <bar>&baz \"quux\"", Entities.unescape("&amp;foo &lt;bar>&amp;baz &quot;quux&quot;"));
        assertEquals("&foo <bar>&baz \"quux\"", Entities.unescape("&foo <bar>&baz \"quux\"", false)); // no strict check
    }

    @Test
    public void testCanEncode() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.encoder(Charset.forName("UTF-8"));

        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'a'));
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'é'));
        assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, 'é'));

        Document.OutputSettings outFallback = new Document.OutputSettings();
        outFallback.encoder(Charset.forName("ISO-8859-1"));

        assertFalse(Entities.canEncode(Entities.CoreCharset.utf, 'é'), outFallback.encoder());
        assertTrue(Entities.canEncode(Entities.CoreCharset.fallback, 'é'), outFallback.encoder());
    }
}