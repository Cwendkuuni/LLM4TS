import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matcherSizeNotEqualArgumentsLength_returnsEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new DummyMatcher());
        Object[] arguments = new Object[2];

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationAndSafelyMatchesAndToStringEqualsAndTypeNotMatches_returnsIndex() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[1];
        arguments[0] = "dummy";

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[]{0}, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationAndSafelyMatchesAndToStringEqualsAndTypeMatches_returnsEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[1];
        arguments[0] = 10;

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testSafelyMatches_matchingMatcherAndArg_returnsTrue() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.safelyMatches(new DummyMatcher(), "dummy");

        Assert.assertTrue(result);
    }

    @Test
    public void testSafelyMatches_nonMatchingMatcherAndArg_returnsFalse() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.safelyMatches(new DummyMatcher(), "non-matching");

        Assert.assertFalse(result);
    }

    @Test
    public void testToStringEquals_matchingMatcherAndArg_returnsTrue() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.toStringEquals(new DummyMatcher(), "dummy");

        Assert.assertTrue(result);
    }

    @Test
    public void testToStringEquals_nonMatchingMatcherAndArg_returnsFalse() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.toStringEquals(new DummyMatcher(), "non-matching");

        Assert.assertFalse(result);
    }

    private static class DummyMatcher implements Matcher {

        @Override
        public boolean matches(Object o) {
            return o.equals("dummy");
        }

        @Override
        public void describeMismatch(Object item, Description mismatchDescription) {

        }

        @Override
        public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

        }

        @Override
        public void describeTo(Description description) {

        }
    }
}