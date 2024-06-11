package org.mockito.internal.verification.argumentmatching;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.internal.arguments.ArgumentMatchingToolPrivateMethods.safelyMatches;
import static org.mockito.internal.arguments.ArgumentMatchingToolPrivateMethods.toStringEquals;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyMatchersAndArguments() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[]{};

        Integer[] expected = new Integer[]{};
        Integer[] actual = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyMatchersAndNonEmptyArguments() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[]{"arg1", "arg2"};

        Integer[] expected = new Integer[]{};
        Integer[] actual = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndEmptyArguments() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Matcher.any());
        matchers.add(Matcher.any());

        Object[] arguments = new Object[]{};

        Integer[] expected = new Integer[]{};
        Integer[] actual = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsWithDifferentSize() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Matcher.any());
        matchers.add(Matcher.any());

        Object[] arguments = new Object[]{"arg1"};

        Integer[] expected = new Integer[]{};
        Integer[] actual = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_allArgumentsMatch() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Matcher.any());
        matchers.add(Matcher.any());

        Object[] arguments = new Object[]{"arg1", "arg2"};

        Integer[] expected = new Integer[]{};
        Integer[] actual = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_someArgumentsSuspicious() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Matcher.any());
        matchers.add(Matcher.any());

        Object[] arguments = new Object[]{"arg1", 2};

        Integer[] expected = new Integer[]{1};
        Integer[] actual = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_allArgumentsSuspicious() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Matcher.any());
        matchers.add(Matcher.any());

        Object[] arguments = new Object[]{1, 2};

        Integer[] expected = new Integer[]{0, 1};
        Integer[] actual = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformation() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Matcher.any());
        matchers.add(Matcher.containsExtraTypeInformation(String.class));

        Object[] arguments = new Object[]{"arg1", "arg2"};

        Integer[] expected = new Integer[]{1};
        Integer[] actual = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    // helper methods are correctly tested in ArgumentMatchingToolPrivateMethodsTest

}