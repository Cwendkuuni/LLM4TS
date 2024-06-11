import static org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_NullObjects_ReturnsTrue() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_FirstObjectNull_ReturnsFalse() {
        Object obj = new Object();
        assertFalse(Equality.areEqual(null, obj));
    }

    @Test
    public void testAreEqual_SecondObjectNull_ReturnsFalse() {
        Object obj = new Object();
        assertFalse(Equality.areEqual(obj, null));
    }

    @Test
    public void testAreEqual_NotEqualObjects_ReturnsFalse() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_SameObject_ReturnsTrue() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }

    @Test
    public void testAreEqual_EqualArrays_ReturnsTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_ArraysDifferentLength_ReturnsFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_ArraysDifferentValues_ReturnsFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 4, 3};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_NestedArrays_ReturnsTrue() {
        int[][] arr1 = {{1, 2}, {3, 4}};
        int[][] arr2 = {{1, 2}, {3, 4}};
        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_NestedArraysDifferentLengths_ReturnsFalse() {
        int[][] arr1 = {{1, 2}, {3, 4}};
        int[][] arr2 = {{1, 2}, {3}};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_NestedArraysDifferentValues_ReturnsFalse() {
        int[][] arr1 = {{1, 2}, {3, 4}};
        int[][] arr2 = {{1, 2}, {3, 5}};
        assertFalse(Equality.areEqual(arr1, arr2));
    }
}