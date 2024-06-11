import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        // Test for normal scenario
        assertEquals("&lt;&gt;&amp;&quot;", Entities.escape("<>&\""));

        // Test for edge case with special characters
        assertEquals("&copy;", Entities.escape("©"));
    }

    @Test
    public void testUnescape() {
        // Test for normal scenario
        assertEquals("<>&\"'", Entities.unescape("&lt;&gt;&amp;&quot;&apos;"));

        // Test for edge case with special characters
        assertEquals("©", Entities.unescape("&copy;"));
    }

    // Add more test methods for remaining methods and edge cases

}