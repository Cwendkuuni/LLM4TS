import static org.junit.Assert.*;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        String input = "<div>hello</div>";
        String expected = "&lt;div&gt;hello&lt;/div&gt;";
        assertEquals(expected, Entities.escape(input));
    }

    @Test
    public void testUnescape() {
        String input = "&lt;div&gt;hello&lt;/div&gt;";
        String expected = "<div>hello</div>";
        assertEquals(expected, Entities.unescape(input));
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testIsNamedEntity_withUnknownEntity() {
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName_withUnknownEntity() {
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape_withNullString() {
        assertNull(Entities.escape(null));
    }

    @Test
    public void testEscape_withEmptyString() {
        assertEquals("", Entities.escape(""));
    }

    @Test
    public void testUnescape_withNullString() {
        assertNull(Entities.unescape(null));
    }

    @Test
    public void testUnescape_withEmptyString() {
        assertEquals("", Entities.unescape(""));
    }
}