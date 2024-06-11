package org.mockito.internal.util;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf_primitiveType_returnPrimitiveType() {
        Class<Boolean> result = Primitives.primitiveTypeOf(boolean.class);
        Assert.assertEquals(boolean.class, result);
    }

    @Test
    public void testPrimitiveTypeOf_wrapperType_returnPrimitiveType() {
        Class<Boolean> result = Primitives.primitiveTypeOf(Boolean.class);
        Assert.assertEquals(boolean.class, result);
    }

    @Test
    public void testIsPrimitiveWrapper_primitiveWrapperType_returnTrue() {
        boolean result = Primitives.isPrimitiveWrapper(Boolean.class);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsPrimitiveWrapper_nonWrapperType_returnFalse() {
        boolean result = Primitives.isPrimitiveWrapper(String.class);
        Assert.assertFalse(result);
    }

    @Test
    public void testPrimitiveWrapperOf_primitiveType_returnWrapperInstance() {
        Boolean result = Primitives.primitiveWrapperOf(boolean.class);
        Assert.assertTrue(result instanceof Boolean);
    }

    @Test
    public void testPrimitiveWrapperOf_wrapperType_returnWrapperInstance() {
        Boolean result = Primitives.primitiveWrapperOf(Boolean.class);
        Assert.assertTrue(result instanceof Boolean);
    }

    @Test
    public void testPrimitiveValueOrNullFor_primitiveType_returnPrimitiveValue() {
        boolean result = Primitives.primitiveValueOrNullFor(boolean.class);
        Assert.assertFalse(result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_nonPrimitiveType_returnNull() {
        String result = Primitives.primitiveValueOrNullFor(String.class);
        Assert.assertNull(result);
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testPrimitiveTypeOf_nonExistingType_returnNull() {
        Class<?> result = Primitives.primitiveTypeOf(String.class);
        Assert.assertNull(result);
    }

    @Test
    public void testIsPrimitiveWrapper_nullType_returnFalse() {
        boolean result = Primitives.isPrimitiveWrapper(null);
        Assert.assertFalse(result);
    }

    @Test
    public void testPrimitiveWrapperOf_nonExistingType_returnNull() {
        Object result = Primitives.primitiveWrapperOf(String.class);
        Assert.assertNull(result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_nullType_returnNull() {
        Object result = Primitives.primitiveValueOrNullFor(null);
        Assert.assertNull(result);
    }
    
    // 
    
    // All the above test cases
    
    // 
}