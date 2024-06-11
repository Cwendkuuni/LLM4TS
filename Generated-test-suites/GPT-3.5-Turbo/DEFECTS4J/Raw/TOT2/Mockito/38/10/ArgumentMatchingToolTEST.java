import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.hamcrest.Matcher;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Typical use case
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(is(1));
        matchers.add(is("two"));
        
        Object[] arguments = {1, "two"};
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
        
        // Edge case: Different types but toString representation is same
        matchers.add(is("3"));
        arguments[0] = 3;
        
        result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(1, result.length);
        assertEquals(0, (int) result[0]);
        
        // Exception handling scenario
        matchers.add(new CustomMatcher());
        arguments[1] = new Object(); // Invalid argument
        
        result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }

    @Test
    public void testSafelyMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Typical use case
        assertTrue(tool.safelyMatches(is(1), 1));
        
        // Edge case: Matcher throws exception
        assertFalse(tool.safelyMatches(new FailingMatcher(), 1));
    }

    @Test
    public void testToStringEquals() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Typical use case
        assertTrue(tool.toStringEquals(is(1), "1"));
        
        // Edge case: toString representations not equal
        assertFalse(tool.toStringEquals(is(1), "one"));
    }
}