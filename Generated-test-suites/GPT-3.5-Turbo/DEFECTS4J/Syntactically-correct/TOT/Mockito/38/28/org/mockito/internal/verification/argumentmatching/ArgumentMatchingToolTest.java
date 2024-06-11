import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {
    
    private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersSizeNotEqualToArgumentsLength_ReturnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList(new Matcher(), new Matcher());
        Object[] arguments = new Object[] {};
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        Assert.assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspiciousArguments_ReturnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList(new Matcher(), new Matcher());
        Object[] arguments = new Object[] {1, 2};
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        Assert.assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ContainsExtraTypeInformationNotMatching_ReturnsSuspiciousIndexes() {
        ContainsExtraTypeInformation matcher1 = new ContainsExtraTypeInformation();
        ContainsExtraTypeInformation matcher2 = new ContainsExtraTypeInformation();
        matcher1.setType("type1");
        matcher2.setType("type2");
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);
        Object[] arguments = new Object[] {"type1", "type2"};
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        Assert.assertEquals(2, result.length);
        Assert.assertEquals(0, result[0].intValue());
        Assert.assertEquals(1, result[1].intValue());
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ContainsExtraTypeInformationMatching_OnlyReturnsSuspiciousIndexes() {
        ContainsExtraTypeInformation matcher1 = new ContainsExtraTypeInformation();
        ContainsExtraTypeInformation matcher2 = new ContainsExtraTypeInformation();
        matcher1.setType("type1");
        matcher2.setType("type2");
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);
        Object[] arguments = new Object[] {"type1", "type1"};
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        Assert.assertEquals(1, result.length);
        Assert.assertEquals(1, result[0].intValue());
    }
    
    @Test
    public void testSafelyMatches_ExceptionThrown_ReturnsFalse() {
        Matcher matcher = new Matcher();
        Object argument = new Object();
        
        boolean result = argumentMatchingTool.safelyMatches(matcher, argument);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testSafelyMatches_NoException_ReturnsTrue() {
        Matcher matcher = new Matcher();
        Object argument = new Object();
        matcher.setExpectedResult(true);
        
        boolean result = argumentMatchingTool.safelyMatches(matcher, argument);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testToStringEquals_MatcherToStringNotEqualsArgumentToString_ReturnsFalse() {
        Matcher matcher = new Matcher();
        Object argument = new Object();
        
        boolean result = argumentMatchingTool.toStringEquals(matcher, argument);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testToStringEquals_MatcherToStringEqualsArgumentToString_ReturnsTrue() {
        Matcher matcher = new Matcher();
        Object argument = new Object();
        matcher.setToString("matcher");
        argument.setToString("matcher");
        
        boolean result = argumentMatchingTool.toStringEquals(matcher, argument);
        
        Assert.assertTrue(result);
    }
}