import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.internal.matchers.Same;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;

public class SameTest {

    @Test
    public void testMatches() {
        Object obj = new Object();
        Same sameMatcher = new Same(obj);
        assertTrue(sameMatcher.matches(obj));
        assertFalse(sameMatcher.matches(new Object()));
    }

    @Test
    public void testDescribeTo() {
        Object obj = "test";
        Same sameMatcher = new Same(obj);
        Description description = mock(Description.class);
        sameMatcher.describeTo(description);
        // Check if correct description is appended
        // This is a basic test case, additional edge cases and exception handling can be added
    }

    @Test
    public void testAppendQuoting() {
        Same same = new Same("test");
        Description description1 = mock(Description.class);
        Description description2 = mock(Description.class);

        same.appendQuoting(description1);
        same.appendQuoting(description2);

        // Check if correct quoting is appended based on instance type
        // This is a basic test case, additional edge cases and exception handling can be added
    }

}