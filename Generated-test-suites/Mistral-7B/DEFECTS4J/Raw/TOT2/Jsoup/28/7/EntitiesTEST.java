import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        // Test with valid named entities
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("gt"));

        // Test with invalid named entities
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity("invalid"));
        assertFalse(Entities.isNamedEntity(" "));
        assertFalse(Entities.isNamedEntity(null));
    }
}