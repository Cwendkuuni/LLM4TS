import org.junit.Test;
import org.junit.Assert;
import org.hamcrest.Matcher;
import org.hamcrest.Description;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameObject() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = wanted;

        Matcher<Object> matcher = sameMatcher;
        boolean result = matcher.matches(actual);

        Assert.assertTrue("match: wanted == actual", result);
    }

    @Test
    public void testSameString() {
        String wanted = "test";
        Same sameMatcher = new Same(wanted);
        String actual = "test";

        Matcher<String> matcher = sameMatcher;
        boolean result = matcher.matches(actual);

        Assert.assertTrue("match: wanted == actual", result);
    }

    @Test
    public void testSameCharacter() {
        Character wanted = 'a';
        Same sameMatcher = new Same(wanted);
        Character actual = 'a';

        Matcher<Character> matcher = sameMatcher;
        boolean result = matcher.matches(actual);

        Assert.assertTrue("match: wanted == actual", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDifferentObjects() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = new Object();

        Matcher<Object> matcher = sameMatcher;
        boolean result = matcher.matches(actual);

        // this test should throw an IllegalArgumentException
        Assert.assertFalse("different objects should not match", result);
    }

    @Test
    public void testNullWanted() {
        Same sameMatcher = new Same(null);

        try {
            sameMatcher.matches(new Object());
        } catch (NullPointerException e) {
            // expected result
        }
    }

    @Test
    public void testNullActual() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        try {
            sameMatcher.matches(null);
        } catch (NullPointerException e) {
            // expected result
        }
    }

    @Test
    public void testDifferentTypes() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Integer actual = 1;

        Matcher<Object> matcher = sameMatcher;
        boolean result = matcher.matches(actual);

        Assert.assertFalse("different types should not match", result);
    }

    @Test
    public void testDifferentClass() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        String actual = "test";

        Matcher<Object> matcher = sameMatcher;
        boolean result = matcher.matches(actual);

        Assert.assertFalse("different classes should not match", result);
    }

    @Test
    public void testStringWithQuotes() {
        String wanted = "test";
        Same sameMatcher = new Same(wanted);
        String actual = "test";

        Matcher<String> matcher = sameMatcher;
        boolean result = matcher.matches(actual);

        Assert.assertTrue("match: wanted == actual", result);
    }

    @Test
    public void testCharacterWithQuotes() {
        Character wanted = 'a';
        Same sameMatcher = new Same(wanted);
        Character actual = 'a';

        Matcher<Character> matcher = sameMatcher;
        boolean result = matcher.matches(actual);

        Assert.assertTrue("match: wanted == actual", result);
    }
}