import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.hamcrest.StringDescription;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool.*;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        
        Object[] arguments = {1, "string"};
        
        Integer[] expected = {1}; // Index of argument "string"
        Integer[] result = getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NullCase() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = {};
        
        Integer[] expected = {}; // No arguments, should return empty array
        Integer[] result = getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_EdgeCases() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        
        Object[] arguments = {1, 1.0};
        
        Integer[] expected = {}; // No suspicious arguments since types match
        Integer[] result = getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testSafelyMatches() {
        Matcher matcher = new ContainsExtraTypeInformation();
        Object argument = new Object();
        
        boolean result = safelyMatches(matcher, argument);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testToStringEquals() {
        Matcher matcher = new ContainsExtraTypeInformation();
        Object argument = new Object();
        
        boolean result = toStringEquals(matcher, argument);
        
        assertEquals(false, result);
    }
}