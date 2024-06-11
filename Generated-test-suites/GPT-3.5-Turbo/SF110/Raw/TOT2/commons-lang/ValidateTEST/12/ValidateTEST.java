import org.junit.Test;
import static org.junit.Assert.*;

public class ValidateTest {

    @Test
    public void testIsTrueLong() {
        try {
            Validate.isTrue(false, "Custom message %d", 10l);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Custom message 10", e.getMessage());
        }
    }

    @Test
    public void testNotNull() {
        Object obj = new Object();
        assertNotNull(Validate.notNull(obj));
    }

    @Test
    public void testNotEmptyCollection() {
        List<String> list = new ArrayList<>();
        list.add("element");
        assertNotNull(Validate.notEmpty(list));
    }

    // Add more test cases for other methods

}