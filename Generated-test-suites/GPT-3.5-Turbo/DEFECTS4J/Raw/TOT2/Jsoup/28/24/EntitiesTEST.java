import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test typical use case
        assertTrue(Entities.isNamedEntity("lt"));
        // Test edge case with unknown entity
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        // Test typical use case
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        // Test edge case with unknown entity
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        // Test typical use case
        assertEquals("amp;", Entities.escape("&", null, Entities.EscapeMode.xhtml));
        // Test edge case with unencodable character
        assertEquals("&#128;", Entities.escape("€", null, Entities.EscapeMode.xhtml));
    }

    @Test
    public void testUnescape() {
        // Test typical use case
        assertEquals("&", Entities.unescape("amp;"));
        // Test edge case with invalid entity
        assertEquals("invalid&", Entities.unescape("invalid&"));
    }

    @Test
    public void testLoadEntities() {
        // Test typical use case
        assertNotNull(Entities.loadEntities("entities-base.properties"));
        // Test edge case with non-existent file
        assertThrows(MissingResourceException.class, () -> Entities.loadEntities("non-existent.properties"));
    }

    @Test
    public void testToCharacterKey() {
        // Test typical use case with lowercase preference
        assertEquals(Entities.toCharacterKey(Entities.base).get('<'), "lt");
        // Test edge case with duplicate entities
        assertEquals(Entities.toCharacterKey(Entities.full).get('\u0160'), "Scaron");
    }
}