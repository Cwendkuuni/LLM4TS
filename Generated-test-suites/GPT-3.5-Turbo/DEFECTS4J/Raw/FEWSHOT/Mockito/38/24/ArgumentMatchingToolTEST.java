package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setUp() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = null;

        // Test case 1
        Integer[] expected1 = new Integer[0];
        Integer[] actual1 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected1, actual1);

        // Test case 2
        matchers.add(mock(Matcher.class));
        arguments = new Integer[1];
        Integer[] expected2 = new Integer[0];
        Integer[] actual2 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected2, actual2);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesContainsExtraTypeInformation() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[1];

        // Test case 1
        matchers.add(mock(ContainsExtraTypeInformation.class));
        arguments[0] = new Object();
        when(((ContainsExtraTypeInformation) matchers.get(0)).typeMatches(arguments[0])).thenReturn(false);
        when(StringDescription.toString(matchers.get(0))).thenReturn("description");
        Integer[] expected1 = new Integer[]{0};
        Integer[] actual1 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected1, actual1);

        // Test case 2
        matchers.add(mock(ContainsExtraTypeInformation.class));
        arguments[0] = "test";
        when(((ContainsExtraTypeInformation) matchers.get(1)).typeMatches(arguments[0])).thenReturn(true);
        when(StringDescription.toString(matchers.get(1))).thenReturn("test");
        Integer[] expected2 = new Integer[0];
        Integer[] actual2 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected2, actual2);
    }
}