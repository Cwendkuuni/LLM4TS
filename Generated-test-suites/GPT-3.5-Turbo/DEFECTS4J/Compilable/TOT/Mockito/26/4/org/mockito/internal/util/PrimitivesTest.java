package org.mockito.internal.util;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf_WithPrimitiveType_ReturnsSameType() {
        Class<Integer> expectedType = Integer.TYPE;
        Class<Integer> resultType = Primitives.primitiveTypeOf(Integer.TYPE);
        
        Assert.assertEquals(expectedType, resultType);
    }

    @Test
    public void testPrimitiveTypeOf_WithWrapperType_ReturnsPrimitiveType() {
        Class<Double> expectedType = Double.TYPE;
        Class<Double> resultType = Primitives.primitiveTypeOf(Double.class);
        
        Assert.assertEquals(expectedType, resultType);
    }

    @Test
    public void testPrimitiveTypeOf_WithNonPrimitiveType_ReturnsNull() {
        Class<String> expectedType = null;
        Class<String> resultType = Primitives.primitiveTypeOf(String.class);
        
        Assert.assertEquals(expectedType, resultType);
    }

    @Test
    public void testIsPrimitiveWrapper_WithPrimitiveWrapperType_ReturnsTrue() {
        boolean expectedResult = true;
        boolean result = Primitives.isPrimitiveWrapper(Byte.class);
        
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testIsPrimitiveWrapper_WithNonPrimitiveWrapperType_ReturnsFalse() {
        boolean expectedResult = false;
        boolean result = Primitives.isPrimitiveWrapper(String.class);
        
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testPrimitiveWrapperOf_WithPrimitiveWrapperType_ReturnsInstanceOfWrapperType() {
        Double expectedValue = 0.0;
        Double result = Primitives.primitiveWrapperOf(Double.class);
        
        Assert.assertEquals(expectedValue, result);
    }

    @Test
    public void testPrimitiveWrapperOf_WithNonPrimitiveWrapperType_ReturnsNull() {
        String expectedValue = null;
        String result = Primitives.primitiveWrapperOf(String.class);
        
        Assert.assertEquals(expectedValue, result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_WithPrimitiveType_ReturnsPrimitiveValue() {
        int expectedValue = 0;
        int result = Primitives.primitiveValueOrNullFor(int.class);
        
        Assert.assertEquals(expectedValue, result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_WithNonPrimitiveType_ReturnsNull() {
        String expectedValue = null;
        String result = Primitives.primitiveValueOrNullFor(String.class);
        
        Assert.assertEquals(expectedValue, result);
    }
}