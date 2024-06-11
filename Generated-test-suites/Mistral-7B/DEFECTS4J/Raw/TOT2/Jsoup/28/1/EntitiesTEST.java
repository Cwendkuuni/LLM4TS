import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.jsoup.nodes.Entities.isNamedEntity;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test known named entities
        assertTrue(isNamedEntity("lt"));
        assertTrue(isNamedEntity("amp"));
        assertTrue(isNamedEntity("apos"));
        assertTrue(isNamedEntity("quot"));

        // Test unknown named entities
        assertFalse(isNamedEntity("unknown"));
    }
}