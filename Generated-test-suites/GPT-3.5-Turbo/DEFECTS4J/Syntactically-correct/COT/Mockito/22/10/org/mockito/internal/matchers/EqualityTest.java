import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

    @Test
    public void testAreEqual_NullObjects_ReturnsTrue() {
        boolean result = Equality.areEqual(null, null);
        Assert.assertTrue(result);
    }

    @Test
    public void testAreEqual_OneNullObject_ReturnsFalse() {
        boolean result = Equality.areEqual(null, new Object());
        Assert.assertFalse(result);
    }
    
    @Test
    public void testAreEqual_DifferentTypes_ReturnsFalse() {
        boolean result = Equality.areEqual("hello", 123);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testAreArrayLengthsEqual_DifferentLengthArrays_ReturnsFalse() {
        boolean result = Equality.areArrayLengthsEqual(new Object[] {1, 2, 3}, new Object[] {1, 2});
        Assert.assertFalse(result);
    }
    
    @Test
    public void testAreArrayElementsEqual_DifferentElementsArrays_ReturnsFalse() {
        boolean result = Equality.areArrayElementsEqual(new Object[] {1, 2, 3}, new Object[] {1, 2, 4});
        Assert.assertFalse(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAreEqual_NonComparableObjects_ThrowsException() {
        Equality.areEqual(new Object(), new Object());
    }

    // Add more test cases for other methods if required

}



// Individual test cases go here

