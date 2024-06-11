import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
    }
    
    @Test
    public void testConstructError() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(simpleType, simpleType.narrow(String.class));
    }

    @Test
    public void testWithContentTypeError() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentType(new SimpleType(Boolean.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("java.lang.String", simpleType.buildCanonicalName());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertTrue(simpleType1.equals(simpleType2));
    }

}