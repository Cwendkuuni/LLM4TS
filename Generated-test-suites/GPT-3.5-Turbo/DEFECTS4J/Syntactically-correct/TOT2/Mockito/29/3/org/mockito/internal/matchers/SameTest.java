import static org.junit.Assert.*;
import static org.mockito.internal.matchers.Same.*;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class SameTest {

    private Same sameInstance;

    @Before
    public void setUp() {
        sameInstance = new Same("test");
    }

    @Test
    public void testMatches() {
        assertTrue(sameInstance.matches("test"));
        assertFalse(sameInstance.matches("different"));
    }

    @Test
    public void testDescribeTo() {
        Description description = new Description();
        sameInstance.describeTo(description);
        assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void testAppendQuoting() {
        Description descriptionString = new Description();
        Description descriptionChar = new Description();

        sameInstance.appendQuoting(descriptionString);
        assertEquals("\"", descriptionString.toString());

        sameInstance = new Same('a');
        sameInstance.appendQuoting(descriptionChar);
        assertEquals("'", descriptionChar.toString());

        sameInstance = new Same(1); // Testing for non-String and non-Character
        Description descriptionNonString = new Description();
        sameInstance.appendQuoting(descriptionNonString);
        assertEquals("", descriptionNonString.toString()); // No quoting should be appended
    }
}