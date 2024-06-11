package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[]{1, "two", 3.0, false};
        
        Integer[] expectedResult = new Integer[]{0, 1, 2, 3};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertArrayEquals(expectedResult, result);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithMatchers() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(eq(1));
        matchers.add(eq("two"));
        matchers.add(eq(3.0));
        matchers.add(eq(false));
        
        Object[] arguments = new Object[]{1, "two", 3.0, false};
        
        Integer[] expectedResult = new Integer[]{};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertArrayEquals(expectedResult, result);
    }
}