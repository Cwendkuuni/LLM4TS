import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_nullInputs_returnsTrue() {
        boolean result = Equality.areEqual(null, null);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_equalObjects_returnsTrue() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        boolean result = Equality.areEqual(obj1, obj1);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_differentObjects_returnsFalse() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        boolean result = Equality.areEqual(obj1, obj2);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_nullAndObject_returnsFalse() {
        Object obj = new Object();
        boolean result = Equality.areEqual(null, obj);
        assertFalse(result);
    }

    @Test
    public void testAreArraysEqual_equalArrays_returnsTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        boolean result = Equality.areArraysEqual(arr1, arr2);
        assertTrue(result);
    }

    @Test
    public void testAreArraysEqual_differentArrays_returnsFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5, 6};
        boolean result = Equality.areArraysEqual(arr1, arr2);
        assertFalse(result);
    }

    @Test
    public void testAreArrayLengthsEqual_equalArrays_returnsTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        boolean result = Equality.areArrayLengthsEqual(arr1, arr2);
        assertTrue(result);
    }

    @Test
    public void testAreArrayLengthsEqual_differentArrays_returnsFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5, 6};
        boolean result = Equality.areArrayLengthsEqual(arr1, arr2);
        assertFalse(result);
    }

    @Test
    public void testAreArrayElementsEqual_equalArrays_returnsTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        boolean result = Equality.areArrayElementsEqual(arr1, arr2);
        assertTrue(result);
    }

    @Test
    public void testAreArrayElementsEqual_differentArrays_returnsFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5, 6};
        boolean result = Equality.areArrayElementsEqual(arr1, arr2);
        assertFalse(result);
    }

    @Test
    public void testIsArray_arrayObject_returnsTrue() {
        int[] arr = {1, 2, 3};
        boolean result = Equality.isArray(arr);
        assertTrue(result);
    }

    @Test
    public void testIsArray_nonArrayObject_returnsFalse() {
        Object obj = new Object();
        boolean result = Equality.isArray(obj);
        assertFalse(result);
    }
}