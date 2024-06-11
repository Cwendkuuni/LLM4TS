import org.junit.Assert;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        // Typical use case
        Assert.assertTrue(Equality.areEqual("hello", "hello"));
        
        // Edge cases
        Assert.assertTrue(Equality.areEqual(null, null));
        Assert.assertTrue(Equality.areEqual(1, 1));
        Assert.assertFalse(Equality.areEqual(2, 1));
        Assert.assertFalse(Equality.areEqual("hello", null));
        Assert.assertFalse(Equality.areEqual(null, "hello"));
        
        // Error scenarios
        try {
            Assert.assertFalse(Equality.areEqual(new int[]{1, 2}, new int[]{1, 2, 3}));
        } catch (Exception e) {
            Assert.fail("Exception was thrown when comparing arrays of different lengths");
        }
    }

    @Test
    public void testAreArraysEqual() {
        // Typical use case
        Assert.assertTrue(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        
        // Edge cases
        Assert.assertTrue(Equality.areArraysEqual(null, null));
        Assert.assertTrue(Equality.areArraysEqual(new int[]{}, new int[]{}));
        Assert.assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{3, 2, 1}));
        Assert.assertFalse(Equality.areArraysEqual(new int[]{1, 2}, new int[]{1, 2, 3}));
        
        // Error scenarios
        try {
            Assert.assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new String[]{"1", "2", "3"}));
        } catch (Exception e) {
            Assert.fail("Exception was thrown when comparing arrays of different types");
        }
    }

    @Test
    public void testAreArrayLengthsEqual() {
        // Typical use case
        Assert.assertTrue(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{4, 5, 6}));
        
        // Edge cases
        Assert.assertTrue(Equality.areArrayLengthsEqual(null, null));
        Assert.assertTrue(Equality.areArrayLengthsEqual(new int[]{}, new int[]{}));
        Assert.assertFalse(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{3, 2, 1}));
        
        // Error scenarios
        try {
            Assert.assertFalse(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new String[]{"1", "2", "3"}));
        } catch (Exception e) {
            Assert.fail("Exception was thrown when comparing array lengths of different types");
        }
    }

    @Test
    public void testAreArrayElementsEqual() {
        // Typical use case
        Assert.assertTrue(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        
        // Edge cases
        Assert.assertTrue(Equality.areArrayElementsEqual(null, null));
        Assert.assertTrue(Equality.areArrayElementsEqual(new int[]{}, new int[]{}));
        Assert.assertFalse(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{3, 2, 1}));
        Assert.assertFalse(Equality.areArrayElementsEqual(new int[]{1, 2}, new int[]{1, 2, 3}));
        
        // Error scenarios
        try {
            Assert.assertFalse(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new String[]{"1", "2", "3"}));
        } catch (Exception e) {
            Assert.fail("Exception was thrown when comparing array elements of different types");
        }
    }

    @Test
    public void testIsArray() {
        // Typical use case
        Assert.assertTrue(Equality.isArray(new int[]{1, 2, 3}));
        
        // Edge cases
        Assert.assertFalse(Equality.isArray(null));
        Assert.assertFalse(Equality.isArray("hello"));
        Assert.assertFalse(Equality.isArray(1));
    }
}