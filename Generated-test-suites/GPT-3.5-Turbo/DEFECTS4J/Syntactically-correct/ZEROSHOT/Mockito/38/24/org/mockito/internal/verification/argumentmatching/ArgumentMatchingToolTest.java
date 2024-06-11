import org.hamcrest.*;
import org.junit.Before;
import org.junit.Test;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setUp() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsDifferentSize() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(CoreMatchers.isA(Integer.class));
        matchers.add(CoreMatchers.isA(String.class));

        Object[] arguments = new Object[]{1, "two", 3.0};

        Integer[] expected = new Integer[0];
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousArguments() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(CoreMatchers.isA(Integer.class));
        matchers.add(CoreMatchers.isA(String.class));

        Object[] arguments = new Object[]{1, "two"};

        Integer[] expected = new Integer[0];
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsSuspiciousArgument() {
        Matcher<Integer> mockMatcher = new ContainsExtraTypeInformation<>(Integer.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mockMatcher);

        Object[] arguments = new Object[]{new Double(1.0)};

        Integer[] expected = new Integer[]{0};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testSafelyMatches_successfulMatch() {
        Matcher<Integer> mockMatcher = CoreMatchers.isA(Integer.class);
        Object arg = 1;

        boolean result = argumentMatchingTool.safelyMatches(mockMatcher, arg);

        assertTrue(result);
    }

    @Test
    public void testSafelyMatches_exceptionThrown() {
        Matcher<Integer> mockMatcher = new CustomMatcher<Integer>("mockMatcher") {
            @Override
            public boolean matches(Object item) {
                throw new RuntimeException("Mock Matcher Exception");
            }
        };
        Object arg = 1;

        boolean result = argumentMatchingTool.safelyMatches(mockMatcher, arg);

        assertFalse(result);
    }

    @Test
    public void testToStringEquals_equalToStrings() {
        Matcher<Integer> mockMatcher = CoreMatchers.isA(Integer.class);
        Object arg = 1;

        boolean result = argumentMatchingTool.toStringEquals(mockMatcher, arg);

        assertTrue(result);
    }

    @Test
    public void testToStringEquals_differentToStrings() {
        Matcher<Integer> mockMatcher = CoreMatchers.isA(Integer.class);
        Object arg = "1";

        boolean result = argumentMatchingTool.toStringEquals(mockMatcher, arg);

        assertFalse(result);
    }
}