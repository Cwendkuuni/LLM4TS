package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());
        
        Object[] arguments = new Object[3];
        arguments[0] = "test";
        arguments[1] = 10;
        arguments[2] = new Object();
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] suspiciousIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(3, suspiciousIndexes.length);
        assertEquals(0, (int) suspiciousIndexes[0]);
        assertEquals(1, (int) suspiciousIndexes[1]);
        assertEquals(2, (int) suspiciousIndexes[2]);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesNoMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        
        Object[] arguments = new Object[0];
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] suspiciousIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, suspiciousIndexes.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesDifferentSizes() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        
        Object[] arguments = new Object[0];
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] suspiciousIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, suspiciousIndexes.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesMatches() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());
        
        Object[] arguments = new Object[2];
        arguments[0] = "test";
        arguments[1] = 10;
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] suspiciousIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, suspiciousIndexes.length);
    }
    
    @Test
    public void testSafelyMatches() {
        Matcher matcher = new ContainsExtraTypeInformation();
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        boolean result = tool.safelyMatches(matcher, "test");
        
        assertEquals(false, result);
    }
    
    @Test
    public void testToStringEquals() {
        Matcher matcher = new ContainsExtraTypeInformation() {
            @Override
            protected boolean matchesSafely(Object item, Description mismatchDescription) {
                return false;
            }
            
            @Override
            public void describeTo(Description description) {
                description.appendText("test");
            }
        };
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        boolean result = tool.toStringEquals(matcher, "test");
        
        assertEquals(true, result);
    }
}