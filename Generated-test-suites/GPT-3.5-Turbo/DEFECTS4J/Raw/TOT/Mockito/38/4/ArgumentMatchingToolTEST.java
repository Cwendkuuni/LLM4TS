import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_typical() {
        // Typical use case with matching arguments
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(Matchers.anyInt(), Matchers.equalTo("test"));
        Object[] arguments = {5, "test"};
        Integer[] expected = {};
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_typicalMismatchedType() {
        // Typical use case with mismatched types
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(Matchers.anyInt(), Matchers.equalTo("test"));
        Object[] arguments = {5, 10};
        Integer[] expected = {1};
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_typicalTypeMismatch() {
        // Typical use case with type mismatch
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation(Integer.class), Matchers.equalTo("test"));
        Object[] arguments = {5, "test"};
        Integer[] expected = {0};
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_edgeEmptyMatchersList() {
        // Edge case with empty matchers list
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        Object[] arguments = {5, "test"};
        Integer[] expected = {};
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_edgeEmptyArgumentsList() {
        // Edge case with empty arguments list
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(Matchers.anyInt(), Matchers.equalTo("test"));
        Object[] arguments = new Object[0];
        Integer[] expected = {};
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_edgeMismatchedSizes() {
        // Edge case with mismatched sizes of matchers and arguments
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(Matchers.anyInt(), Matchers.equalTo("test"));
        Object[] arguments = {5};
        Integer[] expected = new Integer[0];
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_errorThrowable() {
        // Error scenario with a Throwable during matching
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(Matchers.anyInt(), Matchers.equalTo("test"));
        Object[] arguments = {5, "test"};
        Integer[] expected = {};
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexesError(matchers, arguments);
        Assert.assertArrayEquals(expected, result);
    }

    private Integer[] toolErrorMatchesExecution(Object[] arguments, List<Matcher> matchers, int index) {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> modifiedMatchers = new ArrayList<>(matchers);
        modifiedMatchers.set(index, createErrorMatcher());
        return tool.getSuspiciouslyNotMatchingArgsIndexes(modifiedMatchers, arguments);
    }

    private Matcher createErrorMatcher() {
        return new Matcher() {
            @Override
            public boolean matches(Object o) {
                throw new RuntimeException("Error during matching");
            }

            @Override
            public void describeMismatch(Object item, Description mismatchDescription) {}

            @Override
            public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {}

            @Override
            public void describeTo(Description description) {}
        };
    }

    @Test(expected = RuntimeException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_errorThrowablePropagation() {
        // Error scenario with a Throwable during matching propagating to the test
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(Matchers.anyInt(), Matchers.equalTo("test"));
        Object[] arguments = {5, "test"};
        toolErrorMatchesExecution(arguments, matchers, 0);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_errorToString() {
        // Error scenario with toString() equality check error
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(Matchers.anyInt(), Matchers.equalTo("test"));
        Object[] arguments = {5, "test"};
        Integer[] expected = {};
        Integer[] result = toolErrorToStringMatchesExecution(matchers, arguments, 1);
        Assert.assertArrayEquals(expected, result);
    }

    private Integer[] toolErrorToStringMatchesExecution(List<Matcher> matchers, Object[] arguments, int index) {
        ArgumentMatchingTool tool = new ArgumentMatchingTool() {
            @Override
            private boolean toStringEquals(Matcher m, Object arg) {
                throw new RuntimeException("toString() equality check error");
            }
        };
        return tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    @Test(expected = RuntimeException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_errorToStringPropagation() {
        // Error scenario with toString() equality check error propagating to the test
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(Matchers.anyInt(), Matchers.equalTo("test"));
        Object[] arguments = {5, "test"};
        toolErrorToStringMatchesExecution(matchers, arguments, 1);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_errorTypeMatches() {
        // Error scenario with typeMatches() check error
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation(Integer.class), Matchers.equalTo("test"));
        Object[] arguments = {5, "test"};
        Integer[] expected = {};
        Integer[] result = toolErrorTypeMatchesExecution(matchers, arguments, 0);
        Assert.assertArrayEquals(expected, result);
    }

    private Integer[] toolErrorTypeMatchesExecution(List<Matcher> matchers, Object[] arguments, int index) {
        ArgumentMatchingTool tool = new ArgumentMatchingTool() {
            @Override
            private boolean typeMatches(Matcher m, Object arg) {
                throw new RuntimeException("typeMatches() check error");
            }
        };
        return tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    @Test(expected = RuntimeException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_errorTypeMatchesPropagation() {
        // Error scenario with typeMatches() check error propagating to the test
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation(Integer.class), Matchers.equalTo("test"));
        Object[] arguments = {5, "test"};
        toolErrorTypeMatchesExecution(matchers, arguments, 0);
    }
}