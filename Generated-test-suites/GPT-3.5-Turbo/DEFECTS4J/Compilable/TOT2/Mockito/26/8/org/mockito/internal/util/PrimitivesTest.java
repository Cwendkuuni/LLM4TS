package org.mockito.internal.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        // Typical use case
        assertEquals(Boolean.TYPE, Primitives.primitiveTypeOf(Boolean.class));

        // Edge case
        assertNull(Primitives.primitiveTypeOf(String.class));

        // Error scenario
        assertThrows(ClassCastException.class, () -> Primitives.primitiveTypeOf(Object.class));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        // Typical use case
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));

        // Edge case
        assertFalse(Primitives.isPrimitiveWrapper(String.class));

        // Error scenario
        assertThrows(ClassCastException.class, () -> Primitives.isPrimitiveWrapper(Object.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        // Typical use case
        assertEquals(Integer.valueOf(0), Primitives.primitiveWrapperOf(int.class));

        // Edge case
        assertNull(Primitives.primitiveWrapperOf(String.class));

        // Error scenario
        assertThrows(ClassCastException.class, () -> Primitives.primitiveWrapperOf(Object.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        // Typical use case
        assertEquals(Boolean.FALSE, Primitives.primitiveValueOrNullFor(boolean.class));

        // Edge case
        assertNull(Primitives.primitiveValueOrNullFor(String.class));

        // Error scenario
        assertThrows(ClassCastException.class, () -> Primitives.primitiveValueOrNullFor(Object.class));
    }
}