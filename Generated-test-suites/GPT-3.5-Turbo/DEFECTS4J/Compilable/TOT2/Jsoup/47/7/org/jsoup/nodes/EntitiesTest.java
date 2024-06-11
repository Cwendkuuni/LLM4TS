package org.jsoup.nodes;
import static org.junit.Assert.*;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("nonExistentEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertFalse(Entities.isBaseNamedEntity("nonExistentEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("nonExistentEntity"));
    }

    @Test
    public void testEscape() {
        String result = Entities.escape("This <is> a test", new Document.OutputSettings());
        assertEquals("This &lt;is&gt; a test", result);
    }

    @Test
    public void testUnescape() {
        String result = Entities.unescape("This &lt;is&gt; a test");
        assertEquals("This <is> a test", result);
    }

    @Test
    public void testEscapeWithAttributes() {
        String result = Entities.escape("\"This is a test\"", new Document.OutputSettings());
        assertEquals("&quot;This is a test&quot;", result);
    }

    @Test
    public void testUnescapeStrict() {
        String result = Entities.unescape("&lt;This is a test&gt;", true);
        assertEquals("<This is a test>", result);
    }

    @Test
    public void testUnescapeNonStrict() {
        String result = Entities.unescape("&lt;This is a test&gt;", false);
        assertEquals("<This is a test>", result);
    }

    // Add more test cases for edge cases and exceptions if needed

}