
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

import java.lang.reflect.Array;

import static org.junit.Assert.*;

public class EqualityTest {

    // Test cases for areEqual method

    @Test
    public void testAreEqual_withEqualObjects_shouldReturnTrue() {
        assertTrue(Equality.areEqual("hello", "hello"));
    }

    @Test
    public void testAreEqual_withNullAndNull_shouldReturnTrue() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_withNullAndObject_shouldReturnFalse() {
        assertFalse(Equality.areEqual(null, "hello"));
    }

    @Test
    public void testAreEqual_withObjectAndNull_shouldReturnFalse() {
        assertFalse(Equality.areEqual("hello", null));
    }

    @Test
    public void testAreEqual_withDifferentObjects_shouldReturnFalse() {
        assertFalse(Equality.areEqual("hello", "world"));
    }

    // Test cases for areArraysEqual method

    @Test
    public void testAreArraysEqual_withEqualArrays_shouldReturnTrue() {
        Object arr1 = new int[]{1, 2, 3};
        Object arr2 = new int[]{1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_withDifferentArrayLengths_shouldReturnFalse() {
        Object arr1 = new int[]{1, 2, 3};
        Object arr2 = new int[]{1, 2};
        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_withDifferentArrayElements_shouldReturnFalse() {
        Object arr1 = new int[]{1, 2, 3};
        Object arr2 = new int[]{1, 4, 3};
        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    // Test cases for areArrayLengthsEqual method

    @Test
    public void testAreArrayLengthsEqual_withEqualArrays_shouldReturnTrue() {
        Object arr1 = new int[]{1, 2, 3};
        Object arr2 = new int[]{1, 2, 3};
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual_withDifferentArrayLengths_shouldReturnFalse() {
        Object arr1 = new int[]{1, 2, 3};
        Object arr2 = new int[]{1, 2};
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    // Test cases for areArrayElementsEqual method

    @Test
    public void testAreArrayElementsEqual_withEqualArrays_shouldReturnTrue() {
        Object arr1 = new int[]{1, 2, 3};
        Object arr2 = new int[]{1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_withDifferentArrayElements_shouldReturnFalse() {
        Object arr1 = new int[]{1, 2, 3};
        Object arr2 = new int[]{1, 4, 3};
        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    // Test cases for isArray method

    @Test
    public void testIsArray_withArray_shouldReturnTrue() {
        Object arr = new int[]{1, 2, 3};
        assertTrue(Equality.isArray(arr));
    }

    @Test
    public void testIsArray_withNonArrayObject_shouldReturnFalse() {
        assertFalse(Equality.isArray("hello"));
    }
    
    @Test
    public void testIsArray_withNull_shouldReturnFalse() {
        assertFalse(Equality.isArray(null));
    }
}