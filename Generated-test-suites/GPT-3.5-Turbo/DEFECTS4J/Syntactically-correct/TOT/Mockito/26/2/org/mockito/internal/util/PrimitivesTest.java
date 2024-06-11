import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void primitiveTypeOf_Test() {
        // Typical use case - passing a wrapper class
        Class<Integer> result = Primitives.primitiveTypeOf(Integer.class);
        assertEquals(int.class, result);

        // Edge case - passing a primitive class
        Class<Boolean> result2 = Primitives.primitiveTypeOf(boolean.class);
        assertEquals(boolean.class, result2);

        // Error scenario - passing a non-primitive, non-wrapper class
        // Expected: ClassCastException
        try {
            Primitives.primitiveTypeOf(String.class);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Pass
        }
    }

    @Test
    public void isPrimitiveWrapper_Test() {
        // Typical use case - passing a wrapper class
        boolean result = Primitives.isPrimitiveWrapper(Integer.class);
        assertFalse(result);

        // Edge case - passing a primitive class
        boolean result2 = Primitives.isPrimitiveWrapper(int.class);
        assertTrue(result2);

        // Error scenario - passing a non-wrapper class
        boolean result3 = Primitives.isPrimitiveWrapper(String.class);
        assertFalse(result3);
    }

    @Test
    public void primitiveWrapperOf_Test() {
        // Typical use case - passing a wrapper class
        Integer result = Primitives.primitiveWrapperOf(int.class);
        assertEquals(Integer.class, result.getClass());

        // Edge case - passing a primitive class
        Integer result2 = Primitives.primitiveWrapperOf(Integer.TYPE);
        assertEquals(Integer.class, result2.getClass());

        // Error scenario - passing a non-primitive class
        // Expected: ClassCastException
        try {
            Primitives.primitiveWrapperOf(String.class);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Pass
        }
    }

    @Test
    public void primitiveValueOrNullFor_Test() {
        // Typical use case - passing a primitive class
        boolean result1 = Primitives.primitiveValueOrNullFor(boolean.class);
        assertFalse(result1);

        // Edge case - passing a wrapper class
        Character result2 = Primitives.primitiveValueOrNullFor(Character.class);
        assertEquals('\u0000', (char) result2);

        // Error scenario - passing a non-primitive class
        // Expected: ClassCastException
        try {
            Primitives.primitiveValueOrNullFor(String.class);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Pass
        }
    }
}