import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        // Test for valid named entities
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));

        // Test for invalid named entities
        assertFalse(Entities.isNamedEntity("invalidEntity"));
    }
}