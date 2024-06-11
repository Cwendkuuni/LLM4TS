import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WithMatchingMatchersAndArguments() {
        Matcher<Integer> matcher1 = CoreMatchers.is(1); // Matching matcher
        Matcher<Integer> matcher2 = CoreMatchers.is(2); // Matching matcher
        Object[] arguments = {1, 2};
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher1);
        matchers.add(matcher2);

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WithNotMatchingArguments() {
        Matcher<Integer> matcher1 = CoreMatchers.is(1); // Matching matcher
        Matcher<Integer> matcher2 = CoreMatchers.is(2); // Matching matcher
        Object[] arguments = {3, 4}; // Not matching arguments
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher1);
        matchers.add(matcher2);

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(2, result.length);
        assertEquals(0, result[0].intValue());
        assertEquals(1, result[1].intValue());
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WithDifferentMatchersAndArguments() {
        Matcher<Integer> matcher1 = CoreMatchers.is(1); // Matching matcher
        Matcher<String> matcher2 = CoreMatchers.containsString("test"); // Different type matcher
        Object[] arguments = {1, "test"};
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher1);
        matchers.add(matcher2);

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(1, result.length);
        assertEquals(1, result[0].intValue());
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WithMismatchedMatchersAndArguments() {
        Matcher<Integer> matcher1 = CoreMatchers.is(1); // Matching matcher
        Matcher<String> matcher2 = CoreMatchers.containsString("test"); // Different type matcher
        Object[] arguments = {2, "wrong"};
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher1);
        matchers.add(matcher2);

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(2, result.length);
        assertEquals(0, result[0].intValue());
        assertEquals(1, result[1].intValue());
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WithEmptyMatchers() {
        Object[] arguments = {1}; // Single argument
        List<Matcher> matchers = new LinkedList<>(); // Empty list of matchers

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WithMismatchedSizes() {
        Matcher<Integer> matcher1 = CoreMatchers.is(1); // Matching matcher
        Object[] arguments = {1, 2};
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher1);

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WithExceptionInMatcher() {
        Matcher<Integer> matcher1 = CoreMatchers.is(1); // Matching matcher
        Object[] arguments = {null}; // Null argument
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher1);

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }
}