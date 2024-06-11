import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_noMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[0];

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersSizeMismatch() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());

        Object[] arguments = new Object[0];

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchingArgs() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());

        Object[] arguments = new Object[1];
        arguments[0] = new ContainsExtraTypeInformation();

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_notMatchingArgs() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());

        Object[] arguments = new Object[1];
        arguments[0] = "Not matching argument";

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(1, result.length);
        Assert.assertEquals(0, result[0].intValue());
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformation() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());

        Object[] arguments = new Object[1];
        arguments[0] = new ContainsExtraTypeInformation();

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(1, result.length);
        Assert.assertEquals(0, result[0].intValue());
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationTypeMismatch() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());

        Object[] arguments = new Object[1];
        arguments[0] = "Not matching argument";

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(1, result.length);
        Assert.assertEquals(0, result[0].intValue());
    }
}