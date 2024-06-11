import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testConstructArray() {
        try {
            SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType narrowedType = (SimpleType) simpleType._narrow(String.class);
        assertEquals(narrowedType, simpleType);
    }

    @Test
    public void testWithStringContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentType(String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        Object typeHandler = new Object();
        SimpleType typeWithHandler = simpleType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, typeWithHandler.getTypeHandler());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        JavaType refinedType = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(String.class);
        assertEquals(simpleType1, simpleType2);
    }
}