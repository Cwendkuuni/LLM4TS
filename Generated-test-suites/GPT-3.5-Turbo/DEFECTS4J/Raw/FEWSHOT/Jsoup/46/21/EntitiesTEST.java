package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("foo"));
    }

    @Test
    public void testEscape() {
        String escapedString = Entities.escape("<div>Text</div>", new Document.OutputSettings());
        assertEquals("&lt;div&gt;Text&lt;/div&gt;", escapedString);
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&lt;div&gt;Text&lt;/div&gt;");
        assertEquals("<div>Text</div>", unescapedString);
    }
}