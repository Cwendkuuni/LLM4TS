import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

import java.util.*;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool tool;

    @Before
    public void setUp() {
        tool = new ArgumentMatchingTool();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(CoreMatchers.is("test"));
        matchers.add(CoreMatchers.is(123));

        Object[] arguments = {"test", 123};
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithMismatch() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(CoreMatchers.is("test"));
        matchers.add(CoreMatchers.is(123));

        Object[] arguments = {"wrong", 123};
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithException() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(CoreMatchers.is("test"));
        matchers.add(CoreMatchers.is(123));

        Object[] arguments = {"test", new Object()};
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }
}