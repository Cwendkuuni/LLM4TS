package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("fakeEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("fakeEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("fakeEntity"));
    }

    @Test
    public void testEscape() {
        assertEquals("&amp;lt;", Entities.escape("&lt;", null));
        assertEquals("&lt;", Entities.escape("<", null));
        assertEquals("&#xA0;", Entities.escape("\u00A0", null));
    }

    @Test
    public void testUnescape() {
        assertEquals("&lt;", Entities.unescape("&amp;lt;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals("\u00A0", Entities.unescape("&#xA0;"));
    }

    // Additional test cases for potential edge cases and exception handling scenarios

    @Test
    public void testEscapeWithNullOutputSettings() {
        assertEquals("&amp;lt;", Entities.escape("&lt;", null));
    }

    @Test
    public void testUnescapeWithStrictFalse() {
        assertEquals("&lt;", Entities.unescape("&amp;lt;", false));
    }

    @Test
    public void testUnescapeWithStrictTrueAndMissingSemicolon() {
        assertEquals("&amp;lt;", Entities.unescape("&amp;lt;", true));
    }

    @Test
    public void testUnescapeWithStrictTrueAndInvalidEntityName() {
        assertEquals("&fakeEntity;", Entities.unescape("&fakeEntity;", true));
    }

    @Test
    public void testEscapeWithEmptyString() {
        assertEquals("", Entities.escape("", null));
    }

    @Test
    public void testUnescapeWithEmptyString() {
        assertEquals("", Entities.unescape(""));
    }
    
    // Additional test cases can be added for other methods in the Entities class

}