package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertTrue(Primitives.isPrimitiveWrapper(Character.class));
        assertTrue(Primitives.isPrimitiveWrapper(Byte.class));
        assertTrue(Primitives.isPrimitiveWrapper(Short.class));
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
        assertTrue(Primitives.isPrimitiveWrapper(Long.class));
        assertTrue(Primitives.isPrimitiveWrapper(Float.class));
        assertTrue(Primitives.isPrimitiveWrapper(Double.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertNotNull(Primitives.primitiveWrapperOf(boolean.class));
        assertNotNull(Primitives.primitiveWrapperOf(char.class));
        assertNotNull(Primitives.primitiveWrapperOf(byte.class));
        assertNotNull(Primitives.primitiveWrapperOf(short.class));
        assertNotNull(Primitives.primitiveWrapperOf(int.class));
        assertNotNull(Primitives.primitiveWrapperOf(long.class));
        assertNotNull(Primitives.primitiveWrapperOf(float.class));
        assertNotNull(Primitives.primitiveWrapperOf(double.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertNotNull(Primitives.primitiveValueOrNullFor(boolean.class));
        assertNotNull(Primitives.primitiveValueOrNullFor(char.class));
        assertNotNull(Primitives.primitiveValueOrNullFor(byte.class));
        assertNotNull(Primitives.primitiveValueOrNullFor(short.class));
        assertNotNull(Primitives.primitiveValueOrNullFor(int.class));
        assertNotNull(Primitives.primitiveValueOrNullFor(long.class));
        assertNotNull(Primitives.primitiveValueOrNullFor(float.class));
        assertNotNull(Primitives.primitiveValueOrNullFor(double.class));
    }
}