import org.mockito.internal.matchers.Same;
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SameTest {

    @Test
    public void testMatches() {
        Same same = new Same("foo");
        Object actual = "foo";
        boolean result = same.matches(actual);
        assertEquals(true, result);

        actual = "bar";
        result = same.matches(actual);
        assertEquals(false, result);
    }

    @Test
    public void testDescribeTo() {
        Same same = new Same("foo");
        Description description = mock(Description.class);
        same.describeTo(description);

        verify(description).appendText("same(");
        verify(description).appendText("\"");
        verify(description).appendText("foo");
        verify(description).appendText("\"");
        verify(description).appendText(")");
    }
}