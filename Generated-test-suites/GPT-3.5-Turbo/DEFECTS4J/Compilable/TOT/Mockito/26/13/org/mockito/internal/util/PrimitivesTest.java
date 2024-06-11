package org.mockito.internal.util;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf_primitiveClass_returnsClass() {
        // Arrange
        Class<Integer> clazz = Integer.TYPE;

        // Act
        Class<Integer> result = Primitives.primitiveTypeOf(clazz);

        // Assert
        Assert.assertEquals(clazz, result);
    }

    @Test
    public void testPrimitiveTypeOf_wrapperClass_returnsPrimitiveClass() {
        // Arrange
        Class<Integer> clazz = Integer.class;

        // Act
        Class<Integer> result = Primitives.primitiveTypeOf(clazz);

        // Assert
        Assert.assertEquals(Integer.TYPE, result);
    }

    @Test
    public void testPrimitiveTypeOf_unknownClass_returnsNull() {
        // Arrange
        Class<String> clazz = String.class;

        // Act
        Class<String> result = Primitives.primitiveTypeOf(clazz);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testIsPrimitiveWrapper_primitiveWrapperClass_returnsTrue() {
        // Arrange
        Class<Boolean> type = Boolean.class;

        // Act
        boolean result = Primitives.isPrimitiveWrapper(type);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsPrimitiveWrapper_primitiveClass_returnsFalse() {
        // Arrange
        Class<Boolean> type = Boolean.TYPE;

        // Act
        boolean result = Primitives.isPrimitiveWrapper(type);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testPrimitiveWrapperOf_primitiveWrapperClass_returnsWrapperInstance() {
        // Arrange
        Class<Integer> type = Integer.class;
        Integer expected = 0;

        // Act
        Integer result = Primitives.primitiveWrapperOf(type);

        // Assert
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPrimitiveWrapperOf_primitiveClass_returnsNull() {
        // Arrange
        Class<Integer> type = Integer.TYPE;

        // Act
        Integer result = Primitives.primitiveWrapperOf(type);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_primitiveWrapperClass_returnsWrapperValue() {
        // Arrange
        Class<Double> primitiveType = Double.TYPE;
        Double expected = 0.0;

        // Act
        Double result = Primitives.primitiveValueOrNullFor(primitiveType);

        // Assert
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_primitiveClass_returnsNull() {
        // Arrange
        Class<Double> primitiveType = Double.class;

        // Act
        Double result = Primitives.primitiveValueOrNullFor(primitiveType);

        // Assert
        Assert.assertNull(result);
    }
}