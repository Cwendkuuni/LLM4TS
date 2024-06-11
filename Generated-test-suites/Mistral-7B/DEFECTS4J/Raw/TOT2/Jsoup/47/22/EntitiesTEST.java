import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        // Other test cases for different named entities, base named entities, and edge cases can be added here.
    }
}