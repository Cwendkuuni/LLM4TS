package org.mockito.internal.util;
import static org.junit.Assert.*;
import org.junit.Test;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(Boolean.TYPE, Primitives.primitiveTypeOf(boolean.class));
        assertEquals(Integer.TYPE, Primitives.primitiveTypeOf(int.class));
        // additional test case for non-primitive class
        assertEquals(null, Primitives.primitiveTypeOf(String.class));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
        // additional test case for non-primitive wrapper class
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(boolean.class));
        assertEquals(Integer.valueOf(0), Primitives.primitiveWrapperOf(int.class));
        // additional test case for non-primitive class
        assertEquals(null, Primitives.primitiveWrapperOf(String.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals(Boolean.FALSE, Primitives.primitiveValueOrNullFor(boolean.class));
        assertEquals(Integer.valueOf(0), Primitives.primitiveValueOrNullFor(int.class));
        // additional test case for non-primitive class
        assertEquals(null, Primitives.primitiveValueOrNullFor(String.class));
    }

}