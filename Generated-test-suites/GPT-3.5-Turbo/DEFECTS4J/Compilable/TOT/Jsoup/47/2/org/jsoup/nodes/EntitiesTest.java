package org.jsoup.nodes;

import org.jsoup.nodes.Entities;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Typical use case
        assertTrue(Entities.isNamedEntity("lt"));

        // Edge case - empty string
        assertFalse(Entities.isNamedEntity(""));

        // Error scenario - unknown entity
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Typical use case
        assertTrue(Entities.isBaseNamedEntity("lt"));

        // Edge case - empty string
        assertFalse(Entities.isBaseNamedEntity(""));

        // Error scenario - unknown entity
        assertFalse(Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        // Typical use case
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));

        // Edge case - empty string
        assertNull(Entities.getCharacterByName(""));

        // Error scenario - unknown entity
        assertNull(Entities.getCharacterByName("unknown"));
    }
    
    @Test
    public void testEscape() {
        String string = "This is <b>bold</b> text.";
        Document.OutputSettings out = new Document.OutputSettings();

        // Typical use case
        assertEquals("This is &lt;b&gt;bold&lt;/b&gt; text.", Entities.escape(string, out));

        // Edge case - empty string
        assertEquals("", Entities.escape("", out));
    }

    @Test
    public void testUnescape() {
        String escaped = "This is &lt;b&gt;bold&lt;/b&gt; text.";

        // Typical use case
        assertEquals("This is <b>bold</b> text.", Entities.unescape(escaped));

        // Edge case - empty string
        assertEquals("", Entities.unescape(""));

        // Error scenario - missing trailing ';' in strict mode
        assertEquals("This is <b>bold</b> text", Entities.unescape(escaped, true));
    }
}
