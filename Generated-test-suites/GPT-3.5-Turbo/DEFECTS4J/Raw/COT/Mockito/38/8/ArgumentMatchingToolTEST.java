
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyMatchers_emptyArguments() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[0];

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matcherSizeNotEqualToArgumentsSize() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[1];

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_allMatchersMatchArguments() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[1];
        arguments[0] = new ContainsExtraTypeInformation();

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsSuspiciousArguments() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[1];
        arguments[0] = "SuspiciousArgument";

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(1, result.length);
        Assert.assertEquals(0, result[0].intValue());
    }
}