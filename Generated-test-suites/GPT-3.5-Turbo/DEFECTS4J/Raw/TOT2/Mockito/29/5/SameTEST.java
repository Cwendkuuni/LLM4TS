import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Same;
import org.hamcrest.Description;

public class SameTest {

    @Test
    public void testMatches_true() {
        Same same = new Same("test");
        assertTrue(same.matches("test"));
    }

    @Test
    public void testMatches_false() {
        Same same = new Same("test");
        assertFalse(same.matches("mockito"));
    }

    @Test
    public void testDescribeTo_String() {
        Same same = new Same("test");
        Description description = new Description();
        same.describeTo(description);
        assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void testDescribeTo_Character() {
        Same same = new Same('a');
        Description description = new Description();
        same.describeTo(description);
        assertEquals("same('a')", description.toString());
    }

    @Test
    public void testDescribeTo_Null() {
        Same same = new Same(null);
        Description description = new Description();
        same.describeTo(description);
        assertEquals("same(null)", description.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testMatches_Null() {
        Same same = new Same("test");
        same.matches(null);
    }

}