package org.junit.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.internal.matchers.Difference;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(new ContainsExtraTypeInformation(Integer.class, "foo"));
        Object[] arguments = new Object[]{1, "two"};
        Integer[] expected = new Integer[]{1};
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithDifferentListSizes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<Matcher>();
        Object[] arguments = new Object[]{1, 2};
        Integer[] expected = new Integer[0];
        List<Matcher> wrongMatchersSize = new LinkedList<Matcher>();
        Assert.assertArrayEquals(expected, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
        Assert.assertArrayEquals(expected, tool.getSuspiciouslyNotMatchingArgsIndexes(wrongMatchersSize, arguments));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithToStringEqualsButTypesDifferent() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<Matcher>();
        Object[] arguments = new Object[]{1, "1"};
        Integer[] expected = new Integer[]{0};
        matchers.add(new ContainsExtraTypeInformation(Integer.class, "1"));
        Assert.assertArrayEquals(expected, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithToStringEqualsButTypesSame() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<Matcher>();
        Object[] arguments = new Object[]{1, "1"};
        Integer[] expected = new Integer[0];
        matchers.add(new ContainsExtraTypeInformation(String.class, "1"));
        Assert.assertArrayEquals(expected, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithNullMatchers() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Object[] arguments = new Object[]{1, 2};
        List<Matcher> matchers = null;
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithNullArguments() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<Matcher>();
        Object[] arguments = null;
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }
}