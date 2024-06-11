package org.mockito.internal.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(boolean.class, Primitives.primitiveTypeOf(boolean.class));
        assertEquals(int.class, Primitives.primitiveTypeOf(Integer.class));
        assertEquals(null, Primitives.primitiveTypeOf(String.class));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(boolean.class));
        assertEquals(Integer.valueOf(0), Primitives.primitiveWrapperOf(int.class));
        assertEquals(null, Primitives.primitiveWrapperOf(String.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals(false, (boolean) Primitives.primitiveValueOrNullFor(Boolean.class));
        assertEquals((byte) 0, (byte) Primitives.primitiveValueOrNullFor(Byte.class));
        assertEquals('\u0000', (char) Primitives.primitiveValueOrNullFor(Character.class));
        assertEquals((short) 0, (short) Primitives.primitiveValueOrNullFor(Short.class));
        assertEquals(0, (int) Primitives.primitiveValueOrNullFor(Integer.class));
        assertEquals(0L, (long) Primitives.primitiveValueOrNullFor(Long.class));
        assertEquals(0F, (float) Primitives.primitiveValueOrNullFor(Float.class), 0.001);
        assertEquals(0D, (double) Primitives.primitiveValueOrNullFor(Double.class), 0.001);
        assertEquals(null, Primitives.primitiveValueOrNullFor(String.class));
    }
}