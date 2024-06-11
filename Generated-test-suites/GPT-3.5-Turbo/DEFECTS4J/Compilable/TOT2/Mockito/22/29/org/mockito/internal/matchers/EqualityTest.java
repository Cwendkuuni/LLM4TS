package org.mockito.internal.matchers;
import static org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_NullObjects_ReturnsTrue() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_SameObject_ReturnsTrue() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }

    @Test
    public void testAreEqual_DifferentObjects_ReturnsFalse() {
        assertFalse(Equality.areEqual(new Object(), new Object()));
    }

    @Test
    public void testAreArraysEqual_SameArray_ReturnsTrue() {
        Object[] array = new Object[] {1, 2, 3};
        assertTrue(Equality.areArraysEqual(array, array));
    }

    @Test
    public void testAreArraysEqual_DifferentArray_ReturnsFalse() {
        Object[] array1 = new Object[] {1, 2, 3};
        Object[] array2 = new Object[] {1, 2, 4};
        assertFalse(Equality.areArraysEqual(array1, array2));
    }

    @Test
    public void testAreArrayLengthsEqual_SameArray_ReturnsTrue() {
        Object[] array1 = new Object[] {1, 2, 3};
        Object[] array2 = new Object[] {4, 5, 6};
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqual_DifferentArrayElement_ReturnsFalse() {
        Object[] array1 = new Object[] {1, 2, 3};
        Object[] array2 = new Object[] {1, 4, 3};
        assertFalse(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testIsArray_ArrayObject_ReturnsTrue() {
        assertTrue(Equality.isArray(new Object[] {}));
    }

    @Test
    public void testIsArray_NonArrayObject_ReturnsFalse() {
        assertFalse(Equality.isArray(new Object()));
    }

}