import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        assertTrue(Equality.areEqual(1, 1));
        assertFalse(Equality.areEqual(1, 2));
    }

    @Test
    public void testAreArraysEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));
        
        int[] arr3 = {1, 2, 3};
        int[] arr4 = {3, 2, 1};
        assertFalse(Equality.areArraysEqual(arr3, arr4));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3, 4};
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
        
        int[] arr3 = {1, 2, 3};
        int[] arr4 = {3, 2, 1};
        assertFalse(Equality.areArrayElementsEqual(arr3, arr4));
    }

    @Test
    public void testIsArray() {
        int[] arr = {1, 2, 3};
        assertTrue(Equality.isArray(arr));
        
        String str = "test";
        assertFalse(Equality.isArray(str));
    }

}