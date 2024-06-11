import org.junit.Test;
import org.hamcrest.Description;
import org.mockito.internal.matchers.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;

public class SameTest {

    @Test
    public void testMatches() {
        Same same = new Same("example");
        assertTrue(same.matches("example"));
        assertFalse(same.matches("different"));
        assertFalse(same.matches(null));
    }

    @Test
    public void testDescribeTo() {
        Same same = new Same("example");
        Description description = Description.createTestDescription(SameTest.class, "testDescribeTo");
        same.describeTo(description);
        assertEquals("same(\"example\")", description.toString());
    }

    @Test
    public void testAppendQuoting_string() {
        Same same = new Same("example");
        Description description = Description.createTestDescription(SameTest.class, "testAppendQuoting_string");
        same.appendQuoting(description);
        assertEquals("\"", description.toString());
    }

    @Test
    public void testAppendQuoting_character() {
        Same same = new Same('c');
        Description description = Description.createTestDescription(SameTest.class, "testAppendQuoting_character");
        same.appendQuoting(description);
        assertEquals("'", description.toString());
    }

}