import org.junit.Assert;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        // Test when o1 and o2 are both null
        Assert.assertTrue(Equality.areEqual(null, null));

        // Test when o1 is null and o2 is not null
        Assert.assertFalse(Equality.areEqual(null, new Object()));

        // Test when o1 is not null and o2 is null
        Assert.assertFalse(Equality.areEqual(new Object(), null));

        // Test when o1 and o2 are objects with different values
        Assert.assertFalse(Equality.areEqual(new Object(), new Object()));

        // Test when o1 and o2 are objects with the same value
        Object obj = new Object();
        Assert.assertTrue(Equality.areEqual(obj, obj));

        // Test when both o1 and o2 are arrays with different length
        Assert.assertFalse(Equality.areEqual(new Object[1], new Object[2]));

        // Test when both o1 and o2 are arrays with the same length but different values
        Assert.assertFalse(Equality.areEqual(new Object[]{1}, new Object[]{2}));

        // Test when both o1 and o2 are arrays with the same length and same values
        Assert.assertTrue(Equality.areEqual(new Object[]{1, 2, 3}, new Object[]{1, 2, 3}));

        // Test when o1 and o2 are arrays with different types
        Assert.assertFalse(Equality.areEqual(new Object[]{1}, new Integer[]{1}));
    }

    @Test
    public void testAreArraysEqual() {
        // Test when o1 and o2 are arrays with different length
        Assert.assertFalse(Equality.areArraysEqual(new Object[1], new Object[2]));

        // Test when o1 and o2 are arrays with the same length but different values
        Assert.assertFalse(Equality.areArraysEqual(new Object[]{1}, new Object[]{2}));

        // Test when o1 and o2 are arrays with the same length and same values
        Assert.assertTrue(Equality.areArraysEqual(new Object[]{1, 2, 3}, new Object[]{1, 2, 3}));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        // Test when o1 and o2 are arrays with different length
        Assert.assertFalse(Equality.areArrayLengthsEqual(new Object[1], new Object[2]));

        // Test when o1 and o2 are arrays with the same length
        Assert.assertTrue(Equality.areArrayLengthsEqual(new Object[]{1}, new Object[]{2}));
    }

    @Test
    public void testAreArrayElementsEqual() {
        // Test when o1 and o2 are arrays with different values
        Assert.assertFalse(Equality.areArrayElementsEqual(new Object[]{1}, new Object[]{2}));

        // Test when o1 and o2 are arrays with the same values
        Assert.assertTrue(Equality.areArrayElementsEqual(new Object[]{1, 2, 3}, new Object[]{1, 2, 3}));
    }

    @Test
    public void testIsArray() {
        // Test when o is not an array
        Assert.assertFalse(Equality.isArray(""));
        
        // Test when o is an array
        Assert.assertTrue(Equality.isArray(new Object[1]));
    }
}