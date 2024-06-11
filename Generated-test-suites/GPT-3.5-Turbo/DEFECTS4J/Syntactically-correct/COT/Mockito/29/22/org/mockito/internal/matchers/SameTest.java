
import org.junit.Test;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatches_sameObject_returnsTrue() {
        Same same = new Same("Test");

        assertTrue(same.matches("Test"));
    }

    @Test
    public void testMatches_differentObject_returnsFalse() {
        Same same = new Same("Test");

        assertFalse(same.matches("Different"));
    }

    @Test
    public void testMatches_differentType_returnsFalse() {
        Same same = new Same("Test");

        assertFalse(same.matches(12345));
    }

    @Test
    public void testDescribeTo_stringAppendedWithQuotes() {
        Same same = new Same("Test");
        Description description = new StringDescription();

        same.describeTo(description);

        assertEquals("same(\"Test\")", description.toString());
    }

    @Test
    public void testDescribeTo_characterAppendedWithQuotes() {
        Same same = new Same('T');
        Description description = new StringDescription();

        same.describeTo(description);

        assertEquals("same('T')", description.toString());
    }
}
