package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool tool;

    @Before
    public void setUp() {
        tool = new ArgumentMatchingTool();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[0];
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsSizeMismatch() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));
        Object[] arguments = new Object[1];
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationAndNotMatching() {
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher);
        Object[] arguments = new Object[1];
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationAndMatching() {
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher);
        Object[] arguments = new Object[1];
        arguments[0] = "argument";
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleMatchers() {
        ContainsExtraTypeInformation matcher1 = new ContainsExtraTypeInformation();
        ContainsExtraTypeInformation matcher2 = new ContainsExtraTypeInformation();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher1);
        matchers.add(matcher2);
        Object[] arguments = new Object[2];
        arguments[0] = "argument1";
        arguments[1] = "argument2";
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(2, result.length);
        assertTrue(result[0] == 0 || result[0] == 1);
        assertTrue(result[1] == 0 || result[1] == 1);
        assertTrue(result[0] != result[1]);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleMatchersWithSomeMatching() {
        ContainsExtraTypeInformation matcher1 = new ContainsExtraTypeInformation();
        ContainsExtraTypeInformation matcher2 = new ContainsExtraTypeInformation();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher1);
        matchers.add(matcher2);
        Object[] arguments = new Object[2];
        arguments[0] = "argument1";
        arguments[1] = "argument2";
        matcher2.typeMatches("argument2");
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_exceptionThrownDuringMatching() {
        Matcher matcher = mock(Matcher.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher);
        Object[] arguments = new Object[1];
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_toStringEquals() {
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher);
        Object[] arguments = new Object[1];
        arguments[0] = matcher.toString();
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_nullArguments() {
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher);
        Object[] arguments = null;
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
}