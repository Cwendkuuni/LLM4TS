import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        // Typical use case
        List<Matcher> matchers = List.of(is(1), any(String.class), containsString("test"));
        Object[] arguments = new Object[]{1, "mockito", "testing"};
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);

        // Edge case: matchers and arguments length mismatch
        matchers = List.of(is(1), any(String.class));
        arguments = new Object[]{1, "mockito", "testing"};
        result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);

        // Edge case: type mismatch
        matchers = List.of(is(1), any(String.class), containsString("test"));
        arguments = new Object[]{1, 123, "testing"};
        result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(new Integer[]{1}, result);

        // Error scenario: matcher throwing exception
        matchers = List.of(is(1), any(String.class), errorMatcher());
        arguments = new Object[]{1, "mockito", "testing"};
        result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
    }

    private Matcher errorMatcher() {
        return new Matcher() {
            @Override
            public boolean matches(Object item) {
                throw new RuntimeException("Error in matcher");
            }

            @Override
            public void describeTo(org.hamcrest.Description description) {}

            @Override
            public void describeMismatch(Object item, org.hamcrest.Description mismatchDescription) {}
        };
    }
}