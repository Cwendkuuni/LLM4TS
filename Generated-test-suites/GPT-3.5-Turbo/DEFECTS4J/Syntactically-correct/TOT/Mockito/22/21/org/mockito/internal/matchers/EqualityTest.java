import org.junit.Test;
import org.mockito.internal.matchers.Equality;

import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_BothNull_ReturnsTrue() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_OneNull_ReturnsFalse() {
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqual_BothArraysEqual_ReturnsTrue() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_BothArraysDifferent_ReturnsFalse() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 4};
        assertFalse(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_DifferentObjects_ReturnsFalse() {
        assertFalse(Equality.areEqual("hello", new Object()));
    }

    @Test
    public void testAreEqual_EqualObjects_ReturnsTrue() {
        assertTrue(Equality.areEqual("hello", "hello"));
    }

    @Test
    public void testAreArrayLengthsEqual_ArraysSameLength_ReturnsTrue() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayLengthsEqual_ArraysDifferentLength_ReturnsFalse() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2};
        assertFalse(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqual_SameElementsInArrays_ReturnsTrue() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqual_DifferentElementsInArrays_ReturnsFalse() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 4};
        assertFalse(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testIsArray_InputIsNull_ReturnsFalse() {
        assertFalse(Equality.isArray(null));
    }

    @Test
    public void testIsArray_InputIsArray_ReturnsTrue() {
        int[] array = {1, 2, 3};
        assertTrue(Equality.isArray(array));
    }

    @Test
    public void testIsArray_InputIsNotArray_ReturnsFalse() {
        assertFalse(Equality.isArray("hello"));
    }
}