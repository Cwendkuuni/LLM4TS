import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.matchers.Same;
import org.mockito.ArgumentMatcher;
import org.hamcrest.Description;

public class SameTest {

    @Test
    public void testMatches() {
        Object obj = new Object();
        Same same = new Same(obj);
        assertTrue(same.matches(obj));
    }

    @Test
    public void testDescribeToString() {
        Object str = "test";
        Same same = new Same(str);
        Description description = new Description();
        same.describeTo(description);
        assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void testDescribeToChar() {
        Object chr = 't';
        Same same = new Same(chr);
        Description description = new Description();
        same.describeTo(description);
        assertEquals("same('t')", description.toString());
    }

    // Edge Cases and Exception Handling Scenarios

    @Test
    public void testMatchesNullObject() {
        Same same = new Same(null);
        assertTrue(same.matches(null));
    }

    @Test
    public void testMatchesDifferentObjects() {
        Object obj = new Object();
        Same same = new Same(obj);
        assertFalse(same.matches(new Object()));
    }

    @Test
    public void testDescribeToDifferentTypes() {
        Object obj = new Object();
        Same same = new Same(obj);
        Description description = new Description();
        same.describeTo(description);
        assertNotEquals("same()", description.toString());
    }   
}