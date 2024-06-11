package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        
        // Test non-primitive type
        assertEquals(String.class, Primitives.primitiveTypeOf(String.class));
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
        
        // Test non-wrapper type
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
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
        assertEquals(Double.valueOf(0D), Primitives.primitiveWrapperOf(double.class));
        
        // Test non-primitive type
        assertNull(Primitives.primitiveWrapperOf(String.class));
    }
    
    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class));
        assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(byte.class));
        assertEquals((short) 0, Primitives.primitiveValueOrNullFor(short.class));
        assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
        assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class));
        assertEquals(0F, Primitives.primitiveValueOrNullFor(float.class), 0.0F);
        assertEquals(0D, Primitives.primitiveValueOrNullFor(double.class), 0.0);
        
        // Test non-primitive type
        assertNull(Primitives.primitiveValueOrNullFor(String.class));
    }
    
    @Test
    public void testWrapperTypes() {
        assertNotNull(Primitives.wrapperTypes);
        assertFalse(Primitives.wrapperTypes.isEmpty());
        // Add specific assertions for wrapperTypes
        // ...
    }
    
    @Test
    public void testPrimitiveTypes() {
        assertNotNull(Primitives.primitiveTypes);
        assertFalse(Primitives.primitiveTypes.isEmpty());
        // Add specific assertions for primitiveTypes
        // ...
    }
    
    @Test
    public void testWrapperReturnValues() {
        assertNotNull(Primitives.wrapperReturnValues);
        assertFalse(Primitives.wrapperReturnValues.isEmpty());
        // Add specific assertions for wrapperReturnValues
        // ...
    }
    
    @Test
    public void testPrimitiveValues() {
        assertNotNull(Primitives.primitiveValues);
        assertFalse(Primitives.primitiveValues.isEmpty());
        // Add specific assertions for primitiveValues
        // ...
    }
}