
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

import static org.junit.Assert.*;

public class EqualityTest {

    // Test case for "areEqual" method
    @Test
    public void testAreEqual() {
        // Testing when both objects are null
        assertTrue(Equality.areEqual(null, null));
        
        // Testing when only one object is null
        assertFalse(Equality.areEqual("Hello", null));
        assertFalse(Equality.areEqual(null, "Hello"));
        
        // Testing when both objects are arrays
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areEqual(array1, array2));
        
        // Testing when both objects are not arrays
        assertFalse(Equality.areEqual("Hello", "World"));
        
        // Testing for other data types
        assertTrue(Equality.areEqual(5, 5));
        assertFalse(Equality.areEqual(5, 10));
        assertTrue(Equality.areEqual(true, true));
        assertFalse(Equality.areEqual(true, false));
        assertTrue(Equality.areEqual('a', 'a'));
        assertFalse(Equality.areEqual('a', 'b'));
    }
    
    // Test case for "areArraysEqual" method
    @Test
    public void testAreArraysEqual() {
        // Testing for arrays with equal length and equal elements
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areArraysEqual(array1, array2));
        
        // Testing for arrays with equal length but different elements
        int[] array3 = {1, 2, 3};
        int[] array4 = {1, 2, 4};
        assertFalse(Equality.areArraysEqual(array3, array4));
        
        // Testing for arrays with different lengths
        int[] array5 = {1, 2, 3};
        int[] array6 = {1, 2};
        assertFalse(Equality.areArraysEqual(array5, array6));
    }
    
    // Test case for "areArrayLengthsEqual" method
    @Test
    public void testAreArrayLengthsEqual() {
        // Testing for arrays with equal lengths
        int[] array1 = {1, 2, 3};
        int[] array2 = {4, 5, 6};
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));
        
        // Testing for arrays with different lengths
        int[] array3 = {1, 2, 3};
        int[] array4 = {1, 2};
        assertFalse(Equality.areArrayLengthsEqual(array3, array4));
    }
    
    // Test case for "areArrayElementsEqual" method
    @Test
    public void testAreArrayElementsEqual() {
        // Testing for arrays with equal elements
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(array1, array2));
        
        // Testing for arrays with different elements
        int[] array3 = {1, 2, 3};
        int[] array4 = {1, 2, 4};
        assertFalse(Equality.areArrayElementsEqual(array3, array4));
    }
    
    // Test case for "isArray" method
    @Test
    public void testIsArray() {
        // Testing for array object
        int[] array1 = {1, 2, 3};
        assertTrue(Equality.isArray(array1));
        
        // Testing for non-array object
        assertFalse(Equality.isArray("Hello"));
    }
}
