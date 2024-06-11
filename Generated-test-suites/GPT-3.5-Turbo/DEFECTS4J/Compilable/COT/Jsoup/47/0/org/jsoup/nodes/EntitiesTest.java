package org.jsoup.nodes;

import static org.junit.Assert.*;
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
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        String html = "This is <b>bold</b> text.";
        String expected = "This is &lt;b&gt;bold&lt;/b&gt; text.";
        assertEquals(expected, Entities.escape(html, new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        String escaped = "This is &lt;b&gt;bold&lt;/b&gt; text.";
        String expected = "This is <b>bold</b> text.";
        assertEquals(expected, Entities.unescape(escaped));
    }
}
