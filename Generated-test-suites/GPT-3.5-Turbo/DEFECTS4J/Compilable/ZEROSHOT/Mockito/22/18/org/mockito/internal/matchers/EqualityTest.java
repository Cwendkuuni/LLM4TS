package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_NullInputs() {
        boolean result = Equality.areEqual(null, null);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_OneNullInput() {
        boolean result = Equality.areEqual(null, new Object());
        assertFalse(result);
    }

    @Test
    public void testAreEqual_ArraysNotEqual() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 2, 4};
        boolean result = Equality.areEqual(array1, array2);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_ArraysEqual() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 2, 3};
        boolean result = Equality.areEqual(array1, array2);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_NotArrays() {
        boolean result = Equality.areEqual("hello", "world");
        assertFalse(result);
    }

    @Test
    public void testAreArrayLengthsEqual_EqualLength() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {4, 5, 6};
        boolean result = Equality.areArrayLengthsEqual(array1, array2);
        assertTrue(result);
    }

    @Test
    public void testAreArrayLengthsEqual_NotEqualLength() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {4, 5};
        boolean result = Equality.areArrayLengthsEqual(array1, array2);
        assertFalse(result);
    }

    @Test
    public void testAreArrayElementsEqual_EqualElements() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 2, 3};
        boolean result = Equality.areArrayElementsEqual(array1, array2);
        assertTrue(result);
    }

    @Test
    public void testAreArrayElementsEqual_NotEqualElements() {
        Object[] array1 = {1, 2, 3};
        Object[] array2 = {4, 5, 6};
        boolean result = Equality.areArrayElementsEqual(array1, array2);
        assertFalse(result);
    }

    @Test
    public void testIsArray_Array() {
        Object[] array = {1, 2, 3};
        boolean result = Equality.isArray(array);
        assertTrue(result);
    }

    @Test
    public void testIsArray_NotArray() {
        boolean result = Equality.isArray("hello");
        assertFalse(result);
    }
}