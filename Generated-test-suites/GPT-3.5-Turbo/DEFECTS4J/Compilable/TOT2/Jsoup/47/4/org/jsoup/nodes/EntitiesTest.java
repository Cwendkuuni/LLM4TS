package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String escapedString = Entities.escape("<div>&nbsp;</div>", outputSettings);
        assertEquals("&lt;div&gt;&amp;nbsp;&lt;/div&gt;", escapedString);
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&lt;div&gt;&amp;nbsp;&lt;/div&gt;");
        assertEquals("<div>&nbsp;</div>", unescapedString);
        unescapedString = Entities.unescape("&amp;invalid;");
        assertEquals("&invalid;", unescapedString);
    }

    // Add more test cases for edge cases and error scenarios

}