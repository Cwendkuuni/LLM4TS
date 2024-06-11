import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqualBothNull() {
        Object o1 = null;
        Object o2 = null;
        assertTrue(Equality.areEqual(o1, o2));
    }

    @Test
    public void testAreEqualOneNull() {
        Object o1 = null;
        Object o2 = new Object();
        assertFalse(Equality.areEqual(o1, o2));
    }

    @Test
    public void testAreEqualDifferentTypes() {
        Object o1 = "Hello";
        Object o2 = 5;
        assertFalse(Equality.areEqual(o1, o2));
    }

    @Test
    public void testAreEqualSameObject() {
        Object o1 = new Object();
        Object o2 = o1;
        assertTrue(Equality.areEqual(o1, o2));
    }

    @Test
    public void testAreEqualEqualObjects() {
        Object o1 = new Object();
        Object o2 = new Object();
        assertFalse(Equality.areEqual(o1, o2));
    }

    @Test
    public void testAreArraysEqualSameLengthAndElements() {
        int[] arr1 = new int[]{1, 2, 3};
        int[] arr2 = new int[]{1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqualDifferentLengths() {
        int[] arr1 = new int[]{1, 2, 3};
        int[] arr2 = new int[]{1, 2};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqualDifferentElements() {
        int[] arr1 = new int[]{1, 2, 3};
        int[] arr2 = new int[]{3, 2, 1};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqualSameLength() {
        int[] arr1 = new int[]{1, 2, 3};
        int[] arr2 = new int[]{4, 5, 6};
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqualDifferentLengths() {
        int[] arr1 = new int[]{1, 2, 3};
        int[] arr2 = new int[]{1, 2};
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqualAllEqual() {
        int[] arr1 = new int[]{1, 2, 3};
        int[] arr2 = new int[]{1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqualNotEqual() {
        int[] arr1 = new int[]{1, 2, 3};
        int[] arr2 = new int[]{3, 2, 1};
        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testIsArrayTrue() {
        int[] arr = new int[]{1, 2, 3};
        assertTrue(Equality.isArray(arr));
    }

    @Test
    public void testIsArrayFalse() {
        Object obj = new Object();
        assertFalse(Equality.isArray(obj));
    }
}