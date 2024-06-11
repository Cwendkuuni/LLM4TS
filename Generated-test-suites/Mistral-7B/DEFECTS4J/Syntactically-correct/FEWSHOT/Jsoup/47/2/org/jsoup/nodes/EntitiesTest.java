package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        for (Map.Entry<Character, String> entity : Entities.full.entrySet()) {
            assertTrue(Entities.isNamedEntity(entity.getValue()));
        }

        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testIsBaseNamedEntity() {
        for (Map.Entry<Character, String> entity : Entities.base.entrySet()) {
            assertTrue(Entities.isBaseNamedEntity(entity.getValue()));
        }

        assertFalse(Entities.isBaseNamedEntity(""));
        assertFalse(Entities.isBaseNamedEntity(null));

        assertFalse(Entities.isBaseNamedEntity("lt")); // xhtml only
    }

    @Test
    public void testGetCharacterByName() {
        for (Map.Entry<Character, String> entity : Entities.full.entrySet()) {
            assertEquals(entity.getKey(), Entities.getCharacterByName(entity.getValue()));
        }

        assertNull(Entities.getCharacterByName(null));
        assertNull(Entities.getCharacterByName(""));
    }

    @Test
    public void testEscape() {
        String source = "&lt;foo &amp; bar&gt; &quot;bla &quot; < > \"";
        Document.OutputSettings out = new Document.OutputSettings();

        String expected = "&amp;lt;foo &amp;amp; bar&amp;gt; &amp;quot;bla &amp;quot; &amp;lt; &amp;gt; &amp;quot;";
        String result = Entities.escape(source, out);
        assertEquals(expected, result);

        out.setEscapeMode(Entities.EscapeMode.xhtml);
        result = Entities.escape(source, out);
        assertEquals("&lt;foo&gt; &amp; &lt;bar&gt; &quot;bla &quot;", result);
    }

    @Test
    public void testUnescape() {
        String source = "&amp;lt;foo &amp;amp; bar&amp;gt; &amp;quot;bla &amp;quot; &amp;lt; &amp;gt; &amp;quot;";
        String result = Entities.unescape(source);
        assertEquals("&lt;foo&gt; &amp; &lt;bar&gt; &quot;bla &quot; < > &quot;", result);

        String strictSource = "&amp;lt;foo &amp;amp; bar&amp;gt; &amp;quot;bla &amp;quot; &amp;lt; &amp;gt; &amp;quot; &";
        result = Entities.unescape(strictSource);
        assertEquals("&lt;foo&gt; &amp; &lt;bar&gt; &quot;bla &quot; < > &quot; &", result);
    }
}