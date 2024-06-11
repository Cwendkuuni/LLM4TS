import static org.junit.Assert.assertTrue;
import org.jsoup.nodes.Entities;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        // Test for known named entities
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("gt"));

        // Test for unknown named entities
        assertFalse(Entities.isNamedEntity("unknown"));
    }
}