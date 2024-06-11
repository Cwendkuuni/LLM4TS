import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstructException() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstructArray() {
        try {
            SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Add more test cases for other public methods

}