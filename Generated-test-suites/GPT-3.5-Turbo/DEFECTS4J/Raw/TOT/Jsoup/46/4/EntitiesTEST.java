
package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Typical use case
        assertTrue(Entities.isNamedEntity("lt"));
        // Edge case
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Typical use case
        assertTrue(Entities.isBaseNamedEntity("lt"));
        // Edge case
        assertFalse(Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        // Typical use case
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        // Edge case
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        // Typical use case
        String escaped = Entities.escape("<p>Hello, world!</p>", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        assertEquals("&lt;p&gt;Hello, world!&lt;/p&gt;", escaped);
        // Edge case
        escaped = Entities.escape("<p>Hello, world!</p>", new Document.OutputSettings().escapeMode(Entities.EscapeMode.base));
        assertEquals("&lt;p&gt;Hello, world!&lt;/p&gt;", escaped);
    }

    @Test
    public void testUnescape() {
        // Typical use case
        String unescaped = Entities.unescape("&lt;p&gt;Hello, world!&lt;/p&gt;");
        assertEquals("<p>Hello, world!</p>", unescaped);
        // Edge case
        unescaped = Entities.unescape("&lt;p&gt;Hello, world!&lt;/p;&gt;");
        assertEquals("&lt;p&gt;Hello, world!&lt;/p;&gt;", unescaped);
    }
}
