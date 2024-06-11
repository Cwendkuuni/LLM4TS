import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameMethodWithSameObjects() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = wanted;

        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testSameMethodWithDifferentObjectsOfSameClass() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = new Object();

        assertFalse(sameMatcher.matches(actual));
    }

    @Test(expected = AssertionError.class)
    public void testSameMethodWithNullObject() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = null;

        sameMatcher.matches(actual); // This line will throw AssertionError
    }

    @Test
    public void testSameMethodWithDifferentObjectClasses() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        String actual = "Different object";

        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testSameMethodWithSameStrings() {
        String wanted = "Same string";
        Same sameMatcher = new Same(wanted);
        String actual = wanted;

        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testSameMethodWithSameChars() {
        Character wanted = 'A';
        Same sameMatcher = new Same(wanted);
        Character actual = 'A';

        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testSameMethodWithDifferentStrings() {
        String wanted = "Same string";
        Same sameMatcher = new Same(wanted);
        String actual = "Different string";

        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testSameMethodWithDifferentChars() {
        Character wanted = 'A';
        Same sameMatcher = new Same(wanted);
        Character actual = 'B';

        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testDescribeToMethodWithSameObjects() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Description description = new Description();

        sameMatcher.describeTo(description);

        String expectedDescription = "same(Object)";
        String actualDescription = description.toString();

        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testDescribeToMethodWithDifferentObjects() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Description description = new Description();
        Object actual = new Object();

        sameMatcher.describeTo(description);

        String expectedDescription = "same(Object)";
        String actualDescription = description.toString();

        assertNotEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testAppendQuotingMethodWithString() {
        String wanted = "String";
        Description description = new Description();
        Same sameMatcher = new Same(wanted);

        sameMatcher.appendQuoting(description);

        String expectedDescription = "\"String\"";
        String actualDescription = description.toString();

        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testAppendQuotingMethodWithCharacter() {
        Character wanted = 'C';
        Description description = new Description();
        Same sameMatcher = new Same(wanted);

        sameMatcher.appendQuoting(description);

        String expectedDescription = "'C'";
        String actualDescription = description.toString();

        assertEquals(expectedDescription, actualDescription);
    }
}