import org.junit.Test;
import org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object wanted = new Object();
        Object actual = wanted;

        Same sameMatcher = new Same(wanted);

        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentInstances() {
        Object wanted = new Object();
        Object actual = new Object();

        Same sameMatcher = new Same(wanted);

        assertFalse(sameMatcher.matches(actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWanted() {
        Same sameMatcher = new Same(null);
    }

    @Test
    public void testStringMatch() {
        String wanted = "Hello World";
        String actual = "Hello World";

        Same sameMatcher = new Same(wanted);

        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testStringDifference() {
        String wanted = "Hello World";
        String actual = "Hello Test";

        Same sameMatcher = new Same(wanted);

        assertFalse(sameMatcher.matches(actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonStringQuotedWanted() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
    }

    @Test
    public void testQuotedStringMatch() {
        String wanted = "\"Hello World\"";
        String actual = "Hello World";

        Same sameMatcher = new Same(wanted);

        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testQuotedStringDifference() {
        String wanted = "\"Hello World\"";
        String actual = "Hello Test";

        Same sameMatcher = new Same(wanted);

        assertFalse(sameMatcher.matches(actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonQuotedCharacterWanted() {
        char wanted = 'A';
        Same sameMatcher = new Same(wanted);
    }

    @Test
    public void testCharacterMatch() {
        char wanted = 'A';
        char actual = 'A';

        Same sameMatcher = new Same(wanted);

        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testCharacterDifference() {
        char wanted = 'A';
        char actual = 'B';

        Same sameMatcher = new Same(wanted);

        assertFalse(sameMatcher.matches(actual));
    }
}