package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

public class ArgumentMatchingToolTest {
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[0];
        Integer[] expected = new Integer[0];
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(Arrays.asList(expected), Arrays.asList(actual));

        // Test with non-empty matchers and arguments
        matchers.add(new ContainsExtraTypeInformation());
        arguments = new Object[] { "test" };
        expected = new Integer[] { 0 };
        actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(Arrays.asList(expected), Arrays.asList(actual));

        // Test with non-matching arguments
        matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        arguments = new Object[] { 1 };
        expected = new Integer[0];
        actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(Arrays.asList(expected), Arrays.asList(actual));

        // Test with matching arguments
        matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        arguments = new Object[] { "test" };
        expected = new Integer[0];
        actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(Arrays.asList(expected), Arrays.asList(actual));

        // Test with multiple matchers and arguments
        matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());
        arguments = new Object[] { "test", 1 };
        expected = new Integer[] { 1 };
        actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(Arrays.asList(expected), Arrays.asList(actual));
    }
}