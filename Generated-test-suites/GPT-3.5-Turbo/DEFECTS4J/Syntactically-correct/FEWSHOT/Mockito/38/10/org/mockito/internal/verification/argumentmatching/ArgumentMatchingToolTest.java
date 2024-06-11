
package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

public class ArgumentMatchingToolTest {
    
    private ArgumentMatchingTool argumentMatchingTool;
    
    @Before
    public void setUp() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithMatchingArguments() {
        List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation(), new ContainsExtraTypeInformation());
        Object[] arguments = new Object[2];
        arguments[0] = "arg1";
        arguments[1] = "arg2";
        
        Integer[] expected = new Integer[0];
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(expected.length, result.length);
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithNotMatchingArguments() {
        List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation(), new ContainsExtraTypeInformation());
        Object[] arguments = new Object[2];
        arguments[0] = new Integer(5);
        arguments[1] = "arg2";
        
        Integer[] expected = {0};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(expected.length, result.length);
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithMissingMatchers() {
        List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[2];
        arguments[0] = "arg1";
        arguments[1] = "arg2";
        
        Integer[] expected = new Integer[0];
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(expected.length, result.length);
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithExtraMatchers() {
        List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation(), new ContainsExtraTypeInformation(), new ContainsExtraTypeInformation());
        Object[] arguments = new Object[2];
        arguments[0] = "arg1";
        arguments[1] = "arg2";
        
        Integer[] expected = new Integer[0];
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(expected.length, result.length);
        assertEquals(expected, result);
    }
}
