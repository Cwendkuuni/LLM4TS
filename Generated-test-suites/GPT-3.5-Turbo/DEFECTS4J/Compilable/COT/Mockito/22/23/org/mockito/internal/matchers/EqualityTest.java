package org.mockito.internal.matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {
    @Test
    public void testAreEqual_nullObjects_returnTrue() {
        boolean result = Equality.areEqual(null, null);
        Assert.assertTrue(result);
    }

    @Test
    public void testAreEqual_oneObjectNull_returnFalse() {
        boolean result = Equality.areEqual(null, "test");
        Assert.assertFalse(result);
    }

    @Test
    public void testAreEqual_objectsEqual_returnTrue() {
        boolean result = Equality.areEqual("test", "test");
        Assert.assertTrue(result);
    }

    @Test
    public void testAreEqual_objectsNotEqual_returnFalse() {
        boolean result = Equality.areEqual("test1", "test2");
        Assert.assertFalse(result);
    }

    @Test
    public void testAreEqual_intArraysEqual_returnTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        boolean result = Equality.areEqual(arr1, arr2);
        Assert.assertTrue(result);
    }

    @Test
    public void testAreEqual_intArraysNotEqual_returnFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        boolean result = Equality.areEqual(arr1, arr2);
        Assert.assertFalse(result);
    }

    @Test
    public void testAreEqual_objectArraysEqual_returnTrue() {
        Object[] arr1 = {"test1", "test2", "test3"};
        Object[] arr2 = {"test1", "test2", "test3"};
        boolean result = Equality.areEqual(arr1, arr2);
        Assert.assertTrue(result);
    }

    @Test
    public void testAreEqual_objectArraysNotEqual_returnFalse() {
        Object[] arr1 = {"test1", "test2", "test3"};
        Object[] arr2 = {"test1", "test2", "test4"};
        boolean result = Equality.areEqual(arr1, arr2);
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAreEqual_invalidArrayArgument_throwIllegalArgumentException() {
        int[] arr = {1, 2, 3};
        Equality.areEqual(arr, "test");
    }
}