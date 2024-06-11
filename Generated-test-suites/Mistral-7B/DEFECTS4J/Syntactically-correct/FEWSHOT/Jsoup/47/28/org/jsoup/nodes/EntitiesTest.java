package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Entities;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        String input = "&lt;a&gt; &quot;foo&quot; &amp; bar &apos;baz&apos; &nbsp; &ndash; &mdash; &copy; &eacute; &Acirc; &ntilde;";
        OutputSettings out = new OutputSettings();
        String output = Entities.escape(input, out);
        assertEquals("&amp;lt;a&gt; &amp;quot;foo&quot; &amp;amp; bar &amp;apos;baz&apos; &amp;nbsp; &amp;ndash; &amp;mdash; &copy; &amp;eacute; &Acirc; &amp;ntilde;", output);

        out = new OutputSettings(Entities.EscapeMode.XHTML);
        output = Entities.escape(input, out);
        assertEquals("&amp;lt;a&gt; &quot;foo&quot; &amp;amp; &quot;bar&quot; &apos;baz&apos; &nbsp; -- -- &copy; &Acirc; &Ntilde;", output);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;a&gt; &amp;quot;foo&quot; &amp;amp; &quot;bar&quot; &amp;apos;baz&apos; &amp;nbsp; &amp;ndash; &amp;mdash; &copy; &amp;eacute; &Acirc; &amp;ntilde;";
        String output = Entities.unescape(input);
        assertEquals("&lt;a&gt; \"foo\" &amp; &quot;bar&quot; 'baz' &nbsp; -- -- © é Á ñ", output);
    }

    @Test
    public void testCanEncode() {
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'a'));
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, 'a'));
        assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, '\u00E9'));
    }
}