import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.Arrays;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsHaveSameSize() {
        List<Matcher> matchers = Arrays.asList(
                new ContainsExtraTypeInformation(Integer.class, "foo"),
                new ContainsExtraTypeInformation(Integer.class, "bar")
        );
        Object[] arguments = new Object[]{1, 2};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[0], result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_suspiciousArgument() {
        List<Matcher> matchers = Arrays.asList(
                new ContainsExtraTypeInformation(Integer.class, "foo"),
                new ContainsExtraTypeInformation(String.class, "bar")
        );
        Object[] arguments = new Object[]{1, "two"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[]{1}, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousArguments() {
        List<Matcher> matchers = Arrays.asList(
                new ContainsExtraTypeInformation(Integer.class, "foo"),
                new ContainsExtraTypeInformation(Integer.class, "foo")
        );
        Object[] arguments = new Object[]{1, 1};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[0], result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsDifferentSizes() {
        List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation(Integer.class, "foo"));
        Object[] arguments = new Object[]{1, 2, 3};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }
}