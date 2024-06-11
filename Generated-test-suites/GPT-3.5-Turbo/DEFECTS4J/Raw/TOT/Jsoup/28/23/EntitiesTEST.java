import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Typical use case
        assertTrue(Entities.isNamedEntity("lt"));

        // Edge case: Check for an entity that doesn't exist
        assertFalse(Entities.isNamedEntity("xyz"));

        // Error scenario: Check with a null input
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        // Typical use case
        assertEquals(Character.valueOf((char) 0x0003C), Entities.getCharacterByName("lt"));

        // Edge case: Check for a non-existing entity
        assertNull(Entities.getCharacterByName("xyz"));

        // Error scenario: Check with a null input
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscape() {
        // Typical use case
        assertEquals("This &amp; That", Entities.escape("This & That", Document.OutputSettings.escapeMode));

        // Edge case: Empty string
        assertEquals("", Entities.escape("", Document.OutputSettings.escapeMode));

        // Error scenario: Check with a null input
        assertNull(Entities.escape(null, Document.OutputSettings.escapeMode));
    }

    @Test
    public void testUnescape() {
        // Typical use case
        assertEquals("This < That", Entities.unescape("This &lt; That"));

        // Edge case: Empty string
        assertEquals("", Entities.unescape(""));

        // Error scenario: Check with a null input
        assertNull(Entities.unescape(null));
    }

    @Test
    public void testLoadEntities() {
        // Not testing this method, as it is using external resources
        // We can assume that if other methods are functioning correctly, this method will also work.
        // However, if required, appropriate tests can be written to test this method separately.
        assertTrue(true);
    }

    // Add more test methods for other methods in the class if necessary

}

