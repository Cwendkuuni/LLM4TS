import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.io.Serializable;

import static org.junit.Assert.*;

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

        Description description = new Description();
        same.describeTo(description);

        assertEquals("same(" + wanted.toString() + ")", description.toString());
    }
}