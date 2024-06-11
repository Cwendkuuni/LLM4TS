
package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PrimitivesTest {
    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(boolean.class, Primitives.primitiveTypeOf(boolean.class));
        assertEquals(byte.class, Primitives.primitiveTypeOf(Byte.class));
        assertEquals(short.class, Primitives.primitiveTypeOf(Short.class));
        assertEquals(int.class, Primitives.primitiveTypeOf(Integer.class));
        assertEquals(long.class, Primitives.primitiveTypeOf(Long.class));
        assertEquals(float.class, Primitives.primitiveTypeOf(Float.class));
        assertEquals(double.class, Primitives.primitiveTypeOf(Double.class));
        assertNull(Primitives.primitiveTypeOf(String.class));
        assertNull(Primitives.primitiveTypeOf(null));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertTrue(Primitives.isPrimitiveWrapper(Character.class));
        assertTrue(Primitives.isPrimitiveWrapper(Byte.class));
        assertTrue(Primitives.isPrimitiveWrapper(Short.class));
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
        assertTrue(Primitives.isPrimitiveWrapper(Long.class));
        assertTrue(Primitives.isPrimitiveWrapper(Float.class));
        assertTrue(Primitives.isPrimitiveWrapper(Double.class));
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
        assertFalse(Primitives.isPrimitiveWrapper(null));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(boolean.class));
        assertEquals(Character.valueOf('\u0000'), Primitives.primitiveWrapperOf(char.class));
        assertEquals(Byte.valueOf((byte) 0), Primitives.primitiveWrapperOf(byte.class));
        assertEquals(Short.valueOf((short) 0), Primitives.primitiveWrapperOf(short.class));
        assertEquals(Integer.valueOf(0), Primitives.primitiveWrapperOf(int.class));
        assertEquals(Long.valueOf(0L), Primitives.primitiveWrapperOf(long.class));
        assertEquals(Float.valueOf(0F), Primitives.primitiveWrapperOf(float.class));
        assertEquals(Double.valueOf(0D), Primitives.primitiveWrapperOf(double.class));
        assertNull(Primitives.primitiveWrapperOf(String.class));
        assertNull(Primitives.primitiveWrapperOf(null));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertFalse(Primitives.primitiveValueOrNullFor(boolean.class));
        assertEquals('\u0000', (char) Primitives.primitiveValueOrNullFor(char.class));
        assertEquals((byte) 0, (byte) Primitives.primitiveValueOrNullFor(byte.class));
        assertEquals((short) 0, (short) Primitives.primitiveValueOrNullFor(short.class));
        assertEquals(0, (int) Primitives.primitiveValueOrNullFor(int.class));
        assertEquals(0L, (long) Primitives.primitiveValueOrNullFor(long.class));
        assertEquals(0F, (float) Primitives.primitiveValueOrNullFor(float.class), 0);
        assertEquals(0D, (double) Primitives.primitiveValueOrNullFor(double.class), 0);
        assertNull(Primitives.primitiveValueOrNullFor(String.class));
        assertNull(Primitives.primitiveValueOrNullFor(null));
    }
}
