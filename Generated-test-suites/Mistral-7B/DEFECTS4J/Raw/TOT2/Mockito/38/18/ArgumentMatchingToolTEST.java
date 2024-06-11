package org.mockito.internal.verification.argumentmatching.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.verification.ArgumentMatchingTool;

@RunWith(MockitoJUnitRunner.class)
public class ArgumentMatchingToolTest {

    ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsAreEqual_thenReturnEmptyArray() {
        List<Matcher> matchers = new ArrayList<>();
        Object[] arguments = new Object[] { new Integer(1), new Integer(1) };
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(new Integer[0], result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsAreDifferent_thenReturnIndexes() {
        List<Matcher> matchers = new ArrayList<>();
        Matcher m1 = mock(Matcher.class);
        Matcher m2 = mock(Matcher.class);
        Object[] arguments = new Object[] { new Integer(1), new String("test") };
        matchers.add(m1);
        matchers.add(m2);
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsHaveDifferentSizes_thenThrowIllegalArgumentException() {
        List<Matcher> matchers = new ArrayList<>();
        Object[] arguments = new Object[] { new Integer(1), new String("test") };
        argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    @Test
    public void testToStringEquals_whenMatchersToStringEqualsArgumentsToString_thenReturnTrue() {
        List<Matcher> matchers = new ArrayList<>();
        Matcher m = mock(Matcher.class);
        Object arg = new Object() {
            @Override
            public String toString() {
                return "1";
            }
        };
        Matcher mToString = mock(Matcher.class);
        StringDescription mDesc = new StringDescription();
        mDesc.appendValue("1");
        when(m.toString()).thenReturn(mDesc.toString());
        boolean result = argumentMatchingTool.toStringEquals(m, arg);
        assertTrue(result);
    }

    @Test
    public void testToStringEquals_whenMatchersToStringIsNotEqualArgumentsToString_thenReturnFalse() {
        List<Matcher> matchers = new ArrayList<>();
        Matcher m = mock(Matcher.class);
        Object arg = new Object() {
            @Override
            public String toString() {
                return "1";
            }
        };
        StringDescription mDesc = new StringDescription();
        mDesc.appendValue("2");
        when(m.toString()).thenReturn(mDesc.toString());
        boolean result = argumentMatchingTool.toStringEquals(m, arg);
        assertFalse(result);
    }

    @Test
    public void testSafelyMatches_whenMatcherMatchesArgument_thenReturnTrue() {
        Matcher m = mock(Matcher.class);
        Object arg = new Integer(1);
        when(m.matches(arg)).thenReturn(true);
        boolean result = argumentMatchingTool.safelyMatches(m, arg);
        assertTrue(result);
    }

    @Test
    public void testSafelyMatches_whenMatcherDoesNotMatchArgument_thenReturnFalse() {
        Matcher m = mock(Matcher.class);
        Object arg = new Integer(2);
        when(m.matches(arg)).thenReturn(false);
        boolean result = argumentMatchingTool.safelyMatches(m, arg);
        assertFalse(result);
    }

    @Test(expected = Throwable.class)
    public void testSafelyMatches_whenMatcherThrowsAnException_thenThrowException() {
        Matcher m = mock(Matcher.class);
        Object arg = new Object();
        when(m.matches(arg)).thenThrow(new RuntimeException("Test Exception"));
        try {
            argumentMatchingTool.safelyMatches(m, arg);
        } catch (Throwable e) {
            assertEquals("Test Exception", e.getMessage());
        }
    }
}