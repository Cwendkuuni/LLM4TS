import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        assertTrue(Equality.areEqual("hello", "hello"));
        assertFalse(Equality.areEqual("hello", "world"));
    }

    @Test
    public void testAreArraysEqual() {
        Object[] arr1 = {1, 2, 3};
        Object[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));
        Object[] arr3 = {4, 5, 6};
        assertFalse(Equality.areArraysEqual(arr1, arr3));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        Object[] arr1 = {1, 2, 3};
        Object[] arr2 = {1, 2, 3, 4};
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual() {
        Object[] arr1 = {1, 2, 3};
        Object[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
        Object[] arr3 = {4, 5, 6};
        assertFalse(Equality.areArrayElementsEqual(arr1, arr3));
    }

    @Test
    public void testIsArray() {
        Object[] arr = {1, 2, 3};
        assertTrue(Equality.isArray(arr));
        assertFalse(Equality.isArray("hello"));
    }
}