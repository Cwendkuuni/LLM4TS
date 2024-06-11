package org.mockito.internal.verification.argumentmatching;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
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
    public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersSizeNotEqualArgumentsLength_shouldReturnEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[2];
        arguments[0] = "test";
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsAreEmpty_shouldReturnEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[0];
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsMatch_shouldReturnEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[1];
        arguments[0] = new ContainsExtraTypeInformation();
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersDoNotMatchButToStringEquals_shouldReturnEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[1];
        arguments[0] = new StringDescription().appendText("test").toDescription();
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersDoNotMatchAndToStringDoesNotEqual_shouldReturnIndex() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[1];
        arguments[0] = "test";
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
    }
}