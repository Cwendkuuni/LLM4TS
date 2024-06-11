import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.Same;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatchesSameObjects() {
        Same matcher = new Same(new Object());
        Object actual = new Object();

        assertTrue(matcher.matches(actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesDifferentClasses() {
        Same matcher = new Same(new Integer(1));
        String actual = "Hello World";

        assertFalse(matcher.matches(actual));
    }

    @Test
    public void testMatchesNullObjects() {
        Same matcher = new Same(null);
        Object actual = null;

        assertTrue(matcher.matches(actual));
    }

    @Test
    public void testDescribeToQuotedStrings() {
        Same matcher = new Same("Hello");

        String description = "Matchers";
        matcher.describeTo(description);

        assertTrue(description.toString().contains("same(\"Hello\")"));
    }

    @Test
    public void testDescribeToQuotedCharacter() {
        Same matcher = new Same('A');

        String description = "Matchers";
        matcher.describeTo(description);

        assertTrue(description.toString().contains("same('A')"));
    }
}