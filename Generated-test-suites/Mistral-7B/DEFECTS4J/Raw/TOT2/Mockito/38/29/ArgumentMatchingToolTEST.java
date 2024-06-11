import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.Arrays;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyList() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList();
        Object[] arguments = new Object[0];
        Integer[] expected = new Integer[0];
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentSize() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("expected"), new ContainsExtraTypeInformation("expected"));
        Object[] arguments = new Object[]{"arg1", "arg2"};
        Integer[] expected = new Integer[0];
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatcher() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(null);
        Object[] arguments = new Object[]{"arg1", "arg2"};
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArgument() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("expected"));
        Object[] arguments = new Object[]{null};
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchingArgs() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("expectedArg"), new ContainsExtraTypeInformation("expectedArg"));
        Object[] arguments = new Object[]{"expectedArg", "expectedArg"};
        Integer[] expected = new Integer[0];
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NotMatchingArgs() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("expectedArg"), new ContainsExtraTypeInformation("differentTypeArg"));
        Object[] arguments = new Object[]{"expectedArg", new Object()};
        Integer[] expected = new Integer[]{0};
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = RuntimeException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_StringMatcher() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(new ContainsString("expectedString"));
        Object[] arguments = new Object[]{"expectedString"};
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }
}