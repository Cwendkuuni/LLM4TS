import org.junit.Test;
import org.hamcrest.Description;
import org.mockito.internal.matchers.Same;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class SameTest {

    @Test
    public void testMatches() {
        Same same = new Same("test");
        assertThat("test", argThat(same));
        assertThat("different", not(argThat(same)));
    }

    @Test
    public void testDescribeTo() {
        Description description = mock(Description.class);
        Same same = new Same("test");
        same.describeTo(description);

        verify(description).appendText("same(\"test\")");
    }
}