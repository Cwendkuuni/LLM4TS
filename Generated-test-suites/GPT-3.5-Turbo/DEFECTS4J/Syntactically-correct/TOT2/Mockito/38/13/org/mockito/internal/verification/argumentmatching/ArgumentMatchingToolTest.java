import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.hamcrest.Matcher;

import java.util.List;
import java.util.LinkedList;

public class ArgumentMatchingToolTest {

    ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        Matcher<String> matcher1 = mock(Matcher.class);
        Matcher<Integer> matcher2 = mock(Matcher.class);

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher1);
        matchers.add(matcher2);

        Object[] arguments = { "arg1", 2 };

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(1, result.length);
        assertEquals(1, result[0].intValue());
    }

    @Test
    public void testSafelyMatches() {
        Matcher<String> matcher = mock(Matcher.class);

        assertTrue(argumentMatchingTool.safelyMatches(matcher, "test"));
    }

    @Test
    public void testToStringEquals() {
        Matcher<String> matcher = mock(Matcher.class);

        assertTrue(argumentMatchingTool.toStringEquals(matcher, "test"));
    }
}