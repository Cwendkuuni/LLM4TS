import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("foo"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;&gt;&amp;", Entities.escape("<>&", null, Entities.EscapeMode.base));
        assertEquals("&#64;&#256;", Entities.escape("@Ā", null, Entities.EscapeMode.xhtml));
    }

    @Test
    public void testUnescape() {
        assertEquals("<&>", Entities.unescape("&lt;&amp;"));
        assertEquals("@Ā", Entities.unescape("&#64;&#256;"));
    }

    // Additional test cases for edge cases and error scenarios can be added here

}