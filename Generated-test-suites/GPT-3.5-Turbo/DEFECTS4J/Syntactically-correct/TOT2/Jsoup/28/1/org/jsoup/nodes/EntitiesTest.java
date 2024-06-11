package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode = Entities.EscapeMode.xhtml;
        assertEquals("&lt;&gt;&amp;&apos;&quot;", Entities.escape("<>&'\"", out));
    }

    @Test
    public void testUnescape() {
        assertEquals("<>&'", Entities.unescape("&lt;&gt;&amp;&apos;"));
    }

    @Test
    public void testUnescapeStrict() {
        assertEquals("<>&'", Entities.unescape("&lt;&gt;&amp;&apos;", true));
    }
}