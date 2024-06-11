import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Typical use case
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(CoreMatchers.equalTo(1));
        matchers.add(CoreMatchers.instanceOf(String.class));
        
        Object[] arguments = {1, "test"};
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length); // No suspicious indexes
        
        // Edge case: Different number of matchers and arguments
        List<Matcher> matchers2 = new ArrayList<>();
        matchers2.add(CoreMatchers.equalTo(1));
        
        Object[] arguments2 = {1, "test"};
        Integer[] result2 = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers2, arguments2);
        assertEquals(0, result2.length); // No suspicious indexes since there is a mismatch in size
        
        // Exception handling scenario
        List<Matcher> matchers3 = new ArrayList<>();
        matchers3.add(CoreMatchers.equalTo(1));
        matchers3.add(CoreMatchers.instanceOf(String.class));
        
        Object[] arguments3 = {1, new Object()};
        Integer[] result3 = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers3, arguments3);
        assertEquals(1, result3.length); // One suspicious index since type doesn't match
        
    }

    @Test
    public void testSafelyMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Exception handling scenario
        Matcher matcher = CoreMatchers.equalTo(1);
        Object argument = "1";
        boolean result = tool.safelyMatches(matcher, argument);
        assertFalse(result); // Exception should be caught
        
    }

    @Test
    public void testToStringEquals() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Typical use case
        Matcher matcher = CoreMatchers.equalTo(1);
        Object argument = 1;
        boolean result = tool.toStringEquals(matcher, argument);
        assertTrue(result); // toString() representation is same
        
        // Edge case: Different string representation
        Matcher matcher2 = CoreMatchers.equalTo(1);
        Object argument2 = "1";
        boolean result2 = tool.toStringEquals(matcher2, argument2);
        assertFalse(result2); // toString() representation is different
    }

}