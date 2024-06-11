import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        // Test case 1: Both objects are null
        assertTrue(Equality.areEqual(null, null));

        // Test case 2: One object is null
        assertFalse(Equality.areEqual(null, new Object()));

        // Test case 3: Objects are equal
        assertTrue(Equality.areEqual("hello", "hello"));

        // Test case 4: Objects are not equal
        assertFalse(Equality.areEqual("hello", "world"));

        // Test case 5: Arrays are equal
        assertTrue(Equality.areEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));

        // Test case 6: Arrays are not equal
        assertFalse(Equality.areEqual(new int[] {1, 2, 3}, new int[] {4, 5, 6}));

        // Additional test cases can be added for edge cases and boundary conditions
    }

    @Test
    public void testAreArraysEqual() {
        // Test case 1: Arrays have equal length
        assertTrue(Equality.areArraysEqual(new int[] {1, 2, 3}, new int[] {4, 5, 6}));

        // Test case 2: Arrays have different length
        assertFalse(Equality.areArraysEqual(new int[] {1, 2, 3}, new int[] {1, 2}));

        // Additional test cases can be added for edge cases and boundary conditions
    }

    @Test
    public void testAreArrayLengthsEqual() {
        // Test case 1: Arrays have equal length
        assertTrue(Equality.areArrayLengthsEqual(new int[] {1, 2, 3}, new int[] {4, 5, 6}));

        // Test case 2: Arrays have different length
        assertFalse(Equality.areArrayLengthsEqual(new int[] {1, 2, 3}, new int[] {1, 2}));

        // Additional test cases can be added for edge cases and boundary conditions
    }

    @Test
    public void testAreArrayElementsEqual() {
        // Test case 1: Array elements are equal
        assertTrue(Equality.areArrayElementsEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));

        // Test case 2: Array elements are not equal
        assertFalse(Equality.areArrayElementsEqual(new int[] {1, 2, 3}, new int[] {4, 5, 6}));

        // Additional test cases can be added for edge cases and boundary conditions
    }

    @Test
    public void testIsArray() {
        // Test case 1: Object is an array
        assertTrue(Equality.isArray(new int[] {1, 2, 3}));

        // Test case 2: Object is not an array
        assertFalse(Equality.isArray(new Object()));

        // Additional test cases can be added for edge cases and boundary conditions
    }
}