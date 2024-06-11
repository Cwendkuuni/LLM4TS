package org.jsoup.nodes;
import org.junit.*;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("example"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("example"));
    }

    @Test
    public void testEscape() {
        String escapedString = Entities.escape("<html>", null, Entities.EscapeMode.xhtml);
        assertEquals("&lt;html&gt;", escapedString);
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&lt;html&gt;");
        assertEquals("<html>", unescapedString);
    }

    @Test
    public void testEscapeThrowsExceptionForInvalidEncoder() {
        try {
            Entities.escape("test", null, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Exception was thrown as expected
        }
    }

    @Test
    public void testUnescapeWithStrictFlag() {
        String unescapedString = Entities.unescape("&lt;html");
        assertEquals("&lt;html", unescapedString); // Should not unescape due to missing semicolon
    }

}