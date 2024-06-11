
package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    // Test isNamedEntity() method

    @Test
    public void testIsNamedEntity() {
        boolean result = Entities.isNamedEntity("lt");
        assertTrue(result);
    }

    @Test
    public void testIsNamedEntity_unknownEntity() {
        boolean result = Entities.isNamedEntity("unknown");
        assertFalse(result);
    }

    // Test isBaseNamedEntity() method

    @Test
    public void testIsBaseNamedEntity() {
        boolean result = Entities.isBaseNamedEntity("lt");
        assertTrue(result);
    }

    @Test
    public void testIsBaseNamedEntity_unknownEntity() {
        boolean result = Entities.isBaseNamedEntity("unknown");
        assertFalse(result);
    }

    // Test getCharacterByName() method

    @Test
    public void testGetCharacterByName() {
        Character result = Entities.getCharacterByName("lt");
        assertEquals('<', result.charValue());
    }

    @Test
    public void testGetCharacterByName_unknownEntity() {
        Character result = Entities.getCharacterByName("unknown");
        assertNull(result);
    }

    // Test escape() method

    @Test
    public void testEscape() {
        String result = Entities.escape("Test &");
        assertEquals("Test &amp;", result);
    }

    // Test unescape() method

    @Test
    public void testUnescape() {
        String result = Entities.unescape("Test &amp;");
        assertEquals("Test &", result);
    }
}
