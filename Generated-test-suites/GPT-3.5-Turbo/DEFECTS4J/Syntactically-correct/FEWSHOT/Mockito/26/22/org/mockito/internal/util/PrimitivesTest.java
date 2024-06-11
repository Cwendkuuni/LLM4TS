package org.mockito.internal.util;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PrimitivesTest {
    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(Boolean.TYPE, Primitives.primitiveTypeOf(Boolean.TYPE));
        assertEquals(Character.TYPE, Primitives.primitiveTypeOf(Character.TYPE));
        assertEquals(Byte.TYPE, Primitives.primitiveTypeOf(Byte.TYPE));
        assertEquals(Short.TYPE, Primitives.primitiveTypeOf(Short.TYPE));
        assertEquals(Integer.TYPE, Primitives.primitiveTypeOf(Integer.TYPE));
        assertEquals(Long.TYPE, Primitives.primitiveTypeOf(Long.TYPE));
        assertEquals(Float.TYPE, Primitives.primitiveTypeOf(Float.TYPE));
        assertEquals(Double.TYPE, Primitives.primitiveTypeOf(Double.TYPE));
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
        assertEquals(false, Primitives.isPrimitiveWrapper(Boolean.TYPE));
        assertEquals(false, Primitives.isPrimitiveWrapper(Character.TYPE));
        assertEquals(false, Primitives.isPrimitiveWrapper(Byte.TYPE));
        assertEquals(false, Primitives.isPrimitiveWrapper(Short.TYPE));
        assertEquals(false, Primitives.isPrimitiveWrapper(Integer.TYPE));
        assertEquals(false, Primitives.isPrimitiveWrapper(Long.TYPE));
        assertEquals(false, Primitives.isPrimitiveWrapper(Float.TYPE));
        assertEquals(false, Primitives.isPrimitiveWrapper(Double.TYPE));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(false, Primitives.primitiveWrapperOf(Boolean.TYPE));
        assertEquals('\u0000', Primitives.primitiveWrapperOf(Character.TYPE));
        assertEquals((byte) 0, Primitives.primitiveWrapperOf(Byte.TYPE));
        assertEquals((short) 0, Primitives.primitiveWrapperOf(Short.TYPE));
        assertEquals(0, Primitives.primitiveWrapperOf(Integer.TYPE));
        assertEquals(0L, Primitives.primitiveWrapperOf(Long.TYPE));
        assertEquals(0F, Primitives.primitiveWrapperOf(Float.TYPE));
        assertEquals(0D, Primitives.primitiveWrapperOf(Double.TYPE));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals(false, Primitives.primitiveValueOrNullFor(Boolean.TYPE));
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(Character.TYPE));
        assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(Byte.TYPE));
        assertEquals((short) 0, Primitives.primitiveValueOrNullFor(Short.TYPE));
        assertEquals(0, Primitives.primitiveValueOrNullFor(Integer.TYPE));
        assertEquals(0L, Primitives.primitiveValueOrNullFor(Long.TYPE));
        assertEquals(0F, Primitives.primitiveValueOrNullFor(Float.TYPE));
        assertEquals(0D, Primitives.primitiveValueOrNullFor(Double.TYPE));
    }
}