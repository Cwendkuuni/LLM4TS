import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.internal.matchers.Same;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SameTest {

    @Test
    public void testMatches() {
        Object wanted = new Object();
        Same same = new Same(wanted);

        assertTrue(same.matches(wanted));
        assertFalse(same.matches(new Object()));
    }

    @Test
    public void testDescribeTo() {
        Object wanted = new Object();
        Same same = new Same(wanted);
        Description description = mock(Description.class);

        same.describeTo(description);

        verify(description).appendText("same(");
        verify(description).appendText(wanted.toString());
        verify(description).appendText(")");
    }

    @Test
    public void testMatches_withArgumentMatcher() {
        Object wanted = new Object();
        Same same = new Same(wanted);

        assertTrue(same.matches(argThat(same)));
        assertFalse(same.matches(argThat(notSame)));
    }

    private Same notSame = new Same(new Object());

}

