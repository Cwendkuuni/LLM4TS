package org.mockito.internal.util;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PrimitivesTest {
    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(boolean.class, Primitives.primitiveTypeOf(Boolean.TYPE));
        assertEquals(char.class, Primitives.primitiveTypeOf(Character.TYPE));
        assertEquals(byte.class, Primitives.primitiveTypeOf(Byte.TYPE));
        assertEquals(short.class, Primitives.primitiveTypeOf(Short.TYPE));
        assertEquals(int.class, Primitives.primitiveTypeOf(Integer.TYPE));
        assertEquals(long.class, Primitives.primitiveTypeOf(Long.TYPE));
        assertEquals(float.class, Primitives.primitiveTypeOf(Float.TYPE));
        assertEquals(double.class, Primitives.primitiveTypeOf(Double.TYPE));
        assertEquals(null, Primitives.primitiveTypeOf(String.class));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(Boolean.TYPE));
        assertNull(Primitives.primitiveWrapperOf(String.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals(false, Primitives.primitiveValueOrNullFor(Boolean.TYPE));
        assertNull(Primitives.primitiveValueOrNullFor(String.class));
    }
}