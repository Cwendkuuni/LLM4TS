import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_whenBothInputsAreNull_shouldReturnTrue() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_whenOneInputIsNullAndTheOtherIsNotNull_shouldReturnFalse() {
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_whenBothInputsAreArraysAndEqual_shouldReturnTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_whenBothInputsAreArraysAndNotEqual_shouldReturnFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_whenOneInputIsArrayAndTheOtherIsNot_shouldReturnFalse() {
        int[] arr = {1, 2, 3};
        assertFalse(Equality.areEqual(arr, new Object()));
        assertFalse(Equality.areEqual(new Object(), arr));
    }

    @Test
    public void testAreEqual_whenBothInputsAreObjectsAndEqual_shouldReturnTrue() {
        String text1 = "Hello";
        String text2 = new String("Hello");
        assertTrue(Equality.areEqual(text1, text2));
    }

    @Test
    public void testAreEqual_whenBothInputsAreObjectsAndNotEqual_shouldReturnFalse() {
        String text1 = "Hello";
        String text2 = "World";
        assertFalse(Equality.areEqual(text1, text2));
    }

    @Test
    public void testAreEqual_whenOneInputIsObjectAndTheOtherIsNot_shouldReturnFalse() {
        String text = "Hello";
        assertFalse(Equality.areEqual(text, new Object()));
        assertFalse(Equality.areEqual(new Object(), text));
    }

    @Test
    public void testAreArrayLengthsEqual_whenBothInputArraysAreEmpty_shouldReturnTrue() {
        int[] arr1 = new int[0];
        int[] arr2 = new int[0];
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual_whenOneInputArrayIsEmptyAndTheOtherIsNotEmpty_shouldReturnFalse() {
        int[] arr1 = new int[0];
        int[] arr2 = {1, 2, 3};
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
        assertFalse(Equality.areArrayLengthsEqual(arr2, arr1));
    }

    @Test
    public void testAreArrayLengthsEqual_whenBothInputArraysHaveDifferentLengths_shouldReturnFalse() {
        int[] arr1 = {1, 2};
        int[] arr2 = {1, 2, 3};
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_whenBothInputArraysAreEmpty_shouldReturnTrue() {
        int[] arr1 = new int[0];
        int[] arr2 = new int[0];
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_whenBothInputArraysHaveSameElements_shouldReturnTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_whenBothInputArraysHaveDifferentElements_shouldReturnFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_whenOneInputArrayIsSubsetOfTheOther_shouldReturnFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
        assertFalse(Equality.areArrayElementsEqual(arr2, arr1));
    }

    @Test
    public void testIsArray_whenInputIsArray_shouldReturnTrue() {
        int[] arr = {1, 2, 3};
        assertTrue(Equality.isArray(arr));
    }

    @Test
    public void testIsArray_whenInputIsNotArray_shouldReturnFalse() {
        Object obj = new Object();
        assertFalse(Equality.isArray(obj));
    }
}