import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType simpleType = SimpleType.construct(Map.class);
            fail("Should throw IllegalArgumentException for Map class");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }

        try {
            SimpleType simpleType = SimpleType.construct(Collection.class);
            fail("Should throw IllegalArgumentException for Collection class");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }

        try {
            SimpleType simpleType = SimpleType.construct(String[].class);
            fail("Should throw IllegalArgumentException for array class");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    // Add more test cases for other methods...

}