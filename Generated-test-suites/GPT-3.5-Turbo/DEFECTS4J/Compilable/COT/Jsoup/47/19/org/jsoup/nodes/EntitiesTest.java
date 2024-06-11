package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void isNamedEntityTest() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void isBaseNamedEntityTest() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("abc"));
    }

    @Test
    public void getCharacterByNameTest() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("abc"));
    }
    
    // additional test cases for edge cases and exceptions
    
    @Test
    public void escapeTest() {
        // Add test cases for escaping functionality
    }

    @Test
    public void unescapeTest() {
        // Add test cases for unescaping functionality
    }

}
