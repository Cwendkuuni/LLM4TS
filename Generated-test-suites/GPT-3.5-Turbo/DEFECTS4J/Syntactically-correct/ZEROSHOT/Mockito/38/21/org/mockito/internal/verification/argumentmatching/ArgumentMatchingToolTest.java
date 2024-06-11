import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool.*;
import static org.mockito.internal.matchers.ContainsExtraTypeInformation.*;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersSizeNotEqualToArgumentsLength_ReturnsEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[2];
        Integer[] expected = new Integer[0];
        
        Integer[] result = getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ContainsExtraTypeInformation_ThirdConditionFails_ReturnsSuspiciousIndexes() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[1];
        arguments[0] = new Object();
        Integer[] expected = new Integer[] {0};
        
        Integer[] result = getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testSafelyMatches_ThrowException_ReturnsFalse() {
        Matcher matcher = new ContainsExtraTypeInformation();
        Object arg = new Object();
        boolean expected = false;
        
        boolean result = safelyMatches(matcher, arg);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testSafelyMatches_NotThrowException_ReturnsTrue() {
        Matcher matcher = new ContainsExtraTypeInformation();
        Object arg = new ContainsExtraTypeInformation();
        boolean expected = true;
        
        boolean result = safelyMatches(matcher, arg);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testToStringEquals_MatcherToStringEqualsArgToString_ReturnsTrue() {
        Matcher matcher = new ContainsExtraTypeInformation();
        Object arg = new ContainsExtraTypeInformation();
        boolean expected = true;
        
        boolean result = toStringEquals(matcher, arg);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testToStringEquals_MatcherToStringNotEqualsArgToString_ReturnsFalse() {
        Matcher matcher = new ContainsExtraTypeInformation();
        Object arg = new Object();
        boolean expected = false;
        
        boolean result = toStringEquals(matcher, arg);
        
        Assert.assertEquals(expected, result);
    }

}