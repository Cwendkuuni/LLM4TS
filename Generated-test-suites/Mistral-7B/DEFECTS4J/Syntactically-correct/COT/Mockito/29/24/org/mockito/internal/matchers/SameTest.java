import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static org.mockito.internal.matchers.Same.appendQuoting;

public class SameTest {

    @Test
    public void testSameObject() {
        Object wanted = new Object();
        Object actual = wanted;

        Matcher<Object> sameMatcher = new Same(wanted);

        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentObjects() {
        Object wanted = new Object();
        Object actual = new Object();

        Matcher<Object> sameMatcher = new Same(wanted);

        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testNullWanted() {
        Object wanted = null;
        Object actual = new Object();

        Matcher<Object> sameMatcher = new Same(wanted);

        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testNullActual() {
        Object wanted = new Object();
        Object actual = null;

        Matcher<Object> sameMatcher = new Same(wanted);

        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testStringWanted() {
        String wanted = "hello";
        String actual = "hello";

        Matcher<String> sameMatcher = new Same(wanted);

        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentStrings() {
        String wanted = "hello";
        String actual = "world";

        Matcher<String> sameMatcher = new Same(wanted);

        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testCharacterWanted() {
        Character wanted = 'a';
        Character actual = 'a';

        Matcher<Character> sameMatcher = new Same(wanted);

        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentCharacters() {
        Character wanted = 'a';
        Character actual = 'b';

        Matcher<Character> sameMatcher = new Same(wanted);

        assertFalse(sameMatcher.matches(actual));
    }

    private void describeTo(Matcher<Object> matcher, Description description) {
        description.appendText("same(");
        appendQuoting(description, matcher.getWanted());
        description.appendText(")");
    }

    @Test(expected = NullPointerException.class)
    public void testNullMatcher() {
        Object wanted = new Object();
        Description description = new Description();
        Same sameMatcher = null;

        describeTo(sameMatcher, description);
    }
}