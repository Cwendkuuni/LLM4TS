
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
        assertEquals(char.class, Primitives.primitiveTypeOf(char.class));
        assertEquals(byte.class, Primitives.primitiveTypeOf(byte.class));
        assertEquals(short.class, Primitives.primitiveTypeOf(short.class));
        assertEquals(int.class, Primitives.primitiveTypeOf(int.class));
        assertEquals(long.class, Primitives.primitiveTypeOf(long.class));
        assertEquals(float.class, Primitives.primitiveTypeOf(float.class));
        assertEquals(double.class, Primitives.primitiveTypeOf(double.class));

        assertEquals(boolean.class, Primitives.primitiveTypeOf(Boolean.class));
        assertEquals(char.class, Primitives.primitiveTypeOf(Character.class));
        assertEquals(byte.class, Primitives.primitiveTypeOf(Byte.class));
        assertEquals(short.class, Primitives.primitiveTypeOf(Short.class));
        assertEquals(int.class, Primitives.primitiveTypeOf(Integer.class));
        assertEquals(long.class, Primitives.primitiveTypeOf(Long.class));
        assertEquals(float.class, Primitives.primitiveTypeOf(Float.class));
        assertEquals(double.class, Primitives.primitiveTypeOf(Double.class));

        assertNull(Primitives.primitiveTypeOf(String.class));
        assertNull(Primitives.primitiveTypeOf(Object.class));
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

        assertFalse(Primitives.isPrimitiveWrapper(boolean.class));
        assertFalse(Primitives.isPrimitiveWrapper(char.class));
        assertFalse(Primitives.isPrimitiveWrapper(byte.class));
        assertFalse(Primitives.isPrimitiveWrapper(short.class));
        assertFalse(Primitives.isPrimitiveWrapper(int.class));
        assertFalse(Primitives.isPrimitiveWrapper(long.class));
        assertFalse(Primitives.isPrimitiveWrapper(float.class));
        assertFalse(Primitives.isPrimitiveWrapper(double.class));

        assertFalse(Primitives.isPrimitiveWrapper(String.class));
        assertFalse(Primitives.isPrimitiveWrapper(Object.class));
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
        assertEquals(Double.valueOf(0), Primitives.primitiveWrapperOf(double.class));

        assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(Boolean.class));
        assertEquals(Character.valueOf('\u0000'), Primitives.primitiveWrapperOf(Character.class));
        assertEquals(Byte.valueOf((byte) 0), Primitives.primitiveWrapperOf(Byte.class));
        assertEquals(Short.valueOf((short) 0), Primitives.primitiveWrapperOf(Short.class));
        assertEquals(Integer.valueOf(0), Primitives.primitiveWrapperOf(Integer.class));
        assertEquals(Long.valueOf(0L), Primitives.primitiveWrapperOf(Long.class));
        assertEquals(Float.valueOf(0F), Primitives.primitiveWrapperOf(Float.class));
        assertEquals(Double.valueOf(0), Primitives.primitiveWrapperOf(Double.class));

        assertNull(Primitives.primitiveWrapperOf(String.class));
        assertNull(Primitives.primitiveWrapperOf(Object.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class).booleanValue());
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class).charValue());
        assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(byte.class).byteValue());
        assertEquals((short) 0, Primitives.primitiveValueOrNullFor(short.class).shortValue());
        assertEquals(0, Primitives.primitiveValueOrNullFor(int.class).intValue());
        assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class).longValue());
        assertEquals(0F, Primitives.primitiveValueOrNullFor(float.class).floatValue(), 0.0);
        assertEquals(0, Primitives.primitiveValueOrNullFor(double.class).doubleValue(), 0.0);

        assertNull(Primitives.primitiveValueOrNullFor(String.class));
        assertNull(Primitives.primitiveValueOrNullFor(Object.class));
    }
}
