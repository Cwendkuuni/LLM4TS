import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.CoreMatchers;
import org.mockito.internal.matchers.Same;

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
        Object actual = null;

        sameMatcher.matches(actual);
    }

    @Test
    public void testStringWantedAndActual() {
        String wanted = "test";
        Same sameMatcher = new Same(wanted);
        String actual = "test";

        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentStrings() {
        String wanted = "test";
        Same sameMatcher = new Same(wanted);
        String actual = "different";

        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testCharacterWantedAndActual() {
        Character wanted = 'a';
        Same sameMatcher = new Same(wanted);
        Character actual = 'a';

        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentCharacters() {
        Character wanted = 'a';
        Same sameMatcher = new Same(wanted);
        Character actual = 'b';

        assertFalse(sameMatcher.matches(actual));
    }
}