package org.jsoup.nodes;
import static org.junit.Assert.*;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("test"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("test"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('>', Entities.getCharacterByName("gt").charValue());
        assertNull(Entities.getCharacterByName("test"));
    }

    @Test
    public void testEscape() {
        // TODO: Write comprehensive test cases for escape() method
    }

    @Test
    public void testUnescape() {
        // TODO: Write comprehensive test cases for unescape() method
    }

}