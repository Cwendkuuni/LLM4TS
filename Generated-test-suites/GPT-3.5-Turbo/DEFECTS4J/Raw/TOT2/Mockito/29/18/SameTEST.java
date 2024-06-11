import org.junit.Test;
import org.mockito.internal.matchers.Same;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.SameTest.Is;
import static org.mockito.internal.matchers.SameTest.IsNot;

public class SameTest {

    @Test
    public void testMatches_True() {
        Object obj = new Object();
        Same same = new Same(obj);
        assertTrue(same.matches(obj));
    }

    @Test
    public void testMatches_False() {
        Same same = new Same("mockito");
        assertFalse(same.matches("anotherString"));
    }

    @Test
    public void testDescribeTo_String() {
        Same same = new Same("mockito");
        Description description = new StringDescription();
        same.describeTo(description);
        assertEquals("same(\"mockito\")", description.toString());
    }

    @Test
    public void testDescribeTo_Character() {
        Same same = new Same('a');
        Description description = new StringDescription();
        same.describeTo(description);
        assertEquals("same('a')", description.toString());
    }

    @Test
    public void testDescribeTo_Null() {
        Same same = new Same(null);
        Description description = new StringDescription();
        same.describeTo(description);
        assertEquals("same(null)", description.toString());
    }
  
    @Test
    public void testMatches_Exception() {
        Same same = new Same("mockito");
        try {
            same.matches(null);
            fail("Expected NullPointerException not thrown.");
        } catch (NullPointerException e) {
            // success
        }
    }
}