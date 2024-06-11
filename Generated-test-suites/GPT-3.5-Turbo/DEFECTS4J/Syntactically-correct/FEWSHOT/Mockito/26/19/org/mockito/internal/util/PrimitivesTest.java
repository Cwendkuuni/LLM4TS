package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PrimitivesTest {
    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(Boolean.TYPE, Primitives.primitiveTypeOf(boolean.class));
        assertEquals(Character.TYPE, Primitives.primitiveTypeOf(char.class));
        assertEquals(Byte.TYPE, Primitives.primitiveTypeOf(byte.class));
        assertEquals(Short.TYPE, Primitives.primitiveTypeOf(short.class));
        assertEquals(Integer.TYPE, Primitives.primitiveTypeOf(int.class));
        assertEquals(Long.TYPE, Primitives.primitiveTypeOf(long.class));
        assertEquals(Float.TYPE, Primitives.primitiveTypeOf(float.class));
        assertEquals(Double.TYPE, Primitives.primitiveTypeOf(double.class));
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
    }
    
    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(false, Primitives.primitiveWrapperOf(boolean.class));
        assertEquals('\u0000', Primitives.primitiveWrapperOf(char.class));
        assertEquals((byte) 0, Primitives.primitiveWrapperOf(byte.class));
        assertEquals((short) 0, Primitives.primitiveWrapperOf(short.class));
        assertEquals(0, Primitives.primitiveWrapperOf(int.class));
        assertEquals(0L, Primitives.primitiveWrapperOf(long.class));
        assertEquals(0F, Primitives.primitiveWrapperOf(float.class));
        assertEquals(0D, Primitives.primitiveWrapperOf(double.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals(false, Primitives.primitiveValueOrNullFor(Boolean.class));
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(Character.class));
        assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(Byte.class));
        assertEquals((short) 0, Primitives.primitiveValueOrNullFor(Short.class));
        assertEquals(0, Primitives.primitiveValueOrNullFor(Integer.class));
        assertEquals(0L, Primitives.primitiveValueOrNullFor(Long.class));
        assertEquals(0F, Primitives.primitiveValueOrNullFor(Float.class));
        assertEquals(0D, Primitives.primitiveValueOrNullFor(Double.class));
    }
}