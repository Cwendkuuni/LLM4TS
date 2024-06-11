import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(ContainsExtraTypeInformation.class));
        Object[] arguments = new Object[] { "argument" };

        Integer[] suspiciousIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(1, suspiciousIndexes.length);
        assertEquals(0, (int) suspiciousIndexes[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithMismatch() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(ContainsExtraTypeInformation.class));
        Object[] arguments = new Object[] { 123 };

        Integer[] suspiciousIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, suspiciousIndexes.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithNullPointerException() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(ContainsExtraTypeInformation.class));
        Object[] arguments = new Object[] { null };

        Integer[] suspiciousIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, suspiciousIndexes.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithSizeMismatch() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(ContainsExtraTypeInformation.class));
        Object[] arguments = new Object[] { "argument1", "argument2" };

        Integer[] suspiciousIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, suspiciousIndexes.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithEmptyMatchers() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[] { "argument" };

        Integer[] suspiciousIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, suspiciousIndexes.length);
    }
}

