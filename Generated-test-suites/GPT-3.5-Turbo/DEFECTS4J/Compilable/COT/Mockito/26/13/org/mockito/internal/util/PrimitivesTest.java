package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf_PrimitiveClass_ReturnsSameClass() {
        Class<Boolean> clazz = Boolean.TYPE;
        Class<Boolean> result = Primitives.primitiveTypeOf(clazz);
        Assert.assertEquals(clazz, result);
    }

    @Test
    public void testPrimitiveTypeOf_WrapperClass_ReturnsPrimitiveClass() {
        Class<Boolean> clazz = Boolean.class;
        Class<Boolean> result = Primitives.primitiveTypeOf(clazz);
        Assert.assertEquals(Boolean.TYPE, result);
    }

    @Test
    public void testIsPrimitiveWrapper_PrimitiveWrapperClass_ReturnsTrue() {
        boolean result = Primitives.isPrimitiveWrapper(Boolean.class);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsPrimitiveWrapper_NonPrimitiveWrapperClass_ReturnsFalse() {
        boolean result = Primitives.isPrimitiveWrapper(String.class);
        Assert.assertFalse(result);
    }

    @Test
    public void testPrimitiveWrapperOf_PrimitiveWrapperClass_ReturnsWrapperInstance() {
        Boolean result = Primitives.primitiveWrapperOf(Boolean.class);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testPrimitiveWrapperOf_NonPrimitiveWrapperClass_ReturnsNull() {
        Object result = Primitives.primitiveWrapperOf(String.class);
        Assert.assertNull(result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_PrimitiveType_ReturnsPrimitiveValue() {
        boolean result = Primitives.primitiveValueOrNullFor(boolean.class);
        Assert.assertFalse(result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_NonPrimitiveType_ReturnsNull() {
        Object result = Primitives.primitiveValueOrNullFor(String.class);
        Assert.assertNull(result);
    }

}
