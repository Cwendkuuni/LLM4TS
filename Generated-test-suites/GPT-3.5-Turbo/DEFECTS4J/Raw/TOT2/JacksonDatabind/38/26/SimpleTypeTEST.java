import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        // Add more assertions here if needed
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(ArrayList.class);
        assertNotNull(simpleType);
        // Add more assertions here if needed
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(ArrayList.class);
        try {
            simpleType.withContentType(String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    // Add more test cases for other methods in SimpleType class

}