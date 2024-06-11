package org.jsoup.nodes;
import static org.junit.Assert.*;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("abcdef"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("abcdef"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals((Character)'<', Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("abcdef"));
    }

    @Test
    public void testEscape() {
        assertEquals("abc&amp;def&lt;ghi&gt;", Entities.escape("abc&def<ghi>", new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        assertEquals("abc&def<ghi>", Entities.unescape("abc&amp;def&lt;ghi&gt;"));
    }

    // Edge Cases and Exception Handling Test Cases

    @Test
    public void testIsNamedEntity_withNull() {
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testIsBaseNamedEntity_withNull() {
        assertFalse(Entities.isBaseNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName_withNull() {
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscape_withNull() {
        assertNull(Entities.escape(null, new Document.OutputSettings()));
    }

    @Test
    public void testUnescape_withNull() {
        assertNull(Entities.unescape(null));
    }

    @Test
    public void testEscape_withEmptyString() {
        assertEquals("", Entities.escape("", new Document.OutputSettings()));
    }

    @Test
    public void testUnescape_withEmptyString() {
        assertEquals("", Entities.unescape(""));
    }

    // 

    // ... existing tests ...

    // 
}