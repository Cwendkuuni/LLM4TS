import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        // Test case 1: All matchers and arguments are the same
        List<Matcher> matchers1 = new LinkedList<>();
        matchers1.add(new ContainsExtraTypeInformation());
        matchers1.add(new ContainsExtraTypeInformation());
        matchers1.add(new ContainsExtraTypeInformation());
        Object[] arguments1 = new Object[]{"test", "test", "test"};
        Integer[] expected1 = new Integer[0];
        Integer[] result1 = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers1, arguments1);
        Assert.assertArrayEquals(expected1, result1);

        // Test case 2: All matchers and arguments are different
        List<Matcher> matchers2 = new LinkedList<>();
        matchers2.add(new ContainsExtraTypeInformation());
        matchers2.add(new ContainsExtraTypeInformation());
        matchers2.add(new ContainsExtraTypeInformation());
        Object[] arguments2 = new Object[]{"test1", "test2", "test3"};
        Integer[] expected2 = new Integer[]{0, 1, 2};
        Integer[] result2 = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers2, arguments2);
        Assert.assertArrayEquals(expected2, result2);

        // Test case 3: Some matchers and arguments are the same, others are different
        List<Matcher> matchers3 = new LinkedList<>();
        matchers3.add(new ContainsExtraTypeInformation());
        matchers3.add(new ContainsExtraTypeInformation());
        matchers3.add(new ContainsExtraTypeInformation());
        Object[] arguments3 = new Object[]{"test", "test2", "test"};
        Integer[] expected3 = new Integer[]{1};
        Integer[] result3 = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers3, arguments3);
        Assert.assertArrayEquals(expected3, result3);
    }
}