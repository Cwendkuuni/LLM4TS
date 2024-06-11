package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.matchers.Arguments.not;
import static org.mockito.internal.matchers.Arguments.same;
import static org.mockito.internal.matchers.Arguments.type;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyListAndArray() {
        List<Matcher> matchers = Arrays.asList();
        Object[] arguments = new Object[0];

        Integer[] expected = new Integer[0];

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_mismatchedSizes() {
        List<Matcher> matchers = Arrays.asList(same(1));
        Object[] arguments = new Object[2];

        Integer[] expected = new Integer[0];

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousMatches() {
        List<Matcher> matchers = Arrays.asList(same(1), same("foo"), same(1.0f));
        Object[] arguments = new Object[]{1, "foo", 1.0f};

        Integer[] expected = new Integer[0];

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_oneSuspiciousMatch() {
        List<Matcher> matchers = Arrays.asList(
                same(Integer.class),
                same(1),
                not(same(Double.class))
        );
        Object[] arguments = new Object[]{1, 1.0};

        Integer[] expected = new Integer[]{1};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleSuspiciousMatches() {
        List<Matcher> matchers = Arrays.asList(
                same(Integer.class),
                not(same(Double.class)),
                same(Integer.class),
                not(same(String.class))
        );
        Object[] arguments = new Object[]{1, 1.0, 2, "foo"};

        Integer[] expected = new Integer[]{1, 3};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToStringEquals_sameToString() {
        List<Matcher> matchers = Arrays.asList(same("foo"));
        Object[] arguments = new Object[]{"foo"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean actual = tool.toStringEquals(matchers.get(0), arguments[0]);

        assertTrue(actual);
    }

    @Test
    public void testToStringEquals_differentToString() {
        List<Matcher> matchers = Arrays.asList(same("foo"));
        Object[] arguments = new Object[]{"bar"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean actual = tool.toStringEquals(matchers.get(0), arguments[0]);

        assertFalse(actual);
    }

    @Test
    public void testSafelyMatches_matches() {
        List<Matcher> matchers = Arrays.asList(same(Integer.class));
        Object[] arguments = new Object[]{1};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean actual = tool.safelyMatches(matchers.get(0), arguments[0]);

        assertTrue(actual);
    }

    @Test
    public void testSafelyMatches_doesNotMatch() {
        List<Matcher> matchers = Arrays.asList(same(Integer.class));
        Object[] arguments = new Object[]{1.0};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean actual = tool.safelyMatches(matchers.get(0), arguments[0]);

        assertFalse(actual);
    }

    @Test
    public void testSafelyMatches_throwsException() {
        List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation<String>("String") {
            @Override
            public boolean typeMatches(Object argument) {
                throw new RuntimeException("Unexpected error");
            }
        });
        Object[] arguments = new Object[]{"foo"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        assertFalse(tool.safelyMatches(matchers.get(0), arguments[0]));
    }
}