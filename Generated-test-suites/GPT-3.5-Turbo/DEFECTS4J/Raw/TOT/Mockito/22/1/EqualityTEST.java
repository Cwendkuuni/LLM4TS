import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_WithNullObjects_ReturnsTrue() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_WithOneNullObject_ReturnsFalse() {
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_WithDifferentObjects_ReturnsFalse() {
        assertFalse(Equality.areEqual(new Object(), new Object()));
    }

    @Test
    public void testAreEqual_WithEqualObjects_ReturnsTrue() {
        Object obj1 = new Object();
        Object obj2 = obj1;

        assertTrue(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_WithEqualArrays_ReturnsTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};

        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_WithDifferentArrays_ReturnsFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};

        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_WithDifferentArrayLengths_ReturnsFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};

        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual_WithEqualArrays_ReturnsTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5, 6};

        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual_WithDifferentArrays_ReturnsFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};

        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_WithEqualArrays_ReturnsTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};

        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_WithDifferentArrays_ReturnsFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};

        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_WithDifferentArrayLengths_ReturnsFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};

        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testIsArray_WithArray_ReturnsTrue() {
        int[] arr = {1, 2, 3};

        assertTrue(Equality.isArray(arr));
    }

    @Test
    public void testIsArray_WithNonArray_ReturnsFalse() {
        Object obj = new Object();

        assertFalse(Equality.isArray(obj));
    }
}