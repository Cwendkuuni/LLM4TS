import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {
    @Test
    public void testAreEqualNull() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqualPrimitiveArrays() {
        int[] arr1 = new int[3];
        int[] arr2 = new int[3];
        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqualPrimitiveArraysDifferentLengths() {
        int[] arr1 = new int[3];
        int[] arr2 = new int[2];
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqualPrimitiveArraysDifferentElements() {
        int[] arr1 = new int[]{1, 2, 3};
        int[] arr2 = new int[]{1, 2, 4};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqualReferenceArrays() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqualReferenceArraysDifferentElements() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 4};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqualReferenceArraysDifferentLengths() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAreEqualInvalidType() {
        String str1 = new String("abc");
        int[] arr2 = new int[3];
        assertFalse(Equality.areEqual(str1, arr2));
    }

    @Test
    public void testAreArraysEqual() {
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
        int[] arr2 = new int[]{1, 2, 4};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqualSameLengthButDifferentElements() {
        int[] arr1 = new int[]{1, 2, 3};
        int[] arr2 = new int[]{2, 3, 4};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqualSameLengthAndSameElements() {
        int[] arr1 = new int[]{1, 2, 3};
        int[] arr2 = new int[]{1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testIsArray() {
        Object obj1 = new Object();
        Object[] arr1 = new Object[3];
        assertTrue(Equality.isArray(arr1));
        assertFalse(Equality.isArray(obj1));
    }
}