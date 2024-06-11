import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.Same;

public class SameTest {

    // Test case for matches() method - typical use case
    @Test
    public void testMatches_typical() {
        // Create a Same object with a specific value
        Same same = new Same(5);
        
        // Test with the same object
        Assert.assertTrue(same.matches(5));
    }

    // Test case for matches() method - edge case
    @Test
    public void testMatches_edge() {
        // Create a Same object with a specific string value
        Same same = new Same("hello");
        
        // Test with the same string object
        Assert.assertTrue(same.matches("hello"));
    }

    // Test case for matches() method - error scenario
    @Test
    public void testMatches_error() {
        // Create a Same object with a specific value
        Same same = new Same(10);
        
        // Test with a different value
        Assert.assertFalse(same.matches(5));
    }

    // Test case for describeTo() method - typical use case
    @Test
    public void testDescribeTo_typical() {
        // Create a Same object with a specific value
        Same same = new Same("hello");
        
        // Create a Description object for assertion
        Description description = new Description();
        
        // Call the describeTo() method
        same.describeTo(description);
        
        // Assert the description is correct
        Assert.assertEquals("same(\"hello\")", description.toString());
    }

    // Test case for describeTo() method - edge case
    @Test
    public void testDescribeTo_edge() {
        // Create a Same object with a specific character value
        Same same = new Same('a');
        
        // Create a Description object for assertion
        Description description = new Description();
        
        // Call the describeTo() method
        same.describeTo(description);
        
        // Assert the description is correct
        Assert.assertEquals("same('a')", description.toString());
    }

    // Test case for describeTo() method - error scenario
    @Test(expected = NullPointerException.class)
    public void testDescribeTo_error() {
        // Create a Same object with a null value
        Same same = new Same(null);
        
        // Create a Description object for assertion
        Description description = new Description();
        
        // Call the describeTo() method
        same.describeTo(description);
        
        // The method should throw a NullPointerException
    }
}