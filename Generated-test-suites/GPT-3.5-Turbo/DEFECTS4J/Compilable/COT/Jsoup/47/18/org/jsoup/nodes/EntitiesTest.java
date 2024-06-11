package org.jsoup.nodes;

import org.jsoup.nodes.Entities;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("invalidEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(null, Entities.getCharacterByName("invalidEntity"));
    }

    @Test
    public void testEscape() {
        String escaped = Entities.escape("This is a <test> & 'example'", null);
        assertEquals("This is a &lt;test&gt; &amp; &apos;example&apos;", escaped);
    }

    @Test
    public void testEscape_withAttributes() {
        String escaped = Entities.escape("This is a <test> & 'example'", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        assertEquals("This is a &lt;test> &amp; 'example'", escaped);
    }

    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("This is a &lt;test&gt; &amp; &apos;example&apos;");
        assertEquals("This is a <test> & 'example'", unescaped);
    }

    @Test
    public void testUnescape_strict() {
        String unescaped = Entities.unescape("This is a &lt;test&gt; &amp; &apos;example&apos;", true);
        assertEquals("This is a <test> & 'example'", unescaped);
    }

}

