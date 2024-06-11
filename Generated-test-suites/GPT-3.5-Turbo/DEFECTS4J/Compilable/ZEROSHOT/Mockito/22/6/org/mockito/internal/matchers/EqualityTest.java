package org.mockito.internal.matchers;
import static org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_NullValues_ReturnsTrue() {
        boolean result = Equality.areEqual(null, null);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_OneNullValue_ReturnsFalse() {
        boolean result = Equality.areEqual(null, new Object());
        assertFalse(result);
    }

    @Test
    public void testAreEqual_TwoArraysWithDifferentLengths_ReturnsFalse() {
        Object[] array1 = new Object[3];
        Object[] array2 = new Object[2];

        boolean result = Equality.areEqual(array1, array2);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_TwoArraysWithSameLengthsButDifferentElements_ReturnsFalse() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{4, 5, 6};

        boolean result = Equality.areEqual(array1, array2);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_TwoArraysWithSameLengthsAndSameElements_ReturnsTrue() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2, 3};

        boolean result = Equality.areEqual(array1, array2);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_TwoEqualObjects_ReturnsTrue() {
        Object object1 = new Object();
        Object object2 = object1;

        boolean result = Equality.areEqual(object1, object2);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_TwoDifferentObjects_ReturnsFalse() {
        Object object1 = new Object();
        Object object2 = new Object();

        boolean result = Equality.areEqual(object1, object2);
        assertFalse(result);
    }
}