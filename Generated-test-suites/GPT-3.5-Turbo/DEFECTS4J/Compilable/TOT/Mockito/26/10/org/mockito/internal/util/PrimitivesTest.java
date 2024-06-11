package org.mockito.internal.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        // typical use case
        Class<Integer> result1 = Primitives.primitiveTypeOf(int.class);
        assertEquals(int.class, result1);

        // edge case
        Class<Object> result2 = Primitives.primitiveTypeOf(Object.class);
        assertNull(result2);

        // error scenario
        Class<?> result3 = Primitives.primitiveTypeOf(null);
        assertNull(result3);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        // typical use case
        boolean result1 = Primitives.isPrimitiveWrapper(Integer.class);
        assertFalse(result1);

        // edge case
        boolean result2 = Primitives.isPrimitiveWrapper(null);
        assertFalse(result2);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        // typical use case
        Integer result1 = Primitives.primitiveWrapperOf(int.class);
        assertNull(result1);

        // edge case
        Object result2 = Primitives.primitiveWrapperOf(Object.class);
        assertNull(result2);

        // error scenario
        String result3 = Primitives.primitiveWrapperOf(String.class);
        assertNull(result3);
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        // typical use case
        Boolean result1 = Primitives.primitiveValueOrNullFor(boolean.class);
        assertEquals(false, result1);

        // edge case
        Integer result2 = Primitives.primitiveValueOrNullFor(int.class);
        assertEquals(Integer.valueOf(0), result2);

        // error scenario
        Object result3 = Primitives.primitiveValueOrNullFor(null);
        assertNull(result3);
    }
}
