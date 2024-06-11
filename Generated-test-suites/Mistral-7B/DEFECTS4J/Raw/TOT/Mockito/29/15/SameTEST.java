import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.matchers.Same;
import org.hamcrest.Description;

public class SameTest {

    @Test
    public void testSameObjects() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        Object actual = wanted;
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentObjects() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        Object actual = new Object();
        assertFalse(sameMatcher.matches(actual));
    }

    @Test(expected = AssertionError.class)
    public void testNullWanted() {
        Same sameMatcher = new Same(null);
        Object actual = new Object();
        sameMatcher.matches(actual);
    }

    @Test(expected = AssertionError.class)
    public void testNullActual() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        sameMatcher.matches(null);
    }

    @Test
    public void testSameStrings() {
        String wanted = "test string";
        Same sameMatcher = new Same(wanted);

        String actual = wanted;
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentStrings() {
        String wanted = "test string";
        Same sameMatcher = new Same(wanted);

        String actual = "different string";
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testSameChars() {
        Character wanted = 'a';
        Same sameMatcher = new Same(wanted);

        Character actual = 'a';
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentChars() {
        Character wanted = 'a';
        Same sameMatcher = new Same(wanted);

        Character actual = 'b';
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testDescribeTo() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        Description description = new Description();
        sameMatcher.describeTo(description);
        assertEquals("same([Object object])", description.toString());
    }
}