import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SameTest {

    @Mock
    private Description description;

    @Test
    public void testMatchesWithSameObject() {
        Same same = new Same("test");

        boolean result = same.matches("test");

        assertThat(result, is(true));
    }

    @Test
    public void testMatchesWithDifferentObject() {
        Same same = new Same("test");

        boolean result = same.matches("different");

        assertThat(result, is(false));
    }

    @Test
    public void testDescribeToWithString() {
        Same same = new Same("test");

        same.describeTo(description);

        verify(description).appendText("same(\"test\")");
    }

    @Test
    public void testDescribeToWithCharacter() {
        Same same = new Same('c');

        same.describeTo(description);

        verify(description).appendText("same('c')");
    }

    @Test
    public void testDescribeToWithOtherObject() {
        Same same = new Same(10);

        same.describeTo(description);

        verify(description).appendText("same(10)");
    }
}