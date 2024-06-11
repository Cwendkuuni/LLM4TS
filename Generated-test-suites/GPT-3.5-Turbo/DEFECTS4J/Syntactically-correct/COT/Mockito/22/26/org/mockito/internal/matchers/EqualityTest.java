
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        // Test when both objects are null
        assertTrue(Equality.areEqual(null, null));
        
        // Test when one object is null and the other is not
        assertFalse(Equality.areEqual(null, new Object()));
        
        // Test when both objects are arrays
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));
        
        // Test when only one object is an array
        assertFalse(Equality.areEqual(arr1, new Object()));
        
        // Test when both objects are non-null and not arrays
        assertTrue(Equality.areEqual("hello", "hello"));
        
        // Test when both objects are non-null but not equal
        assertFalse(Equality.areEqual("hello", "world"));
    }
    
    @Test
    public void testAreArraysEqual() {
        // Test when array lengths are equal
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));
        
        // Test when array lengths are not equal
        int[] arr3 = {1, 2, 3};
        int[] arr4 = {1, 2};
        assertFalse(Equality.areArraysEqual(arr3, arr4));
    }
    
    @Test
    public void testAreArrayLengthsEqual() {
        // Test when array lengths are equal
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
        
        // Test when array lengths are not equal
        int[] arr3 = {1, 2, 3};
        int[] arr4 = {1, 2};
        assertFalse(Equality.areArrayLengthsEqual(arr3, arr4));
    }
    
    @Test
    public void testAreArrayElementsEqual() {
        // Test when array elements are equal
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
        
        // Test when array elements are not equal
        int[] arr3 = {1, 2, 3};
        int[] arr4 = {1, 4, 3};
        assertFalse(Equality.areArrayElementsEqual(arr3, arr4));
    }
    
    @Test
    public void testIsArray() {
        // Test when object is an array
        int[] arr = {1, 2, 3};
        assertTrue(Equality.isArray(arr));
        
        // Test when object is not an array
        assertFalse(Equality.isArray("hello"));
    }
}

