import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(
            mock(Matcher.class),
            mock(Matcher.class),
            mock(Matcher.class)
        );
        Object[] arguments = { "arg1", "arg2", "arg3" };

        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, indexes.length);
    }

    @Test
    public void testSafelyMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Matcher mockMatcher = mock(Matcher.class);
        Object argument = "arg";

        boolean result = tool.safelyMatches(mockMatcher, argument);

        assertTrue(result);
    }

    @Test
    public void testToStringEquals() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Matcher mockMatcher = mock(Matcher.class);
        Object argument = "arg";

        boolean result = tool.toStringEquals(mockMatcher, argument);

        assertFalse(result);
    }
}