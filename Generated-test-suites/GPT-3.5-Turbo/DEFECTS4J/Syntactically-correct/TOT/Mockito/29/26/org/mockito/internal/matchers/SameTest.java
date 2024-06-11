import org.junit.Test;
import org.mockito.internal.matchers.Same;
import org.mockito.ArgumentMatcher;
import org.hamcrest.Description;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatches_sameObject_returnsTrue() {
        Same same = new Same("test");

        boolean result = same.matches("test");

        assertTrue(result);
    }

    @Test
    public void testMatches_differentObject_returnsFalse() {
        Same same = new Same("test");

        boolean result = same.matches("different");

        assertFalse(result);
    }

    @Test
    public void testDescribeTo_stringObject_appendsQuoting() {
        Same same = new Same("test");
        Description description = new Description();

        same.describeTo(description);

        assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void testDescribeTo_charObject_appendsQuoting() {
        Same same = new Same('a');
        Description description = new Description();

        same.describeTo(description);

        assertEquals("same('a')", description.toString());
    }

    @Test
    public void testDescribeTo_nonStringOrCharObject_doesNotAppendQuoting() {
        Same same = new Same(123);
        Description description = new Description();

        same.describeTo(description);

        assertEquals("same(123)", description.toString());
    }
}