package org.mockito.internal.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        // Typical use case
        assertEquals(boolean.class, Primitives.primitiveTypeOf(Boolean.class));

        // Edge case
        assertEquals(int.class, Primitives.primitiveTypeOf(Integer.class));

        // Error scenario
        assertNull(Primitives.primitiveTypeOf(Object.class));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        // Typical use case
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));

        // Edge case
        assertTrue(Primitives.isPrimitiveWrapper(Float.class));

        // Error scenario
        assertFalse(Primitives.isPrimitiveWrapper(Object.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        // Typical use case
        assertEquals(Boolean.valueOf(false), Primitives.primitiveWrapperOf(boolean.class));

        // Edge case
        assertEquals(Float.valueOf(0F), Primitives.primitiveWrapperOf(float.class));

        // Error scenario
        assertNull(Primitives.primitiveWrapperOf(Object.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        // Typical use case
        assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class).booleanValue());

        // Edge case
        assertEquals(0D, Primitives.primitiveValueOrNullFor(double.class).doubleValue(), 0.0);

        // Error scenario
        assertNull(Primitives.primitiveValueOrNullFor(Object.class));
    }
}