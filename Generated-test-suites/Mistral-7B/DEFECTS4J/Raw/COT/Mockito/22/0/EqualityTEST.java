package org.mockito.internal.matchers.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

import java.util.Arrays;

public class EqualityTest {

    @Test
    public void testAreEqual_nullWithNull() {
        Assert.assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_nullWithNonnull() {
        Assert.assertFalse(Equality.areEqual(null, new Object[]{1, 2, 3}));
    }

    @Test
    public void testAreEqual_nonnullWithNull() {
        Assert.assertFalse(Equality.areEqual(new Object[]{1, 2, 3}, null));
    }

    @Test
    public void testAreEqual_nonnullWithNonnull() {
        Object obj1 = new Object[]{1, 2, 3};
        Object obj2 = new Object[]{1, 2, 3};
        Assert.assertTrue(Equality.areEqual(obj1, obj2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAreEqual_objectsOfDifferentTypes() {
        Equality.areEqual("abc", new Object[]{1, 2, 3});
    }

    @Test
    public void testAreArraysEqual_equalArrays() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3};
        Assert.assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_arraysOfDifferentLengths() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3, 4};
        Assert.assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_arraysWithSameLengthButDifferentElements() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{2, 2, 3};
        Assert.assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_arraysWithSameElementsButInDifferentOrder() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{3, 2, 1};
        Assert.assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_arraysWithSameElementsInSameOrder() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3};
        Assert.assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testIsArray_isArray() {
        Object[] arr = new Object[]{1, 2, 3};
        Assert.assertTrue(Equality.isArray(arr));
    }

    @Test
    public void testIsArray_nonArray() {
        String str = "abc";
        Assert.assertFalse(Equality.isArray(str));
    }
}