package org.mockito.internal.verification.argumentmatching;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertArrayEquals;

@RunWith(MockitoJUnitRunner.class)
public class ArgumentMatchingToolTest {

    // Expert 1's test cases for getSuspiciouslyNotMatchingArgsIndexes method

    // ...

    @Test
    public void testGetInstance() {
        ArgumentMatchingTool tool = ArgumentMatchingTool.getInstance();
        ArgumentMatchingTool anotherTool = ArgumentMatchingTool.getInstance();
        assert (tool == anotherTool);
    }

    @Test
    public void testSafelyMatches_StringMatcher() {
        Matcher<String> matcher = Matchers.matches("test.*");
        Object argument = "testString";
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.safelyMatches(matcher, argument);
        assertTrue(result);
    }

    @Test
    public void testSafelyMatches_NumberMatcher() {
        Matcher<Integer> matcher = Matchers.eq(10);
        Object argument = new Integer(10);
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.safelyMatches(matcher, argument);
        assertTrue(result);
    }

    @Test
    public void testSafelyMatches_NullMatcher() {
        Matcher<?> matcher = null;
        Object argument = new Object();
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.safelyMatches(matcher, argument);
        assertFalse(result);
    }

    @Test
    public void testToStringEquals_MatchingStrings() {
        Matcher<String> matcher = Matchers.eq("testString");
        String argument = "testString";
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.toStringEquals(matcher, argument);
        assertTrue(result);
    }

    @Test
    public void testToStringEquals_NonMatchingStrings() {
        Matcher<String> matcher = Matchers.eq("testString1");
        String argument = "testString2";
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.toStringEquals(matcher, argument);
        assertFalse(result);
    }

    @Test
    public void testToStringEquals_NullMatcher() {
        Matcher<?> matcher = null;
        Object argument = new Object();
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.toStringEquals(matcher, argument);
        assertFalse(result);
    }

}