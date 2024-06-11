import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matcher;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchingArguments() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(ArgumentMatchers.any(Integer.class));
        matchers.add(ArgumentMatchers.any(String.class));
        
        Object[] arguments = { 1, "test" };
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_notMatchingArguments() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(ArgumentMatchers.any(Integer.class));
        matchers.add(ArgumentMatchers.any(String.class));
        
        Object[] arguments = { "test", 1 };
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(2, result.length);
        assertEquals(Integer.valueOf(0), result[0]);
        assertEquals(Integer.valueOf(1), result[1]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_differentTypesWithSameToString() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new ContainsExtraTypeInformation("1"));
        matchers.add(new ContainsExtraTypeInformation(1));
        
        Object[] arguments = { "1", 1 };
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_exceptionThrown() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(ArgumentMatchers.any(Integer.class));
        matchers.add(ArgumentMatchers.any(String.class));
        
        Object[] arguments = { 1, "test" };
        
        // Mock the matcher to throw an exception
        Matcher m = ArgumentMatchers.any();
        Object arg = 1;
        boolean exceptionThrown = false;
        try {
            tool.safelyMatches(m, arg);
        } catch (Throwable t) {
            exceptionThrown = true;
        }
        
        assertTrue(exceptionThrown);
    }
}