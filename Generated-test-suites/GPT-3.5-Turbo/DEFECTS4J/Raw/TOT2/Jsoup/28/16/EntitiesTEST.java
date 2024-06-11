package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        assertEquals("&gt;&lt;", Entities.escape("><", new Document.OutputSettings(), Entities.EscapeMode.xhtml));
    }

    @Test
    public void testUnescape() {
        assertEquals("<>", Entities.unescape("&lt;&gt;"));
    }

    @Test
    public void testEscape_edgeCases() {
        assertEquals("&#256;", Entities.escape("Ā", new Document.OutputSettings(), Entities.EscapeMode.xhtml));
        assertEquals("", Entities.escape("", new Document.OutputSettings(), Entities.EscapeMode.xhtml));
    }

    @Test
    public void testUnescape_edgeCases() {
        assertEquals("Ā", Entities.unescape("&#256;"));
        assertEquals("", Entities.unescape(""));
    }

}