package org.mockito.internal.util;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    // Method: primitiveTypeOf(Class<T> clazz)
    @Test
    public void testPrimitiveTypeOf_PrimitiveType_ReturnsSameClass() {
        Assert.assertSame(boolean.class, Primitives.primitiveTypeOf(boolean.class));
    }

    @Test
    public void testPrimitiveTypeOf_WrapperType_ReturnsPrimitiveType() {
        Assert.assertSame(int.class, Primitives.primitiveTypeOf(Integer.class));
    }

    // Method: isPrimitiveWrapper(Class<?> type)
    @Test
    public void testIsPrimitiveWrapper_PrimitiveType_ReturnsFalse() {
        Assert.assertFalse(Primitives.isPrimitiveWrapper(boolean.class));
    }

    @Test
    public void testIsPrimitiveWrapper_WrapperType_ReturnsTrue() {
        Assert.assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
    }

    // Method: primitiveWrapperOf(Class<T> type)
    @Test
    public void testPrimitiveWrapperOf_WrapperType_ReturnsWrapperInstance() {
        Assert.assertEquals(new Integer(0), Primitives.primitiveWrapperOf(int.class));
    }

    // Method: primitiveValueOrNullFor(Class<T> primitiveType)
    @Test
    public void testPrimitiveValueOrNullFor_PrimitiveType_ReturnsDefaultValue() {
        Assert.assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor_WrapperType_ReturnsNull() {
        Assert.assertNull(Primitives.primitiveValueOrNullFor(Integer.class));
    }

    // Make sure all methods are listed above to avoid confusion

    

    // Edge cases and exception handling scenarios tests

    // Method: primitiveTypeOf(Class<T> clazz)
    @Test
    public void testPrimitiveTypeOf_NullClass_ThrowsException() {
        try {
            Primitives.primitiveTypeOf(null);
            Assert.fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Method: isPrimitiveWrapper(Class<?> type)
    @Test
    public void testIsPrimitiveWrapper_NullClass_ReturnsFalse() {
        Assert.assertFalse(Primitives.isPrimitiveWrapper(null));
    }

    // Method: primitiveWrapperOf(Class<T> type)
    @Test
    public void testPrimitiveWrapperOf_NullClass_ReturnsNull() {
        Assert.assertNull(Primitives.primitiveWrapperOf(null));
    }

    // Method: primitiveValueOrNullFor(Class<T> primitiveType)
    @Test
    public void testPrimitiveValueOrNullFor_NullClass_ReturnNull() {
        Assert.assertNull(Primitives.primitiveValueOrNullFor(null));
    }

    
}