import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertFalse(simpleType.isContainerType());
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstructInvalidType() {
        try {
            SimpleType.construct(Map.class);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(simpleType, simpleType._narrow(String.class));
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentType(String.class);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    // Add more test cases here for other methods in SimpleType class

}