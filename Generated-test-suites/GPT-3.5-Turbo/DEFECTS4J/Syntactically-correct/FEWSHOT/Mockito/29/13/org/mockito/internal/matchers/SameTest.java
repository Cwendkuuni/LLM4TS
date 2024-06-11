import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

public class SameTest {
    @Test
    public void testMatchesWithSameObject() {
        Object object = new Object();
        Same same = new Same(object);

        assertTrue(same.matches(object));
    }

    @Test
    public void testMatchesWithDifferentObject() {
        Object object1 = new Object();
        Object object2 = new Object();
        Same same = new Same(object1);

        assertFalse(same.matches(object2));
    }

    @Test
    public void testDescribeToWithQuotingString() {
        Description description = mock(Description.class);
        Same same = new Same("test");

        same.describeTo(description);

        verify(description).appendText("same(\"test\")");
    }

    @Test
    public void testDescribeToWithQuotingCharacter() {
        Description description = mock(Description.class);
        Same same = new Same('c');

        same.describeTo(description);

        verify(description).appendText("same('c')");
    }

    @Test
    public void testDescribeToWithoutQuoting() {
        Description description = mock(Description.class);
        Object object = new Object();
        Same same = new Same(object);

        same.describeTo(description);

        verify(description).appendText("same(" + object.toString() + ")");
    }

    @Test
    public void testArgumentMatcherWithSameObject() {
        Object object = new Object();
        Same same = new Same(object);

        assertTrue(same.matches(object));
    }

    @Test
    public void testArgumentMatcherWithDifferentObject() {
        Object object1 = new Object();
        Object object2 = new Object();
        Same same = new Same(object1);

        assertFalse(same.matches(object2));
    }
}