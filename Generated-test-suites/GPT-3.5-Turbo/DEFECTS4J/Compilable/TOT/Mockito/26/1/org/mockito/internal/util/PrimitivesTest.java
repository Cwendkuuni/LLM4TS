package org.mockito.internal.util;
import org.junit.Test;

import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        // Typical use case
        assertEquals(boolean.class, Primitives.primitiveTypeOf(Boolean.class));

        // Edge case
        assertEquals(int.class, Primitives.primitiveTypeOf(Integer.class));

        // Error scenario
        assertNull(Primitives.primitiveTypeOf(String.class));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        // Typical use case
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));

        // Edge case
        assertFalse(Primitives.isPrimitiveWrapper(String.class));

        // Error scenario
        assertFalse(Primitives.isPrimitiveWrapper(null));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        // Typical use case
        assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(boolean.class));

        // Edge case
        assertEquals(Byte.valueOf((byte) 0), Primitives.primitiveWrapperOf(byte.class));

        // Error scenario
        assertNull(Primitives.primitiveWrapperOf(null));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        // Typical use case
        assertEquals(Boolean.FALSE, Primitives.primitiveValueOrNullFor(boolean.class));

        // Edge case
        assertEquals('\u0000', (char) Primitives.primitiveValueOrNullFor(char.class));

        // Error scenario
        assertNull(Primitives.primitiveValueOrNullFor(String.class));
    }
}