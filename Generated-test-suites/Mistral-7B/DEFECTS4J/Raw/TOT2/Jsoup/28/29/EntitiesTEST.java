import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Basic test case
        assertTrue(Entities.isNamedEntity("lt"));

        // Edge cases
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));

        // Exception handling
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
    }
}