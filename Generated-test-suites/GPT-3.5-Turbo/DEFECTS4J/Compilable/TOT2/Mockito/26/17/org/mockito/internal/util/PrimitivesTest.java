package org.mockito.internal.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        // Typical use case
        Class<Integer> result = Primitives.primitiveTypeOf(Integer.class);
        assertEquals(Integer.TYPE, result);

        // Edge case - passing a primitive type
        Class<String> result2 = Primitives.primitiveTypeOf(String.class);
        assertNull(result2);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        // Typical use case
        boolean result = Primitives.isPrimitiveWrapper(Integer.class);
        assertTrue(result);

        // Edge case - passing a non-wrapper class
        boolean result2 = Primitives.isPrimitiveWrapper(String.class);
        assertFalse(result2);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        // Typical use case
        Integer result = Primitives.primitiveWrapperOf(int.class);
        assertEquals(Integer.valueOf(0), result);

        // Edge case - passing an unsupported type
        String result2 = Primitives.primitiveWrapperOf(String.class);
        assertNull(result2);
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        // Typical use case
        Integer result = Primitives.primitiveValueOrNullFor(int.class);
        assertEquals(Integer.valueOf(0), result);

        // Edge case - passing a non-primitive type
        String result2 = Primitives.primitiveValueOrNullFor(String.class);
        assertNull(result2);
    }
}