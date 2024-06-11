import org.junit.Test;
import org.mockito.internal.matchers.Same;
import org.hamcrest.Description;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.internal.hamcrest.HamcrestHelpers.mockDescription;

public class SameTest {

    @Test
    public void testMatches() {
        Same same = new Same("test");
        assertTrue(same.matches("test"));
        assertFalse(same.matches("not test"));
    }

    @Test
    public void testDescribeTo() {
        Same same = new Same("test");
        Description description = mockDescription();
        same.describeTo(description);
        assertTrue(description.toString().contains("same(\"test\")"));
    }

    @Test
    public void testAppendQuoting() {
        Same sameString = new Same("string");
        Description descriptionString = mockDescription();
        sameString.appendQuoting(descriptionString);
        assertTrue(descriptionString.toString().equals("\""));

        Same sameChar = new Same('c');
        Description descriptionChar = mockDescription();
        sameChar.appendQuoting(descriptionChar);
        assertTrue(descriptionChar.toString().equals("'"));
    }
}