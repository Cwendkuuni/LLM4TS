import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {
    // Method: boolean areEqual(Object o1, Object o2)
    @Test
    public void testAreEqual_NullInputs_ReturnsTrue() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_OneNullInput_ReturnsFalse() {
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_NonArrayInputs_ReturnsCorrectEquality() {
        assertTrue(Equality.areEqual("hello", "hello"));
        assertFalse(Equality.areEqual("hello", "world"));       
    }

    @Test
    public void testAreEqual_ArrayInputs_ReturnsCorrectEquality() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        int[] array3 = {1, 2, 4};
        assertTrue(Equality.areEqual(array1, array2));
        assertFalse(Equality.areEqual(array1, array3));
    }

    // Method: boolean areArraysEqual(Object o1, Object o2)
    @Test
    public void testAreArraysEqual_ArraysOfDifferentLengths_ReturnsFalse() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2};
        assertFalse(Equality.areArraysEqual(array1, array2));
    }

    @Test
    public void testAreArraysEqual_ArraysOfSameLength_ReturnsCorrectEquality() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        int[] array3 = {1, 2, 4};
        assertTrue(Equality.areArraysEqual(array1, array2));
        assertFalse(Equality.areArraysEqual(array1, array3));
    }

    // Method: boolean areArrayLengthsEqual(Object o1, Object o2)
    @Test
    public void testAreArrayLengthsEqual_ArraysOfDifferentLengths_ReturnsFalse() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2};
        assertFalse(Equality.areArrayLengthsEqual(array1, array2));
    }

    @Test
    public void testAreArrayLengthsEqual_ArraysOfSameLength_ReturnsTrue() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));
    }

    // Method: boolean areArrayElementsEqual(Object o1, Object o2)
    @Test
    public void testAreArrayElementsEqual_ArraysWithDifferentElements_ReturnsFalse() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 4};
        assertFalse(Equality.areArrayElementsEqual(array1, array2));
    }

    @Test
    public void testAreArrayElementsEqual_ArraysWithSameElements_ReturnsTrue() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(array1, array2));
    }

    // Method: boolean isArray(Object o)
    @Test
    public void testIsArray_ObjectIsArray_ReturnsTrue() {
        int[] array = {1, 2, 3};
        assertTrue(Equality.isArray(array));
    }

    @Test
    public void testIsArray_ObjectIsNotArray_ReturnsFalse() {
        assertFalse(Equality.isArray("hello"));
    }
}