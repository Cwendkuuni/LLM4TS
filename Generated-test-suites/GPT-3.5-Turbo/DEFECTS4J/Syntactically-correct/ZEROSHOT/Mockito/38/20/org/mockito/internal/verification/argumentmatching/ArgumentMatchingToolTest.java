import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        // Test case 1: matchers size is not equal to arguments length
        List<Matcher> matchers1 = new LinkedList<>();
        Object[] arguments1 = {};
        Integer[] expected1 = {};
        Integer[] result1 = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers1, arguments1);
        Assert.assertArrayEquals(expected1, result1);

        // Test case 2: matchers size is equal to arguments length
        List<Matcher> matchers2 = new LinkedList<>();
        matchers2.add(CoreMatchers.is(5));
        matchers2.add(CoreMatchers.is("hello"));
        matchers2.add(CoreMatchers.is(true));

        Object[] arguments2 = {5, "hello", false};
        Integer[] expected2 = {2};
        Integer[] result2 = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers2, arguments2);
        Assert.assertArrayEquals(expected2, result2);
    }

    @Test
    public void testSafelyMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        // Test case 1: Matcher matches argument
        Matcher matcher1 = CoreMatchers.is(5);
        Object argument1 = 5;
        boolean result1 = tool.safelyMatches(matcher1, argument1);
        Assert.assertTrue(result1);

        // Test case 2: Matcher does not match argument
        Matcher matcher2 = CoreMatchers.is("hello");
        Object argument2 = 5;
        boolean result2 = tool.safelyMatches(matcher2, argument2);
        Assert.assertFalse(result2);
    }

    @Test
    public void testToStringEquals() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        // Test case 1: Matcher toString() is equal to argument toString()
        Matcher matcher1 = CoreMatchers.is(5);
        Object argument1 = 5;
        boolean result1 = tool.toStringEquals(matcher1, argument1);
        Assert.assertTrue(result1);

        // Test case 2: Matcher toString() is not equal to argument toString()
        Matcher matcher2 = CoreMatchers.is(5);
        Object argument2 = "hello";
        boolean result2 = tool.toStringEquals(matcher2, argument2);
        Assert.assertFalse(result2);
    }
}