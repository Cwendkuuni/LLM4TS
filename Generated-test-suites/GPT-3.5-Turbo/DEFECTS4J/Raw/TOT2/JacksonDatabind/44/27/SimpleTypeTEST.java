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
    public void testConstructException() {
        try {
            SimpleType.construct(Map.class);
            fail("Exception not thrown for constructing SimpleType with Map");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructArray() {
        try {
            SimpleType.construct(String[].class);
            fail("Exception not thrown for constructing SimpleType with array");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}