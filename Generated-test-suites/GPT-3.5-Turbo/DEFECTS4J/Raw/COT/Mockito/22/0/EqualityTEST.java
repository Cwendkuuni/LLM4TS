import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        // Basic Test Cases
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
        assertTrue(Equality.areEqual(new Object(), new Object()));

        // Edge Cases
        assertFalse(Equality.areEqual(new Object(), new Object())); // Non-equal non-null objects
        assertTrue(Equality.areEqual(null, null)); // Equal null objects
    }

    @Test
    public void testAreArraysEqual() {
        // Basic Test Cases
        assertTrue(Equality.areArraysEqual(new Object[] {1, 2, 3}, new Object[] {1, 2, 3}));
        assertFalse(Equality.areArraysEqual(new Object[] {1, 2, 3}, new Object[] {1, 2, 4}));

        // Edge Cases
        assertFalse(Equality.areArraysEqual(new Object[] {1, 2, 3}, new Object[] {4, 5, 6})); // Non-equal arrays
        assertTrue(Equality.areArraysEqual(new Object[] {}, new Object[] {})); // Equal empty arrays
    }

    @Test
    public void testAreArrayLengthsEqual() {
        // Basic Test Cases
        assertTrue(Equality.areArrayLengthsEqual(new Object[] {1, 2, 3}, new Object[] {4, 5, 6}));
        assertFalse(Equality.areArrayLengthsEqual(new Object[] {1, 2, 3}, new Object[] {1, 2}));

        // Edge Cases
        assertFalse(Equality.areArrayLengthsEqual(new Object[] {1, 2, 3}, new Object[] {4, 5, 6, 7})); // Non-equal array lengths
        assertTrue(Equality.areArrayLengthsEqual(new Object[] {}, new Object[] {})); // Equal empty array lengths
    }

    @Test
    public void testAreArrayElementsEqual() {
        // Basic Test Cases
        assertTrue(Equality.areArrayElementsEqual(new Object[] {1, 2, 3}, new Object[] {1, 2, 3}));
        assertFalse(Equality.areArrayElementsEqual(new Object[] {1, 2, 3}, new Object[] {1, 2, 4}));

        // Edge Cases
        assertFalse(Equality.areArrayElementsEqual(new Object[] {1, 2, 3}, new Object[] {4, 5, 6})); // Non-equal array elements
        assertTrue(Equality.areArrayElementsEqual(new Object[] {}, new Object[] {})); // Equal empty array elements
    }

    @Test
    public void testIsArray() {
        // Basic Test Cases
        assertTrue(Equality.isArray(new Object[] {1, 2, 3}));
        assertFalse(Equality.isArray(new Object()));

        // Edge Cases
        assertTrue(Equality.isArray(new Object[] {})); // Empty array
        assertFalse(Equality.isArray(new Object())); // Non-array object
    }
}