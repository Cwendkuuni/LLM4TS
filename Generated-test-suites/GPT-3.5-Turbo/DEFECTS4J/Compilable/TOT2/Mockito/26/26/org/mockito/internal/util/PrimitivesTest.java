package org.mockito.internal.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Boolean> result = Primitives.primitiveTypeOf(Boolean.class);
        assertEquals(Boolean.TYPE, result);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        boolean result = Primitives.isPrimitiveWrapper(Integer.class);
        assertFalse(result);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Integer result = Primitives.primitiveWrapperOf(Integer.class);
        assertNull(result);
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Object result = Primitives.primitiveValueOrNullFor(boolean.class);
        assertEquals(false, result);
    }

    // Additional test cases for edge cases and exceptions
    
    @Test
    public void testPrimitiveTypeOfEdgeCase() {
        Class<Integer> result = Primitives.primitiveTypeOf(Integer.class);
        assertNull(result);
    }

    @Test
    public void testIsPrimitiveWrapperEdgeCase() {
        boolean result = Primitives.isPrimitiveWrapper(int.class);
        assertFalse(result);
    }

    @Test
    public void testPrimitiveWrapperOfException() {
        try {
            Primitives.primitiveWrapperOf(String.class);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot find primitive wrapper for type java.lang.String", e.getMessage());
        }
    }

    @Test
    public void testPrimitiveValueOrNullForEdgeCase() {
        Object result = Primitives.primitiveValueOrNullFor(Integer.class);
        assertNull(result);
    }
}