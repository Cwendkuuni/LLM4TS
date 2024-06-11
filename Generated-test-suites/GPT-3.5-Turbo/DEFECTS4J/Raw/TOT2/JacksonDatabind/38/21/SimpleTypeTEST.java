import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
    }
    
    @Test
    public void testConstructIllegalArgumentException() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testConstructArrayIllegalArgumentException() {
        try {
            SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}