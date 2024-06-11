import org.junit.Test;
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.Same;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SameTest {

    @Test
    public void testMatches() {
        Object wanted = new Object();
        Same same = new Same(wanted);

        Object actual1 = wanted;
        Object actual2 = new Object();

        assertThat(same.matches(actual1)).isTrue();
        assertThat(same.matches(actual2)).isFalse();
    }

    @Test
    public void testDescribeTo_withString() {
        Object wanted = "test";
        Same same = new Same(wanted);
        Description description = mock(Description.class);

        same.describeTo(description);

        verify(description).appendText("same(\"test\")");
    }

    @Test
    public void testDescribeTo_withCharacter() {
        Object wanted = 'a';
        Same same = new Same(wanted);
        Description description = mock(Description.class);

        same.describeTo(description);

        verify(description).appendText("same('a')");
    }

    @Test
    public void testDescribeTo_withNonStringNonCharacter() {
        Object wanted = 123;
        Same same = new Same(wanted);
        Description description = mock(Description.class);

        same.describeTo(description);

        verify(description).appendText("same(123)");
    }
}