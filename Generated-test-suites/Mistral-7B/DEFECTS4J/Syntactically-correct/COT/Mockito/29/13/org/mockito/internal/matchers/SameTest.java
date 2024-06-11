import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameMethodWithSameObjects() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = wanted;

        boolean result = sameMatcher.matches(actual);

        Assert.assertTrue("Should return true because wanted and actual are the same object", result);
    }

    @Test
    public void testSameMethodWithDifferentObjectsTypes() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Integer actual = 1;

        boolean result = sameMatcher.matches(actual);

        Assert.assertFalse("Should return false because wanted is an object and actual is an integer", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSameMethodWithNullWanted() {
        Same sameMatcher = new Same(null);

        // This test case is expected to throw an IllegalArgumentException since the constructor checks for null values
        // and throws an exception if the wanted parameter is null
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSameMethodWithNullActual() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        // This test case is expected to throw an IllegalArgumentException since the matches method checks for null values
        // and throws an exception if the actual parameter is null
        sameMatcher.matches(null);
    }

    @Test
    public void testDescribeToMethodWithStringWanted() {
        Object wanted = "mockito";
        Same sameMatcher = new Same(wanted);

        Description description = new Description();
        sameMatcher.describeTo(description);

        String expectedDescription = "same(\"mockito\")";
        String actualDescription = description.toString();

        Assert.assertEquals("Should generate the correct description for a String wanted", expectedDescription, actualDescription);
    }

    @Test
    public void testDescribeToMethodWithCharacterWanted() {
        Character wanted = 'A';
        Same sameMatcher = new Same(wanted);

        Description description = new Description();
        sameMatcher.describeTo(description);

        String expectedDescription = "same('A')";
        String actualDescription = description.toString();

        Assert.assertEquals("Should generate the correct description for a Character wanted", expectedDescription, actualDescription);
    }
}