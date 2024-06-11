package org.mockito.internal.verification.argumentmatching;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.internal.matchers.ContainsExtraTypeInformation.typeMatches;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyList_returnsEmptyArray() {
        List<Matcher> matchers = new ArrayList<>();
        Object[] arguments = new Object[0];

        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[0], result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_mismatchedSizes_returnsEmptyArray() {
        List<Matcher> matchers = new ArrayList<>();
        Object[] arguments = new Object[2];

        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[0], result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchingArguments_returnsEmptyArray() {
        List<Matcher> matchers = new ArrayList<>();
        Object[] arguments = new Object[]{new Object(), new Object()};

        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[0], result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_differentTypes_returnsIndex() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new ContainsExtraTypeInformation("String") {
            @Override
            public boolean matches(Object arg) {
                return arg instanceof Integer;
            }
        });
        Object[] arguments = new Object[]{new Integer(1), new String("Test")};

        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[]{1}, result);
    }

    @Test
    public void testSafelyMatches_matching_returnsTrue() {
        List<Matcher> matchers = new ArrayList<>();
        Object arg = new Object();

        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        boolean result = argumentMatchingTool.safelyMatches(new Equals(arg), arg);

        assertTrue(result);
    }

    @Test
    public void testSafelyMatches_nonMatching_returnsFalse() {
        List<Matcher> matchers = new ArrayList<>();
        Object arg = new Object();

        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        boolean result = argumentMatchingTool.safelyMatches(new Not(new Equals(arg)), arg);

        assertFalse(result);
    }

    @Test
    public void testToStringEquals_matchingStrings_returnsTrue() {
        List<Matcher> matchers = new ArrayList<>();
        Object arg = "Test";

        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        boolean result = argumentMatchingTool.toStringEquals(new Equals("Test"), arg);

        assertTrue(result);
    }

    @Test
    public void testToStringEquals_nonMatchingStrings_returnsFalse() {
        List<Matcher> matchers = new ArrayList<>();
        Object arg = "Test";

        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        boolean result = argumentMatchingTool.toStringEquals(new Equals("Different"), arg);

        assertFalse(result);
    }

    @Test
    public void testToStringEquals_matchingObjects_returnsTrue() {
        List<Matcher> matchers = new ArrayList<>();
        Object arg1 = new Object();
        Object arg2 = arg1;

        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        boolean result = argumentMatchingTool.toStringEquals(new Equals(arg1), arg2);

        assertTrue(result);
    }

    @Test
    public void testToStringEquals_nonMatchingObjects_returnsFalse() {
        List<Matcher> matchers = new ArrayList<>();
        Object arg1 = new Object();
        Object arg2 = new Object();

        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        boolean result = argumentMatchingTool.toStringEquals(new Equals(arg1), arg2);

        assertFalse(result);
    }

    @Test(expected = NullPointerException.class)
    public void testSafelyMatches_nullMatcher_throwsException() {
        List<Matcher> matchers = null;
        Object arg = new Object();

        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        argumentMatchingTool.safelyMatches(matchers, arg);
    }

    @Test(expected = NullPointerException.class)
    public void testToStringEquals_nullArgument_throwsException() {
        List<Matcher> matchers = new ArrayList<>();
        Object arg = null;

        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        argumentMatchingTool.toStringEquals(matchers, arg);
    }

    @Test
    public void testToStringEquals_matchingToString_returnsTrue() {
        List<Matcher> matchers = new ArrayList<>();
        Object obj = new Object() {
            @Override
            public String toString() {
                return "Test";
            }
        };

        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        boolean result = argumentMatchingTool.toStringEquals(new Equals("Test"), obj);

        assertTrue(result);
    }

    @Test
    public void testToStringEquals_nonMatchingToString_returnsFalse() {
        List<Matcher> matchers = new ArrayList<>();
        Object obj = new Object() {
            @Override
            public String toString() {
                return "Different";
            }
        };

        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        boolean result = argumentMatchingTool.toStringEquals(new Equals("Test"), obj);

        assertFalse(result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchingTypes_returnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList(new Equals(new Integer(1)), new Equals(new Integer(2)));
        Object[] arguments = new Object[] {new Integer(1), new Integer(2)};

        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[0], result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchingTypesWithExtraInfo_returnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList(
                new ContainsExtraTypeInformation("Integer") {
                    @Override
                    public boolean matches(Object arg) {
                        return arg instanceof Integer;
                    }
                },
                new Equals(new Integer(2))
        );
        Object[] arguments = new Object[] {new Integer(1), new Integer(2)};

        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[0], result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_nonMatchingTypes_returnsIndex() {
        List<Matcher> matchers = Arrays.asList(
                new ContainsExtraTypeInformation("String") {
                    @Override
                    public boolean matches(Object arg) {
                        return arg instanceof Integer;
                    }
                },
                new Equals("Test")
        );
        Object[] arguments = new Object[] {new Integer(1), "Test"};

        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[]{1}, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_nonMatchingTypesWithExtraInfo_returnsIndex() {
        List<Matcher> matchers = Arrays.asList(
                new ContainsExtraTypeInformation("String") {
                    @Override
                    public boolean matches(Object arg) {
                        return arg instanceof Integer;
                    }
                },
                new ContainsExtraTypeInformation("String") {
                    @Override
                    public boolean matches(Object arg) {
                        return arg instanceof String;
                    }

                    @Override
                    public boolean typeMatches(Object arg) {
                        return arg instanceof String;
                    }
                }
        );
        Object[] arguments = new Object[] {new Integer(1), "Test"};

        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[]{0}, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchingTypesWithExtraInfo_returnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList(
                new ContainsExtraTypeInformation("Integer") {
                    @Override
                    public boolean matches(Object arg) {
                        return arg instanceof Integer;
                    }
                },
                new ContainsExtraTypeInformation("Integer") {
                    @Override
                    public boolean matches(Object arg) {
                        return arg instanceof Integer;
                    }

                    @Override
                    public boolean typeMatches(Object arg) {
                        return arg instanceof Integer;
                    }
                }
        );
        Object[] arguments = new Object[] {new Integer(1), new Integer(2)};

        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[0], result);
    }
}