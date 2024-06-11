package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setup() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsLengthNotEqual() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = {1, 2, 3};
        Integer[] expected = {};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousArguments() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = {1, 2, 3};
        Integer[] expected = {};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_suspiciousArgumentsExist() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = {1, "2", 3};
        Integer[] expected = {1};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }
}