import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        Object actual = wanted;
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentInstances() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        Object actual = new Object();
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testNullAndSameObject() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        Object actual = wanted;
        assertNull(sameMatcher.matches(null));
        assertTrue(sameMatcher.matches(actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWantedObject() {
        Same sameMatcher = new Same(null);
        throw new IllegalArgumentException();
    }

    @Test
    public void testSameString() {
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
    public void testSameCharacter() {
        char wanted = 'a';
        Same sameMatcher = new Same(wanted);

        char actual = 'a';
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentCharacters() {
        char wanted = 'a';
        Same sameMatcher = new Same(wanted);

        char actual = 'b';
        assertFalse(sameMatcher.matches(actual));
    }
}