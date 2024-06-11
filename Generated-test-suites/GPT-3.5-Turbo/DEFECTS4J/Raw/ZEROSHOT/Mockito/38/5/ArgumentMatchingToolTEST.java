import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {
    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setUp() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyMatchersAndArguments_emptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[]{};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersSizeNotEqualArgumentsSize_emptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationAndNotSafelyMatchesAndToStringEqualsAndNotTypeMatches_singleResult() {
        List<Matcher> matchers = new LinkedList<>();
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        matchers.add(matcher);
        Object[] arguments = new Object[]{new Object()};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(1, result.length);
        assertEquals(0, (int) result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationAndSafelyMatches_singleResult() {
        List<Matcher> matchers = new LinkedList<>();
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        matchers.add(matcher);
        Object[] arguments = new Object[]{matcher};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationAndNotToStringEquals_singleResult() {
        List<Matcher> matchers = new LinkedList<>();
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        matchers.add(matcher);
        Object[] arguments = new Object[]{"different"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(1, result.length);
        assertEquals(0, (int) result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationAndToStringEqualsAndNotTypeMatches_singleResult() {
        List<Matcher> matchers = new LinkedList<>();
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        matchers.add(matcher);
        Object[] arguments = new Object[]{1};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(1, result.length);
        assertEquals(0, (int) result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationAndToStringEqualsAndTypeMatches_emptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        matchers.add(matcher);
        Object[] arguments = new Object[]{1};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(1, result.length);
        assertEquals(0, (int) result[0]);
    }

    @Test
    public void testSafelyMatches_matcherThrowsException_false() {
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();

        boolean result = argumentMatchingTool.safelyMatches(matcher, new Object());

        assertFalse(result);
    }

    @Test
    public void testSafelyMatches_matcherDoesNotThrowException_true() {
        Matcher matcher = new Matcher() {
            @Override
            public boolean matches(Object arg) {
                return true;
            }
        };

        boolean result = argumentMatchingTool.safelyMatches(matcher, new Object());

        assertTrue(result);
    }

    @Test
    public void testToStringEquals_matcherToStringEqualsArg_true() {
        Matcher matcher = new Matcher() {
            @Override
            public boolean matches(Object arg) {
                return false;
            }

            @Override
            public String toString() {
                return "matcher";
            }
        };
        Object arg = "matcher";

        boolean result = argumentMatchingTool.toStringEquals(matcher, arg);

        assertTrue(result);
    }

    @Test
    public void testToStringEquals_matcherToStringNotEqualsArg_false() {
        Matcher matcher = new Matcher() {
            @Override
            public boolean matches(Object arg) {
                return false;
            }

            @Override
            public String toString() {
                return "matcher";
            }
        };
        Object arg = "different";

        boolean result = argumentMatchingTool.toStringEquals(matcher, arg);

        assertFalse(result);
    }
}