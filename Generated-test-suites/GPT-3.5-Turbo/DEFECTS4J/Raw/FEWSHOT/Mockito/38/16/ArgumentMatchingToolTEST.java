package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyMatchersAndArguments() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[]{};

        Integer[] expected = new Integer[]{};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyMatchersNonEmptyArguments() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[]{1, 2, 3};

        Integer[] expected = new Integer[]{};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_nonEmptyMatchersEmptyArguments() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        Object[] arguments = new Object[]{};

        Integer[] expected = new Integer[]{};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_singleMatcherAndArgument_notMatching() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        Matcher matcher = mock(Matcher.class);
        matchers.add(matcher);
        Object[] arguments = new Object[]{1};

        Integer[] expected = new Integer[]{0};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_singleMatcherAndArgument_matching() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        Matcher matcher = mock(Matcher.class);
        matchers.add(matcher);
        Object[] arguments = new Object[]{(Integer) any()};

        Integer[] expected = new Integer[]{};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleMatchersAndArguments_allMatching() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        Object[] arguments = new Object[]{1, 2, 3};

        Integer[] expected = new Integer[]{};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleMatchersAndArguments_someNotMatching() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        Object[] arguments = new Object[]{1, "2", 3.0};

        Integer[] expected = new Integer[]{1};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleMatchersAndArguments_allNotMatching() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        Object[] arguments = new Object[]{"1", "2", "3"};

        Integer[] expected = new Integer[]{0, 1, 2};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }
}