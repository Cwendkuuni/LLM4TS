import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_BothNull_ReturnsTrue() {
        boolean result = Equality.areEqual(null, null);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_OneNullAndOneNotNull_ReturnsFalse() {
        boolean result = Equality.areEqual(null, new Object());
        assertFalse(result);
    }

    @Test
    public void testAreEqual_OneNotNullAndOneNull_ReturnsFalse() {
        boolean result = Equality.areEqual(new Object(), null);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_BothSameObject_ReturnsTrue() {
        Object obj = new Object();
        boolean result = Equality.areEqual(obj, obj);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_BothDifferentObjects_ReturnsFalse() {
        boolean result = Equality.areEqual(new Object(), new Object());
        assertFalse(result);
    }

    @Test
    public void testAreEqual_BothArraysSameLengthAndElementsEqual_ReturnsTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        boolean result = Equality.areEqual(arr1, arr2);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_BothArraysSameLengthButElementsDifferent_ReturnsFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {3, 2, 1};
        boolean result = Equality.areEqual(arr1, arr2);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_BothArraysDifferentLength_ReturnsFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        boolean result = Equality.areEqual(arr1, arr2);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_BothObjectsImplementEqualsMethodAndEqual_ReturnsTrue() {
        String str1 = "Hello";
        String str2 = "Hello";
        boolean result = Equality.areEqual(str1, str2);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_BothObjectsImplementEqualsMethodButNotEqual_ReturnsFalse() {
        String str1 = "Hello";
        String str2 = "World";
        boolean result = Equality.areEqual(str1, str2);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_BothObjectsDoNotImplementEqualsMethod_ReturnsFalse() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        boolean result = Equality.areEqual(obj1, obj2);
        assertFalse(result);
    }

    @Test
    public void testAreArraysEqual_BothArraysSameLengthAndElementsEqual_ReturnsTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        boolean result = Equality.areArraysEqual(arr1, arr2);
        assertTrue(result);
    }

    @Test
    public void testAreArraysEqual_BothArraysSameLengthButElementsDifferent_ReturnsFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {3, 2, 1};
        boolean result = Equality.areArraysEqual(arr1, arr2);
        assertFalse(result);
    }

    @Test
    public void testAreArrayLengthsEqual_BothArraysSameLength_ReturnsTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        boolean result = Equality.areArrayLengthsEqual(arr1, arr2);
        assertTrue(result);
    }

    @Test
    public void testAreArrayLengthsEqual_BothArraysDifferentLength_ReturnsFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        boolean result = Equality.areArrayLengthsEqual(arr1, arr2);
        assertFalse(result);
    }

    @Test
    public void testAreArrayElementsEqual_BothArraysElementsEqual_ReturnsTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        boolean result = Equality.areArrayElementsEqual(arr1, arr2);
        assertTrue(result);
    }

    @Test
    public void testAreArrayElementsEqual_BothArraysElementsDifferent_ReturnsFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {3, 2, 1};
        boolean result = Equality.areArrayElementsEqual(arr1, arr2);
        assertFalse(result);
    }

    @Test
    public void testIsArray_ObjectIsArray_ReturnsTrue() {
        int[] arr = {1, 2, 3};
        boolean result = Equality.isArray(arr);
        assertTrue(result);
    }

    @Test
    public void testIsArray_ObjectIsNotArray_ReturnsFalse() {
        Object obj = new Object();
        boolean result = Equality.isArray(obj);
        assertFalse(result);
    }
}