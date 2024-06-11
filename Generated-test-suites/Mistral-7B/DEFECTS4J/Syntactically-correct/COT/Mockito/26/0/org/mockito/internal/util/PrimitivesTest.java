package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PrimitivesTest {
    

    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> intClass = Integer.class;
        Class<Integer> intType = Primitives.primitiveTypeOf(intClass);
        assertTrue(intType.isPrimitive());
        assertSame(intType, Integer.TYPE);

        Class<String> stringClass = String.class;
        assertFalse(Primitives.primitiveTypeOf(stringClass).isPrimitive());
    }

    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertTrue(Primitives.isPrimitiveWrapper(Character.class));
        assertTrue(Primitives.isPrimitiveWrapper(Byte.class));
        assertTrue(PrMITives.isPrimitiveWrapper(Short.class));
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
        assertTrue(Primitives.isPrimitiveWrapper(Long.class));
        assertTrue(Primitives.isPrimitiveWrapper(Float.class));
        assertTrue(Primitives.isPrimitiveWrapper(Double.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertSame(false, Primitives.primitiveWrapperOf(Boolean.class));
        assertSame('\u0000', Primitives.primitiveWrapperOf(Character.class));
        assertSame((byte) 0, Primitives.primitiveWrapperOf(Byte.class));
        assertSame((short) 0, Primitives.primitiveWrapperOf(Short.class));
        assertSame(0, Primitives.primitiveWrapperOf(Integer.class));
        assertSame(0L, Primitives.primitiveWrapperOf(Long.class));
        assertSame(0F, Primitives.primitiveWrapperOf(Float.class));
        assertSame(0D, Primitives.primitiveWrapperOf(Double.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertSame(false, Primitives.primitiveValueOrNullFor(boolean.class));
        assertSame('\u0000', Primitives.primitiveValueOrNullFor(char.class));
        assertSame((byte) 0, Primitives.primitiveValueOrNullFor(byte.class));
        assertSame((short) 0, Primitives.primitiveValueOrNullFor(short.class));
        assertSame(0, Primitives.primitiveValueOrNullFor(int.class));
        assertSame(0L, Primitives.primitiveValueOrNullFor(long.class));
        assertSame(0F, Primitives.primitiveValueOrNullFor(float.class));
        assertSame(0D, Primitives.primitiveValueOrNullFor(double.class));
    }

    @Test(expected = NullPointerException.class)
    public void testNullClassPassedToPrimitiveTypeOf() {
        Primitives.primitiveTypeOf(null);
    }

    @Test(expected = ClassCastException.class)
    public void testNonPrimitiveClassPassedToPrimitiveTypeOf() {
        Primitives.primitiveTypeOf(String.class);
    }

    @Test(expected = NullPointerException.class)
    public void testNullClassPassedToIsPrimitiveWrapper() {
        Primitives.isPrimitiveWrapper(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullClassPassedToPrimitiveWrapperOf() {
        Primitives.primitiveWrapperOf(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullClassPassedToPrimitiveValueOrNullFor() {
        Primitives.primitiveValueOrNullFor(null);
    }

    
}