import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.matchers.Same;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class SameTest {

    @Test
    public void testSameMethodWithSameObjects() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = wanted;

        boolean result = sameMatcher.matches(actual);
        Assert.assertTrue(result);
    }

    @Test
    public void testDescribeToMethodWithString() {
        Object wanted = "test string";
        Same sameMatcher = new Same(wanted);

        Description description = new Description();
        sameMatcher.describeTo(description);

        String expectedDescription = "same(\"test string\")";
        String actualDescription = description.toString();

        Assert.assertEquals(expectedDescription, actualDescription);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSameMethodWithDifferentObjects() {
        Object wanted = new Object();
        Object actual = new Object();

        Same sameMatcher = new Same(wanted);

        boolean result = sameMatcher.matches(actual);
        Assert.assertFalse(result);
    }

    @Test
    public void testSameMethodWithNullObject() {
        Object wanted = null;
        Same sameMatcher = new Same(wanted);

        Object actual = null;

        boolean result = sameMatcher.matches(actual);
        Assert.assertTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testDescribeToMethodWithNullObject() {
        Object wanted = null;
        Same sameMatcher = new Same(wanted);

        Description description = mock(Description.class);

        sameMatcher.describeTo(description);
    }
}