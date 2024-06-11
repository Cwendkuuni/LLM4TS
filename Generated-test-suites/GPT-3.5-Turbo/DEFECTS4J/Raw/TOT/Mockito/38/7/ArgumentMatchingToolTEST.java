import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;
    
    @Before
    public void setUp() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyMatchersAndArguments_ReturnEmptyArray() {
        List<Matcher> matchers = Arrays.asList();
        Object[] arguments = new Object[]{};
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsSizeMismatch_ReturnEmptyArray() {
        List<Matcher> matchers = Arrays.asList((Matcher) any(), (Matcher) any());
        Object[] arguments = new Object[]{1};
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspiciousArguments_ReturnEmptyArray() {
        List<Matcher> matchers = Arrays.asList((Matcher) eq("test"), (Matcher) any());
        Object[] arguments = new Object[]{"test", 1};
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousArgumentNotContainsExtraTypeInformation_ReturnEmptyArray() {
        List<Matcher> matchers = Arrays.asList((Matcher) eq("test"), (Matcher) eq(2), (Matcher) eq(true));
        Object[] arguments = new Object[]{"test", 1, true};
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousArgumentContainsExtraTypeInformation_ReturnSingleIndex() {
        List<Matcher> matchers = Arrays.asList((Matcher) eq("test"), (Matcher) eq(2), (Matcher) eq(true));
        Object[] arguments = new Object[]{"test", "2", true};
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(1, result.length);
        assertEquals(Integer.valueOf(1), result[0]);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousArgumentContainsExtraTypeInformationAndTypeMatches_ReturnEmptyArray() {
        List<Matcher> matchers = Arrays.asList((Matcher) eq("test"), (Matcher) eq(2), (Matcher) eq(true));
        Object[] arguments = new Object[]{"test", "2.0", true};
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testSafelyMatches_ValidMatcherAndArgument_ReturnTrue() {
        Matcher matcher = mock(Matcher.class);
        Object argument = new Object();
        when(matcher.matches(argument)).thenReturn(true);
        
        boolean result = argumentMatchingTool.safelyMatches(matcher, argument);
        
        assertTrue(result);
    }
    
    @Test
    public void testSafelyMatches_InvalidMatcherAndArgument_ReturnFalse() {
        Matcher matcher = mock(Matcher.class);
        Object argument = new Object();
        when(matcher.matches(argument)).thenThrow(new RuntimeException("Mockito Error"));
        
        boolean result = argumentMatchingTool.safelyMatches(matcher, argument);
        
        assertFalse(result);
    }
    
    @Test
    public void testToStringEquals_MatcherToStringEqualsArgumentToString_ReturnTrue() {
        Matcher matcher = mock(Matcher.class);
        Object argument = new Object();
        StringDescription description = new StringDescription();
        description.appendText(argument.toString());
        when(matcher.toString()).thenReturn(description.toString());
        
        boolean result = argumentMatchingTool.toStringEquals(matcher, argument);
        
        assertTrue(result);
    }
    
    @Test
    public void testToStringEquals_MatcherToStringNotEqualsArgumentToString_ReturnFalse() {
        Matcher matcher = mock(Matcher.class);
        Object argument = new Object();
        StringDescription description = new StringDescription();
        description.appendText("Different Description");
        when(matcher.toString()).thenReturn(description.toString());
        
        boolean result = argumentMatchingTool.toStringEquals(matcher, argument);
        
        assertFalse(result);
    }
}