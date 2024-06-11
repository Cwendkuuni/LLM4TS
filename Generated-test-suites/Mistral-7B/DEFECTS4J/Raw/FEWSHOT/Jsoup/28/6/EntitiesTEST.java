package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("nonexistent"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("nonexistent"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;&gt;&amp;&apos;foo&quot;bar", Entities.escape("< >& 'foo\"bar'", null, Entities.EscapeMode.base));
        assertEquals("&amp;&lt;>&amp;apos;foo&amp;quot;bar", Entities.escape("& < >& 'foo\"bar'", null, Entities.EscapeMode.base));
        assertEquals("&lt;>&gt;&amp;apos;&quot;foo&quot;bar", Entities.escape("< >& 'foo\"bar'", null, Entities.EscapeMode.xhtml));
    }

    @Test
    public void testUnescape() {
        assertEquals("< >& 'foo\"bar'", Entities.unescape("&lt;>&gt;&amp;apos;&quot;foo&quot;bar"));
        assertEquals("< >& 'foo\"bar'", Entities.unescape("&lt;>&gt;&amp;apos;&quot;foo&quot;bar", true));
        assertEquals("< >& 'foo\"bar'", Entities.unescape("&lt;>&gt;&amp;apos;&quot;foo&quot;bar", false));
    }

    @Test
    public void testEscapeModeXhtml() {
        assertEquals("&quot;&amp;&apos;&lt;&gt;", Entities.escape("\"&'<> ", Entities.EscapeMode.xhtml.getMap()));
    }

    @Test
    public void testEscapeModeBase() {
        assertEquals("&quot;&amp;&apos;&lt;&gt;", Entities.escape("\"&'<> ", Entities.EscapeMode.base.getMap()));
        assertEquals("&quot;&#34;&amp;&#39;&lt;&gt;", Entities.escape("\"&'<> ", Entities.EscapeMode.base.getMap()));
    }

    @Test
    public void testEscapeModeExtended() {
        assertEquals("&quot;&amp;&apos;&#x201C;&#x201D;&lt;&gt;", Entities.escape("\"&'<> ", Entities.EscapeMode.extended.getMap()));
    }
}