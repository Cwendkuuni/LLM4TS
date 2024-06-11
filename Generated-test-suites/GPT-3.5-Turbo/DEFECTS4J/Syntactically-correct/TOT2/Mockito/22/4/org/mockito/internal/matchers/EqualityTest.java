import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqualWithNullObjects() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqualWithArrayObjects() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        int[] arr3 = {1, 2, 4};

        assertTrue(Equality.areEqual(arr1, arr2));
        assertFalse(Equality.areEqual(arr1, arr3));
    }

    @Test
    public void testAreEqualWithDifferentObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        assertFalse(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};

        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsNotEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};

        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};

        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsNotEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};

        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testIsArray() {
        int[] arr = {1, 2, 3};

        assertTrue(Equality.isArray(arr));
    }

    @Test
    public void testIsNotArray() {
        Object obj = new Object();

        assertFalse(Equality.isArray(obj));
    }
}