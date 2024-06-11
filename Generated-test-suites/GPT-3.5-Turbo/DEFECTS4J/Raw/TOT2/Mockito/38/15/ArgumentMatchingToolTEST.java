import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsMatch() {
        List<Matcher> matchers = Arrays.asList(CoreMatchers.anyString(), CoreMatchers.instanceOf(Integer.class));
        Integer[] arguments = new Integer[]{1, 2};
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsMismatch() {
        List<Matcher> matchers = Arrays.asList(CoreMatchers.anyString(), CoreMatchers.any());
        Integer[] arguments = new Integer[]{"2", 3};
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(2, result.length);
        assertTrue(Arrays.asList(result).contains(0));
        assertTrue(Arrays.asList(result).contains(1));
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersSizeNotEqualToArgumentsSize() {
        List<Matcher> matchers = Arrays.asList(CoreMatchers.anyString());
        Integer[] arguments = new Integer[]{"2", 3};
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testSafelyMatches_ThrowsException() {
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        boolean result = argumentMatchingTool.safelyMatches(matcher, "test");
        
        assertFalse(result);
    }
    
    @Test
    public void testSafelyMatches_NoException() {
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        boolean result = argumentMatchingTool.safelyMatches(matcher, 123);
        
        assertTrue(result);
    }
    
    @Test
    public void testToStringEquals_StringsEqual() {
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        boolean result = argumentMatchingTool.toStringEquals(matcher, "test");
        
        assertTrue(result);
    }
    
    @Test
    public void testToStringEquals_StringsNotEqual() {
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        boolean result = argumentMatchingTool.toStringEquals(matcher, 123);
        
        assertFalse(result);
    }
}