import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setup() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withMatchingMatchersAndArguments() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        Object[] arguments = { "arg1", "arg2" };

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withMismatchingMatchersAndArguments() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        Object[] arguments = { "arg1", 123 };

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(1, result.length);
        assertEquals(Integer.valueOf(1), result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withMismatchingMatchersAndArgumentsAndContainsExtraTypeInformation() {
        ContainsExtraTypeInformation mockMatcher = mock(ContainsExtraTypeInformation.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mockMatcher);
        Object[] arguments = { "arg1" };

        when(mockMatcher.typeMatches(arguments[0])).thenReturn(false);

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(1, result.length);
        assertEquals(Integer.valueOf(0), result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withMismatchingMatchersAndArgumentsAndContainsExtraTypeInformationAndSafelyMatchesThrowsException() {
        ContainsExtraTypeInformation mockMatcher = mock(ContainsExtraTypeInformation.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mockMatcher);
        Object[] arguments = { "arg1" };

        when(mockMatcher.typeMatches(arguments[0])).thenReturn(true);
        when(mockMatcher.matches(arguments[0])).thenThrow(new RuntimeException());

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(1, result.length);
        assertEquals(Integer.valueOf(0), result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withMismatchingMatchersAndArgumentsAndToStringEqualsReturnsFalse() {
        ContainsExtraTypeInformation mockMatcher = mock(ContainsExtraTypeInformation.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mockMatcher);
        Object[] arguments = { "arg1" };

        when(mockMatcher.typeMatches(arguments[0])).thenReturn(true);
        when(mockMatcher.matches(arguments[0])).thenReturn(true);
        when(mockMatcher.toString()).thenReturn("mockMatcher");

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(1, result.length);
        assertEquals(Integer.valueOf(0), result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withMismatchingMatchersAndArgumentsAndToStringEqualsReturnsTrue() {
        ContainsExtraTypeInformation mockMatcher = mock(ContainsExtraTypeInformation.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mockMatcher);
        Object[] arguments = { "arg1" };

        when(mockMatcher.typeMatches(arguments[0])).thenReturn(true);
        when(mockMatcher.matches(arguments[0])).thenReturn(true);
        when(mockMatcher.toString()).thenReturn(arguments[0].toString());

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }

}