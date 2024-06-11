package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("fake"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("fake"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals((char) 0x3C, (char) Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("fake"));
    }

    @Test
    public void testEscape() {
        // Add test cases for the escape method
    }

    @Test
    public void testUnescape() {
        // Add test cases for the unescape method
    }
    
    // Add more test cases for other methods as needed
    
}