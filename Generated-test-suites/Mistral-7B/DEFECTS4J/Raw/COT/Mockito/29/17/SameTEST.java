import org.junit.Test;
import org.junit.Assert;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import static org.mockito.internal.matchers.Same.same;

public class SameTest {

    @Test
    public void testSameObject() {
        Object wanted = new Object();
        Matcher<Object> matcher = same(wanted);
        Object actual = wanted;
        Assert.assertTrue(matcher.matches(actual));
    }

    @Test
    public void testSameNull() {
        Object wanted = null;
        Matcher<Object> matcher = same(wanted);
        Object actual = null;
        Assert.assertTrue(matcher.matches(actual));
    }

    @Test
    public void testSameString() {
        String wanted = "test String";
        Matcher<String> matcher = same(wanted);
        String actual = "test String";
        Assert.assertTrue(matcher.matches(actual));
    }

    @Test
    public void testDifferentObjects() {
        Object wanted = new Object();
        Matcher<Object> matcher = same(wanted);
        Object actual = new Object();
        Assert.assertFalse(matcher.matches(actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWanted() {
        Same matcher = new Same(null);
    }

    @Test
    public void testDescribeTo() {
        Object wanted = new Object();
        Matcher<Object> matcher = same(wanted);
        Description description = new Description();
        matcher.describeTo(description);
        String expectedDescription = "same(" + wanted.toString() + ")";
        String actualDescription = description.toString();
        Assert.assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testDifferentTypes() {
        Object wanted = new Object();
        Matcher<String> matcher = same(wanted);
        String actual = "test String";
        Assert.assertFalse(matcher.matches(actual));
    }

    @Test
    public void testDifferentClasses() {
        Integer wanted = new Integer(1);
        Matcher<Double> matcher = same(wanted);
        Double actual = 1.0;
        Assert.assertFalse(matcher.matches(actual));
    }

}