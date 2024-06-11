package org.mockito.internal.matchers;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

    @Test
    public void testAreEqual_typicalCase() {
        Object o1 = new Object();
        Object o2 = new Object();
        assertFalse(Equality.areEqual(o1, o2));
    }

    @Test
    public void testAreEqual_nullCheck() {
        Object o1 = null;
        Object o2 = null;
        assertTrue(Equality.areEqual(o1, o2));
    }

    @Test
    public void testAreEqual_nullObjectCheck() {
        Object o1 = new Object();
        Object o2 = null;
        assertFalse(Equality.areEqual(o1, o2));
    }

    @Test
    public void testAreEqual_arrayCheck() {
        int[] o1 = new int[]{1, 2, 3};
        int[] o2 = new int[]{1, 2, 3};
        assertTrue(Equality.areEqual(o1, o2));
    }

    @Test
    public void testAreEqual_arrayLengthMismatch() {
        int[] o1 = new int[]{1, 2, 3};
        int[] o2 = new int[]{1, 2, 3, 4};
        assertFalse(Equality.areEqual(o1, o2));
    }

    @Test
    public void testAreEqual_arrayElementMismatch() {
        int[] o1 = new int[]{1, 2, 3};
        int[] o2 = new int[]{1, 4, 3};
        assertFalse(Equality.areEqual(o1, o2));
    }

    @Test
    public void testAreArraysEqual_typicalCase() {
        int[] o1 = new int[]{1, 2, 3};
        int[] o2 = new int[]{1, 2, 3};
        assertTrue(Equality.areArraysEqual(o1, o2));
    }

    @Test
    public void testAreArraysEqual_lengthMismatch() {
        int[] o1 = new int[]{1, 2, 3};
        int[] o2 = new int[]{1, 2, 3, 4};
        assertFalse(Equality.areArraysEqual(o1, o2));
    }

    @Test
    public void testAreArrayLengthsEqual_typicalCase() {
        int[] o1 = new int[]{1, 2, 3};
        int[] o2 = new int[]{4, 5, 6};
        assertTrue(Equality.areArrayLengthsEqual(o1, o2));
    }

    @Test
    public void testAreArrayLengthsEqual_lengthMismatch() {
        int[] o1 = new int[]{1, 2, 3};
        int[] o2 = new int[]{1, 2, 3, 4};
        assertFalse(Equality.areArrayLengthsEqual(o1, o2));
    }

    @Test
    public void testAreArrayElementsEqual_typicalCase() {
        int[] o1 = new int[]{1, 2, 3};
        int[] o2 = new int[]{1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(o1, o2));
    }

    @Test
    public void testAreArrayElementsEqual_elementMismatch() {
        int[] o1 = new int[]{1, 2, 3};
        int[] o2 = new int[]{1, 4, 3};
        assertFalse(Equality.areArrayElementsEqual(o1, o2));
    }

    @Test
    public void testIsArray_typicalCase() {
        int[] o = new int[]{1, 2, 3};
        assertTrue(Equality.isArray(o));
    }

    @Test
    public void testIsArray_nonArrayObject() {
        Object o = new Object();
        assertFalse(Equality.isArray(o));
    }
}