package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PrimitivesTest {
    
    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(boolean.class, Primitives.primitiveTypeOf(Boolean.class));
        assertEquals(char.class, Primitives.primitiveTypeOf(Character.class));
        assertEquals(byte.class, Primitives.primitiveTypeOf(Byte.class));
        assertEquals(short.class, Primitives.primitiveTypeOf(Short.class));
        assertEquals(int.class, Primitives.primitiveTypeOf(Integer.class));
        assertEquals(long.class, Primitives.primitiveTypeOf(Long.class));
        assertEquals(float.class, Primitives.primitiveTypeOf(Float.class));
        assertEquals(double.class, Primitives.primitiveTypeOf(Double.class));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        assertEquals(true, Primitives.isPrimitiveWrapper(Boolean.class));
        assertEquals(true, Primitives.isPrimitiveWrapper(Character.class));
        assertEquals(true, Primitives.isPrimitiveWrapper(Byte.class));
        assertEquals(true, Primitives.isPrimitiveWrapper(Short.class));
        assertEquals(true, Primitives.isPrimitiveWrapper(Integer.class));
        assertEquals(true, Primitives.isPrimitiveWrapper(Long.class));
        assertEquals(true, Primitives.isPrimitiveWrapper(Float.class));
        assertEquals(true, Primitives.isPrimitiveWrapper(Double.class));
        assertEquals(false, Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals((Boolean) false, Primitives.primitiveWrapperOf(Boolean.class));
        assertEquals((Character) '\u0000', Primitives.primitiveWrapperOf(Character.class));
        assertEquals((Byte) (byte) 0, Primitives.primitiveWrapperOf(Byte.class));
        assertEquals((Short) (short) 0, Primitives.primitiveWrapperOf(Short.class));
        assertEquals((Integer) 0, Primitives.primitiveWrapperOf(Integer.class));
        assertEquals((Long) 0L, Primitives.primitiveWrapperOf(Long.class));
        assertEquals((Float) 0F, Primitives.primitiveWrapperOf(Float.class));
        assertEquals((Double) 0D, Primitives.primitiveWrapperOf(Double.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class));
        assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(byte.class));
        assertEquals((short) 0, Primitives.primitiveValueOrNullFor(short.class));
        assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
        assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class));
        assertEquals(0F, Primitives.primitiveValueOrNullFor(float.class));
        assertEquals(0D, Primitives.primitiveValueOrNullFor(double.class));
    }
}