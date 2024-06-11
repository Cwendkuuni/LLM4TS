

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.internal.matchers.ContainsExtraTypeInformation.containsExtraTypeInfo;
import static org.mockito.internal.util.ArgumentMatcherStorage.CONSTANT_MATCHER;
import static org.mockito.internal.util.ArgumentMatcherStorage.intThat;
import static org.mockito.internal.util.MockUtil.createMock;


public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setUp() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_noMatchersAndArguments() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[]{};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[0], result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_noMatchersWithArguments() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[]{"arg1", "arg2"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[0], result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersSizeNotEqualArgumentsSize() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(intThat(Matchers.is(5)));
        Object[] arguments = new Object[]{"arg1", "arg2"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[0], result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_suspiciouslyNotMatchingArgs() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(intThat(Matchers.is(5)));
        matchers.add(containsExtraTypeInfo("extraType1"));
        Object[] arguments = new Object[]{5, "notMatching"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[]{1}, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_safelyMatchesThrowsThrowable() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(CONSTANT_MATCHER);
        Object[] arguments = new Object[]{"arg1"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[0], result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_toStringNotEqual() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(intThat(Matchers.is(5)));
        matchers.add(containsExtraTypeInfo("extraType1"));
        Object[] arguments = new Object[]{5, "notMatching"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[]{1}, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_toStringEquals() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(intThat(Matchers.is(5)));
        matchers.add(containsExtraTypeInfo("extraType1"));
        Object[] arguments = new Object[]{5, "extraType1"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[]{}, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_typeMatches() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(intThat(Matchers.is(5)));
        matchers.add(containsExtraTypeInfo("extraType1"));
        Object[] arguments = new Object[]{5, "extraType2"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[]{}, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsNullArgument() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(createMock(Matcher.class));
        Object[] arguments = new Object[]{null};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[0], result);
    }
}
