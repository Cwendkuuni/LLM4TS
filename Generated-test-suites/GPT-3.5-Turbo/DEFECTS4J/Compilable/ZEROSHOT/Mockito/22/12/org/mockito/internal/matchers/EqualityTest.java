package org.mockito.internal.matchers;
import org.junit.Assert;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_BothNull_ReturnsTrue() {
        boolean result = Equality.areEqual(null, null);
        Assert.assertTrue(result);
    }

    @Test
    public void testAreEqual_OneNull_ReturnsFalse() {
        boolean result = Equality.areEqual(null, new Object());
        Assert.assertFalse(result);

        result = Equality.areEqual(new Object(), null);
        Assert.assertFalse(result);
    }

    @Test
    public void testAreEqual_BothNotNull_ObjectEquals_ReturnsTrue() {
        boolean result = Equality.areEqual(new Object(), new Object());
        Assert.assertTrue(result);
    }

    @Test
    public void testAreEqual_BothNotNull_ObjectNotEquals_ReturnsFalse() {
        boolean result = Equality.areEqual(new Object(), new Integer(5));
        Assert.assertFalse(result);
    }

    @Test
    public void testAreEqual_BothArrays_ArraysEqual_ReturnsTrue() {
        boolean result = Equality.areEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3});
        Assert.assertTrue(result);
    }

    @Test
    public void testAreEqual_BothArrays_ArraysNotEqual_ReturnsFalse() {
        boolean result = Equality.areEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4});
        Assert.assertFalse(result);
    }

    @Test
    public void testAreArraysEqual_ArrayLengthsEqual_ReturnsTrue() {
        boolean result = Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{4, 5, 6});
        Assert.assertTrue(result);
    }

    @Test
    public void testAreArraysEqual_ArrayLengthsNotEqual_ReturnsFalse() {
        boolean result = Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{4, 5});
        Assert.assertFalse(result);
    }

    @Test
    public void testAreArrayElementsEqual_ArrayElementsEqual_ReturnsTrue() {
        boolean result = Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3});
        Assert.assertTrue(result);
    }

    @Test
    public void testAreArrayElementsEqual_ArrayElementsNotEqual_ReturnsFalse() {
        boolean result = Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{4, 5, 6});
        Assert.assertFalse(result);
    }

    @Test
    public void testIsArray_IsArray_ReturnsTrue() {
        boolean result = Equality.isArray(new int[]{});
        Assert.assertTrue(result);
    }

    @Test
    public void testIsArray_IsNotArray_ReturnsFalse() {
        boolean result = Equality.isArray(new Object());
        Assert.assertFalse(result);
    }
}