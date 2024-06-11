package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.*;
import org.junit.Test;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        Object[] arguments = new Object[0];
        
        Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
        
        matchers.add(new ContainsExtraTypeInformation());
        
        result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
        
        arguments = new Object[1];
        arguments[0] = new Object();
        
        result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
        
        arguments[0] = "test";
        
        result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
        
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        matcher.typeMatches("test");
        matchers.set(0, matcher);
        
        result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
    }
    
    @Test
    public void testSafelyMatches() {
        Matcher matcher = new ContainsExtraTypeInformation();
        Object arg = new Object();
        
        assertTrue(ArgumentMatchingTool.safelyMatches(matcher, arg));
        
        matcher = new ContainsExtraTypeInformation() {
            public boolean matches(Object item) {
                throw new RuntimeException();
            }
        };
        
        assertFalse(ArgumentMatchingTool.safelyMatches(matcher, arg));
    }
    
    @Test
    public void testToStringEquals() {
        Matcher matcher = new ContainsExtraTypeInformation();
        Object arg = "test";
        
        assertTrue(ArgumentMatchingTool.toStringEquals(matcher, arg));
        
        matcher = new ContainsExtraTypeInformation();
        
        assertFalse(ArgumentMatchingTool.toStringEquals(matcher, arg));
    }
}