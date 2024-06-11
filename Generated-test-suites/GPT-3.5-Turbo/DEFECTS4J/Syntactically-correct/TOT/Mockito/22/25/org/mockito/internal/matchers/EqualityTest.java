package org.mockito.internal.matchers;

import org.junit.Assert;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_NullObjects_ReturnsTrue() {
        // Arrange
        Object o1 = null;
        Object o2 = null;

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testAreEqual_NullAndNonNullObjects_ReturnsFalse() {
        // Arrange
        Object o1 = null;
        Object o2 = new Object();

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testAreEqual_NonNullAndNullObjects_ReturnsFalse() {
        // Arrange
        Object o1 = new Object();
        Object o2 = null;

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testAreEqual_SameNonNullObjects_ReturnsTrue() {
        // Arrange
        Object o1 = new Object();
        Object o2 = o1;

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testAreEqual_DifferentTypes_ReturnsFalse() {
        // Arrange
        Object o1 = new Integer(10);
        Object o2 = new String("10");

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testAreEqual_SameArrayObjects_ReturnsTrue() {
        // Arrange
        int[] arr1 = {1, 2, 3};
        int[] arr2 = arr1;

        // Act
        boolean result = Equality.areEqual(arr1, arr2);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testAreEqual_DifferentArrayObjects_ReturnsFalse() {
        // Arrange
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};

        // Act
        boolean result = Equality.areEqual(arr1, arr2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testAreEqual_ArraysWithDifferentLengths_ReturnsFalse() {
        // Arrange
        int[] arr1 = {1, 2, 3, 4};
        int[] arr2 = {1, 2, 3};

        // Act
        boolean result = Equality.areEqual(arr1, arr2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testAreEqual_ArraysWithDifferentElements_ReturnsFalse() {
        // Arrange
        int[] arr1 = {1, 2, 3, 4};
        int[] arr2 = {1, 2, 5, 6};

        // Act
        boolean result = Equality.areEqual(arr1, arr2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testAreEqual_NestedArrays_ReturnsTrue() {
        // Arrange
        int[][] arr1 = {{1, 2}, {3, 4}};
        int[][] arr2 = {{1, 2}, {3, 4}};

        // Act
        boolean result = Equality.areEqual(arr1, arr2);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testAreEqual_NestedArrays_DifferentElements_ReturnsFalse() {
        // Arrange
        int[][] arr1 = {{1, 2}, {3, 4}};
        int[][] arr2 = {{1, 2}, {5, 6}};

        // Act
        boolean result = Equality.areEqual(arr1, arr2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testAreEqual_NestedArrays_DifferentLengths_ReturnsFalse() {
        // Arrange
        int[][] arr1 = {{1, 2}, {3, 4, 5}};
        int[][] arr2 = {{1, 2}, {3, 4}};

        // Act
        boolean result = Equality.areEqual(arr1, arr2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testAreEqual_EmptyArrays_ReturnsTrue() {
        // Arrange
        int[] arr1 = {};
        int[] arr2 = {};

        // Act
        boolean result = Equality.areEqual(arr1, arr2);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testAreEqual_DifferentArrayTypes_ReturnsFalse() {
        // Arrange
        int[] arr = {1, 2, 3};
        Object obj = new Object();

        // Act
        boolean result = Equality.areEqual(arr, obj);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testAreEqual_ArrayAndNull_ReturnsFalse() {
        // Arrange
        int[] arr = {1, 2, 3};
        Object obj = null;

        // Act
        boolean result = Equality.areEqual(arr, obj);

        // Assert
        Assert.assertFalse(result);
    }
}