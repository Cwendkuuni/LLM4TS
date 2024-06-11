package org.mockito.internal.verification.argumentmatching.test;

import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.ArrayList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_validArguments() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(contains("Test1"));
        matchers.add(contains("Test2"));
        Object[] arguments = new Object[]{"Test1", "Test2"};
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertArrayEquals(new Integer[]{}, indexes);
    }

    @Test
    public void testGetSuspectinglyNotMatchingArgsIndexes_mismatchedSize() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(contains("Test1"));
        Object[] arguments = new Object[]{"Test1"};
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertArrayEquals(new Integer[0], indexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_mismatchedTypes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(contains("Test1"));
        matchers.add(instanceOf(String.class));
        Object[] arguments = new Object[]{"Test1", 1};
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertArrayEquals(new Integer[]{1}, indexes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_nullMatchers() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Object[] arguments = new Object[]{"Test1", "Test2"};
        tool.getSuspiciouslyNotMatchingArgsIndexes(null, arguments);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_nullArguments() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(contains("Test1"));
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null);
    }

    @Test
    public void testToStringEquals_matchingStrings() {
        String mStr = "contains(\"Test\")";
        String aStr = "\"Test\"";
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.toStringEquals(Matchers.contains(contains("Test")), "Test");
        assertTrue(result);
        result = tool.toStringEquals(Matchers.contains(contains("Test")), aStr);
        assertTrue(result);
    }

    @Test
    public void testToStringEquals_nonMatchingStrings() {
        String mStr = "contains(\"Test\")";
        String aStr = "\"OtherTest\"";
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.toStringEquals(Matchers.contains(contains("Test")), "Test");
        assertTrue(result);
        result = tool.toStringEquals(Matchers.contains(contains("Test")), aStr);
        assertFalse(result);
    }

    @Test
    public void testSafelyMatches_matchingArguments() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Matcher m = contains("Test");
        Object arg = "Test";
        boolean result = tool.safelyMatches(m, arg);
        assertTrue(result);
    }

    @Test
    public void testSafelyMatches_nonMatchingArguments() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Matcher m = contains("Test");
        Object arg = "OtherTest";
        boolean result = tool.safelyMatches(m, arg);
        assertFalse(result);
    }

    @Test(expected = Throwable.class)
    public void testSafelyMatches_throwingMatcher() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Matcher m = new ThrowingMatcher<Object, RuntimeException>() {
            @Override
            public void matches(Object argument) throws RuntimeException {
                throw new RuntimeException();
            }
        };
        Object arg = "Test";
        tool.safelyMatches(m, arg);
    }
}