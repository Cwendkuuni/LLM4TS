package org.jsoup.nodes;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    // Test cases for isNamedEntity method
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    // Test cases for isBaseNamedEntity method
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }

    // Test cases for getCharacterByName method
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("invalid"));
    }

    // Test cases for escape method
    @Test
    public void testEscape() {
        // Add test cases for escape method
    }

    // Test cases for unescape method
    @Test
    public void testUnescape() {
        // Add test cases for unescape method
    }

    // Test cases for other methods with edge cases and exceptions
    // Add test cases for other methods in Entities class

}