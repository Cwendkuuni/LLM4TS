import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setUp() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NoMatchersOrArguments_ReturnsEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[0];
        
        Integer[] expected = new Integer[0];
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsWithDifferentSizes_ReturnsEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[1];
        
        Integer[] expected = new Integer[0];
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ContainsExtraTypeInformationMatcher_DoesNotMatch_ReturnsIndexes() {
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        Object argument = mock(Object.class);
        
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher);
        
        Object[] arguments = new Object[] { argument };
        
        when(matcher instanceof ContainsExtraTypeInformation).thenReturn(true);
        when(argument.toString()).thenReturn("test");
        when(matcher.matches(argument)).thenReturn(false);
        when(matcher.typeMatches(argument)).thenReturn(false);
        
        Integer[] expected = new Integer[] { 0 };
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ContainsExtraTypeInformationMatcher_Matches_ReturnsEmptyArray() {
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        Object argument = mock(Object.class);
        
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher);
        
        Object[] arguments = new Object[] { argument };
        
        when(matcher instanceof ContainsExtraTypeInformation).thenReturn(true);
        when(argument.toString()).thenReturn("test");
        when(matcher.matches(argument)).thenReturn(true);
        when(matcher.typeMatches(argument)).thenReturn(true);
        
        Integer[] expected = new Integer[0];
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_OtherMatcher_DoesNotMatch_ReturnsEmptyArray() {
        Matcher matcher = mock(Matcher.class);
        Object argument = mock(Object.class);
        
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher);
        
        Object[] arguments = new Object[] { argument };
        
        when(matcher instanceof ContainsExtraTypeInformation).thenReturn(false);
        when(argument.toString()).thenReturn("test");
        when(matcher.matches(argument)).thenReturn(false);
        
        Integer[] expected = new Integer[0];
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_OtherMatcher_Matches_ReturnsEmptyArray() {
        Matcher matcher = mock(Matcher.class);
        Object argument = mock(Object.class);
        
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher);
        
        Object[] arguments = new Object[] { argument };
        
        when(matcher instanceof ContainsExtraTypeInformation).thenReturn(false);
        when(argument.toString()).thenReturn("test");
        when(matcher.matches(argument)).thenReturn(true);
        
        Integer[] expected = new Integer[0];
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertArrayEquals(expected, actual);
    }

}