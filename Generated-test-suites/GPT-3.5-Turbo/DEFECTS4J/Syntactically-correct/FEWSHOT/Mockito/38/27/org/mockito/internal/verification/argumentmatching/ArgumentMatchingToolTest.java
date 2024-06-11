package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.matchers.ContainsExtraTypeInformation;

public class ArgumentMatchingToolTest {
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        // Create mock objects
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        
        // Set up mock behavior
        when(matcher1.matches(any())).thenReturn(true);
        when(matcher2.matches(any())).thenReturn(true);
        
        // Create ArgumentMatchingTool instance
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Create test arguments
        Object[] arguments = { "foo", 10, true };
        
        // Call the method under test
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(List.of(matcher1, matcher2), arguments);
        
        // Verify the result
        assertArrayEquals(new Integer[0], result);
        
        // Verify mock interactions
        verify(matcher1).matches("foo");
        verify(matcher2).matches(10);
        verifyZeroInteractions(matcher1, matcher2);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesContainsExtraTypeInformation() {
        // Create mock objects
        ContainsExtraTypeInformation matcher1 = mock(ContainsExtraTypeInformation.class);
        ContainsExtraTypeInformation matcher2 = mock(ContainsExtraTypeInformation.class);
        
        // Set up mock behavior
        when(matcher1.matches(any())).thenReturn(false);
        when(matcher2.matches(any())).thenReturn(false);
        when(matcher1.typeMatches(any())).thenReturn(false);
        when(matcher2.typeMatches(any())).thenReturn(false);
        when(matcher1.toString()).thenReturn("foo");
        when(matcher2.toString()).thenReturn("bar");
        
        // Create ArgumentMatchingTool instance
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Create test arguments
        Object[] arguments = { "foo", 10 };
        
        // Call the method under test
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(List.of(matcher1, matcher2), arguments);
        
        // Verify the result
        assertArrayEquals(new Integer[] { 1 }, result);
        
        // Verify mock interactions
        verify(matcher1).matches("foo");
        verify(matcher2).matches(10);
        verify(matcher1).toString();
        verify(matcher2).toString();
        verify(matcher1).typeMatches(10);
        verify(matcher2).typeMatches("foo");
        verifyZeroInteractions(matcher1, matcher2);
    }
}