import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import static org.junit.Assert.*;

public class SameTest {

    private Same same;

    @Before
    public void setUp() {
        same = new Same("test");
    }

    @Test
    public void testMatchesSameObject() {
        assertTrue(same.matches("test"));
    }

    @Test
    public void testMatchesDifferentObject() {
        assertFalse(same.matches("different"));
    }

    @Test
    public void testDescribeToWithString() {
        Description description = new StringDescription();
        same.describeTo(description);
        assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void testDescribeToWithCharacter() {
        Same sameChar = new Same('a');
        Description description = new StringDescription();
        sameChar.describeTo(description);
        assertEquals("same('a')", description.toString());
    }

    @Test
    public void testDescribeToWithOtherObject() {
        Same sameObject = new Same(123);
        Description description = new StringDescription();
        sameObject.describeTo(description);
        assertEquals("same(123)", description.toString());
    }
}