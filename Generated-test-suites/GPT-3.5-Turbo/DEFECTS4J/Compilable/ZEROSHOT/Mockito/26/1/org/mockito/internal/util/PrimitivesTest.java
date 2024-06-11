package org.mockito.internal.util;
import org.junit.Test;

import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> clazz = Integer.class;
        Class<Integer> expected = Integer.TYPE;
        Class<Integer> result = Primitives.primitiveTypeOf(clazz);
        assertEquals(expected, result);
        
        // Add more test cases here
    }
    
    @Test
    public void testIsPrimitiveWrapper() {
        Class<Boolean> type = Boolean.class;
        boolean expected = true;
        boolean result = Primitives.isPrimitiveWrapper(type);
        assertEquals(expected, result);
        
        // Add more test cases here
    }
    
    @Test
    public void testPrimitiveWrapperOf() {
        Class<Double> type = Double.class;
        Double expected = 0D;
        Double result = Primitives.primitiveWrapperOf(type);
        assertEquals(expected, result);
        
        // Add more test cases here
    }
    
    @Test
    public void testPrimitiveValueOrNullFor() {
        Class<Character> primitiveType = Character.class;
        Character expected = '\u0000';
        Character result = Primitives.primitiveValueOrNullFor(primitiveType);
        assertEquals(expected, result);
        
        // Add more test cases here
    }
}