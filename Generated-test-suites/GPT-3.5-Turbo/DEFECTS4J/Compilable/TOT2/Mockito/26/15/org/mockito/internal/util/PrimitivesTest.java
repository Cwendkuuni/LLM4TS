package org.mockito.internal.util;
import static org.junit.Assert.*;
import org.junit.Test;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        // Typical use case
        Class<Integer> result = Primitives.primitiveTypeOf(Integer.class);
        assertEquals(Integer.TYPE, result);
        
        // Edge case
        Class<Integer> result2 = Primitives.primitiveTypeOf(int.class);
        assertEquals(Integer.TYPE, result2);
        
        // Error scenario
        try {
            Primitives.primitiveTypeOf(Object.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testIsPrimitiveWrapper() {
        // Typical use case
        boolean result = Primitives.isPrimitiveWrapper(Integer.class);
        assertTrue(result);
        
        // Edge case
        boolean result2 = Primitives.isPrimitiveWrapper(int.class);
        assertTrue(result2);
        
        // Error scenario
        boolean result3 = Primitives.isPrimitiveWrapper(Object.class);
        assertFalse(result3);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        // Typical use case
        Integer result = Primitives.primitiveWrapperOf(Integer.TYPE);
        assertEquals(Integer.valueOf(0), result);
        
        // Edge case
        Integer result2 = Primitives.primitiveWrapperOf(int.class);
        assertEquals(Integer.valueOf(0), result2);
        
        // Error scenario
        try {
            Primitives.primitiveWrapperOf(Object.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        // Typical use case
        Integer result = Primitives.primitiveValueOrNullFor(Integer.TYPE);
        assertEquals(Integer.valueOf(0), result);
        
        // Edge case
        Integer result2 = Primitives.primitiveValueOrNullFor(int.class);
        assertEquals(Integer.valueOf(0), result2);
        
        // Error scenario
        try {
            Primitives.primitiveValueOrNullFor(Object.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }
}