import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

public class SameTest {

    @Test
    public void testMatches() {
        Same same = new Same("hello");
        boolean result = same.matches("hello");
        assertEquals(true, result);
    }

    @Test
    public void testMatchesDifferent() {
        Same same = new Same("hello");
        boolean result = same.matches("world");
        assertEquals(false, result);
    }

    @Test
    public void testDescribeTo() {
        Same same = new Same("hello");
        Description description = mock(Description.class);
        same.describeTo(description);
        verify(description).appendText("same(\"hello\")");
    }

    @Test
    public void testDescribeToCharacter() {
        Same same = new Same('a');
        Description description = mock(Description.class);
        same.describeTo(description);
        verify(description).appendText("same('a')");
    }

    @Test
    public void testMatchesNull() {
        Same same = new Same(null);
        boolean result = same.matches(null);
        assertEquals(true, result);
    }

    @Test
    public void testMatchesNullDifferent() {
        Same same = new Same(null);
        boolean result = same.matches("hello");
        assertEquals(false, result);
    }

}