import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testMatches() {
        Same same = new Same("test");
        assertTrue(same.matches("test"));
        assertFalse(same.matches("mockito"));
    }

    @Test
    public void testDescribeTo() {
        Same same = new Same("description");
        Description description = new StringDescription();
        same.describeTo(description);
        assertEquals("same(\"description\")", description.toString());
    }

    @Test
    public void testAppendQuotingString() {
        Same same = new Same("string");
        Description description = new StringDescription();
        same.appendQuoting(description);
        assertEquals("\"", description.toString());
    }

    @Test
    public void testAppendQuotingCharacter() {
        Same same = new Same('c');
        Description description = new StringDescription();
        same.appendQuoting(description);
        assertEquals("'", description.toString());
    }

    @Test
    public void testAppendQuotingOther() {
        Same same = new Same(123);
        Description description = new StringDescription();
        same.appendQuoting(description);
        assertEquals("", description.toString());
    }

}