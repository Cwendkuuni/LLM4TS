import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Typical use case
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        
        Object[] arguments = new Object[] { "argument1", "argument2" };
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
        
        // Edge case: matchers size not equal to arguments
        matchers.add(mock(Matcher.class));
        
        Integer[] result2 = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result2.length);
        
        // Edge case: ContainsExtraTypeInformation instance encountered
        List<Matcher> matchers2 = new LinkedList<Matcher>();
        matchers2.add(new ContainsExtraTypeInformation(String.class));
        
        Object[] arguments2 = new Object[] { "5" };
        
        Integer[] result3 = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers2, arguments2);
        
        assertEquals(1, result3.length);
    }

    @Test
    public void testSafelyMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        // Error scenario: matcher throws exception
        Matcher matcher = mock(Matcher.class);
        Object arg = "test";
        
        boolean result = tool.safelyMatches(matcher, arg);
        
        assertFalse(result);
    }

    @Test
    public void testToStringEquals() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        // Typical use case
        Matcher matcher = mock(Matcher.class);
        Object arg = "test";
        
        boolean result = tool.toStringEquals(matcher, arg);
        
        assertTrue(result);
    }
}