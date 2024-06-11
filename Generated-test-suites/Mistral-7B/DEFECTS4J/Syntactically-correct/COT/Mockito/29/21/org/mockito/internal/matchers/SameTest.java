import org.junit.Test;
import static org.junit.Assert.*;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class SameTest {

    @Test
    public void testSameMethodWithSameObjects() {
        Object wanted = new Object();
        Object actual = wanted;
        Same sameMatcher = new Same(wanted);
        Matcher<Object> matcher = sameMatcher;
         assertTrue(matcher.matches(actual));
    }

    @Test
    public void testSameMethodWithDifferentObjectsButSameReference() {
        Object wanted = new Object();
        Object actual = wanted;
        Same sameMatcher = new Same(wanted);
        Matcher<Object> matcher = sameMatcher;
         assertTrue(matcher.matches(actual));
    }

    @Test
    public void testSameMethodWithNullObject() {
        Object wanted = null;
        Object actual = null;
        Same sameMatcher = new Same(wanted);
        Matcher<Object> matcher = sameMatcher;
         assertTrue(matcher.matches(actual));
    }

    @Test
    public void testSameMethodWithDifferentObjectsButEqualValues() {
        Object wanted = new Object();
        Object actual = new Object();
        if (wanted.getClass() != actual.getClass()) {
            actual = new Object() {
                @Override
                public boolean equals(Object o) {
                    return wanted.equals(o);
                }
            };
        }
        Same sameMatcher = new Same(wanted);
        Matcher<Object> matcher = sameMatcher;
        assertTrue(matcher.matches(actual));
    }

    @Test(expected = NullPointerException.class)
    public void testSameMethodWithNullWantedObject() {
        Object actual = new Object();
        Same sameMatcher = new Same(null);
        Matcher<Object> matcher = sameMatcher;
        matcher.matches(actual);
    }

    @Test(expected = NullPointerException.class)
    public void testSameMethodWithNullActualObject() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Matcher<Object> matcher = sameMatcher;
        matcher.matches(null);
    }

    @Test
    public void testSameMethodWithDifferentClasses() {
        int wanted = 1;
        String actual = "one";
        Same sameMatcher = new Same(wanted);
        Matcher<Object> matcher = sameMatcher;
        assertFalse(matcher.matches(actual));
    }

    @Test
    public void testDescribeToMethod() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Description description = new Description();
        sameMatcher.describeTo(description);
        assertTrue(description.toString().contains("same(" + wanted.toString() + ")"));
    }
}