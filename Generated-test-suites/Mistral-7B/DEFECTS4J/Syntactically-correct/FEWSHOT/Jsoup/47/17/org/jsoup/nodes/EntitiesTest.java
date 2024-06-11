package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("gt"));
        assertFalse(Entities.isBaseNamedEntity("nbsp;"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('"', Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("gt"));
    }

    @Test
    public void testEscapeAndUnescape() {
        String input = "&lt;foo&gt; bar &amp; &quot;quoted&quot; string &apos;single&apos; &eacute;UG&uuml; &nbsp; &lt; &gt; &amp;";
        String expected = "&amp;lt;foo&amp;gt; bar &amp;amp; &amp;quot;quoted&amp;quot; &amp;apos;single&amp;apos; &amp;#x00E9;UG&amp;#x00FC; &amp;#x00A0; &amp;lt; &amp;gt; &amp;amp;";
        assertEquals(expected, Entities.escape(input, Document.OutputSettings.prettyHtml()));

        String result = Entities.unescape(expected);
        assertEquals(input, result);
    }

    @Test
    public void testEscapeInAttribute() {
        String input = "<script src=\"script.js\" type=\"text/javascript\">";
        String expected = "<script src=\"script.js\" type=\"text/javascript\">"; // no need to escape "<" and ">" in attribute values
        String output = Entities.escape(input, Document.OutputSettings.xhtml());
        assertEquals(expected, output);

        input = "&lt;script&gt; alert('XSS'); &lt;/script&gt;";
        expected = "&amp;lt;script&gt; alert('XSS'); &amp;lt;/script&gt;"; // need to escape "<" and ">" in character data
        output = Entities.escape(input, Document.OutputSettings.xhtml());
        assertEquals(expected, output);
    }

    @Test
    public void testCanEncode() {
        CoreCharset charset = CoreCharset.utf;
        CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
        for (char c = 0; c < 256; c++) {
            boolean canEncode = Entities.canEncode(charset, c, encoder);
            if (Character.isDefined(c)) {
                assertTrue(canEncode);
            } else {
                assertFalse(canEncode);
            }
        }
    }
}