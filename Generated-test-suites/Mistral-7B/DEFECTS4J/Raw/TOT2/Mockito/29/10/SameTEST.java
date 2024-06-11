import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSame() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = wanted;

        // Test basic functionality
        Assert.assertTrue(sameMatcher.matches(actual));

        // Test edge cases
        // Test for null objects
        Object nullWanted = null;
        Same nullSameMatcher = new Same(nullWanted);
        Assert.assertFalse(nullSameMatcher.matches(actual));

        // Test for different classes
        Object differentObject = new Object();
        Assert.assertFalse(sameMatcher.matches(differentObject));

        // Test for different instances of the same class
        Object differentInstance = new Object() {};
        Assert.assertFalse(sameMatcher.matches(differentInstance));
    }
}