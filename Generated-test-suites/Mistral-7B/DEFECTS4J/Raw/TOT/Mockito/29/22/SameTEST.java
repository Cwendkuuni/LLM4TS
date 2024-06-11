import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class SameTest {

    @Test
    public void testSameObject() {
        Object wanted = new Object();
        Object actual = wanted;

        ArgumentMatcher<Object> sameMatcher = new Same(wanted);

        // Test matching same object
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testNotSameObject() {
        Object wanted = new Object();
        Object actual = new Object();

        ArgumentMatcher<Object> sameMatcher = new Same(wanted);

        // Test not matching different objects
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testSameString() {
        String wanted = "mockito";
        String actual = "mockito";

        ArgumentMatcher<String> sameMatcher = new Same(wanted);

        // Test matching same string
        assertThat(sameMatcher, is(instanceOf(Same.class)));
        verify(sameMatcher).matches(actual);
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testNotSameString() {
        String wanted = "mockito";
        String actual = "junit";

        ArgumentMatcher<String> sameMatcher = new Same(wanted);

        // Test not matching different strings
        assertFalse(sameMatcher.matches(actual));
    }

    @Test(expected = AssertionError.class)
    public void testErrorScenario() {
        Object wanted = new Object();
        Object actual = null;

        ArgumentMatcher<Object> sameMatcher = new Same(wanted);

        // Test error scenario: asserting that null is same as an object
        sameMatcher.matches(actual);
    }
}