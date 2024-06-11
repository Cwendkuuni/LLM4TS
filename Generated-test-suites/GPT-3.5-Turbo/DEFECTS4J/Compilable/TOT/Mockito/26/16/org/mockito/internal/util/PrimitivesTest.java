package org.mockito.internal.util;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        // Test typical use case
        assertEquals(boolean.class, Primitives.primitiveTypeOf(Boolean.class));

        // Test edge case with char
        assertEquals(char.class, Primitives.primitiveTypeOf(Character.class));

        // Test error scenario with unknown type
        assertNull(Primitives.primitiveTypeOf(String.class));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        // Test typical use case
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));

        // Test edge case with boolean
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));

        // Test error scenario with unknown type
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        // Test typical use case with int
        assertEquals(Integer.valueOf(0), Primitives.primitiveWrapperOf(int.class));

        // Test edge case with char
        assertEquals(Character.valueOf('\u0000'), Primitives.primitiveWrapperOf(char.class));

        // Test error scenario with unknown type
        assertNull(Primitives.primitiveWrapperOf(String.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        // Test typical use case with int
        assertEquals(0, (int) Primitives.primitiveValueOrNullFor(Integer.class));

        // Test edge case with boolean
        assertEquals(false, (boolean) Primitives.primitiveValueOrNullFor(Boolean.class));

        // Test error scenario with unknown type
        assertNull(Primitives.primitiveValueOrNullFor(String.class));
    }
}