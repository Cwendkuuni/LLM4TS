package org.mockito.internal.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        // typical use case
        Class<?> result = Primitives.primitiveTypeOf(Boolean.class);
        assertEquals(boolean.class, result);

        // edge case
        result = Primitives.primitiveTypeOf(String.class);
        assertNull(result);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        // typical use case
        boolean result = Primitives.isPrimitiveWrapper(Integer.class);
        assertTrue(result);

        // edge case
        result = Primitives.isPrimitiveWrapper(String.class);
        assertFalse(result);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        // typical use case
        Integer result = Primitives.primitiveWrapperOf(int.class);
        assertEquals(Integer.valueOf(0), result);

        // edge case
        assertNull(Primitives.primitiveWrapperOf(String.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        // typical use case
        int result = Primitives.primitiveValueOrNullFor(Integer.TYPE);
        assertEquals(0, result);

        // edge case
        assertNull(Primitives.primitiveValueOrNullFor(String.class));
    }
}