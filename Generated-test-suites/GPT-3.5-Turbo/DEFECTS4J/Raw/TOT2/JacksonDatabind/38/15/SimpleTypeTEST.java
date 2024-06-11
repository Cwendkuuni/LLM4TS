import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstructWithMap() {
        try {
            SimpleType.construct(Map.class);
            fail("Should throw IllegalArgumentException for Map");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testConstructWithCollection() {
        try {
            SimpleType.construct(Collection.class);
            fail("Should throw IllegalArgumentException for Collection");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testConstructWithArray() {
        try {
            SimpleType.construct(String[].class);
            fail("Should throw IllegalArgumentException for Array");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    // Additional test cases for other methods can be added here

}