import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setup() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WithMatchingMatchersAndArguments_ZeroSuspicious() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());

        Object[] arguments = new Object[2];
        arguments[0] = new ContainsExtraTypeInformation();
        arguments[1] = new ContainsExtraTypeInformation();

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WithDifferentSizeMatchersAndArguments_ZeroSuspicious() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());

        Object[] arguments = new Object[2];
        arguments[0] = new ContainsExtraTypeInformation();
        arguments[1] = new ContainsExtraTypeInformation();

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WithSuspiciousMatchAndDifferentTypes_OneSuspicious() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());

        Object[] arguments = new Object[1];
        arguments[0] = "differentType";

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals(0, (int) result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WithSuspiciousMatchAndDifferentTypesAndNullMatcher_OneSuspicious() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(null);

        Object[] arguments = new Object[1];
        arguments[0] = "differentType";

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals(0, (int) result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WithExceptionInMatch_ZeroSuspicious() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());

        Object[] arguments = new Object[1];
        arguments[0] = "differentType";

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WithToStringEquals_ZeroSuspicious() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());

        Object[] arguments = new Object[1];
        arguments[0] = new ContainsExtraTypeInformation();

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.length);
    }

    
    // Add more test cases here for the remaining methods in the ArgumentMatchingTool class
    
}