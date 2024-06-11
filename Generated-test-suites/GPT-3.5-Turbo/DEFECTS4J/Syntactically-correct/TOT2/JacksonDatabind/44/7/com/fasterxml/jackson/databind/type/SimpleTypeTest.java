import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstruct_withMap() {
        try {
            SimpleType.construct(Map.class);
            fail("Should throw IllegalArgumentException for Map class");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testConstruct_withCollection() {
        try {
            SimpleType.construct(Collection.class);
            fail("Should throw IllegalArgumentException for Collection class");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

}