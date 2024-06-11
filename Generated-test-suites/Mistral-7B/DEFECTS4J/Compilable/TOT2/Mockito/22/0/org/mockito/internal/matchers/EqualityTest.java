package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EqualityTest {
    @Test
    public void testAreEqualNullWithNull() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqualNullWithObject() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(null, obj));
        assertTrue(Equality.areEqual(obj, null));
        assertFalse(Equality.areEqual(null, new Object[]{1}));
    }

    @Test
    public void testAreEqualSameObjects() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }

    @Test
    public void testAreEqualDifferentObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreArraysEqualSameArrays() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArraysEqual(array1, array2));
    }

    @Test
    public void testAreArraysEqualDifferentLengths() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2};
        assertFalse(Equality.areArraysEqual(array1, array2));
    }

    @Test
    public void testAreArraysEqualDifferentTypes() {
        Object[] array1 = new Object[]{1, 2, 3};
        String[] array2 = new String[]{"1", "2", "3"};
        assertFalse(Equality.areArraysEqual(array1, array2));
    }

    @Test
    public void testAreArrayLengthsEqualSameLengths() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayLengthsEqualDifferentLengths() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2};
        assertFalse(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqualSameElements() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqualDifferentElements() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{2, 3, 4};
        assertFalse(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testIsArrayTrue() {
        Object[] array = new Object[]{1, 2, 3};
        assertTrue(Equality.isArray(array));
    }

    @Test
    public void testIsArrayFalse() {
        Object obj = new Object();
        assertFalse(Equality.isArray(obj));
    }
}