import org.hamcrest.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool.*;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Typical use case
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new ContainsExtraTypeInformation());
        
        Object[] arguments = new Object[] { "test" };
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
        
        // Edge case: Empty matchers list
        matchers.clear();
        matchers.add(new ContainsExtraTypeInformation());
        
        Integer[] resultEmptyMatchers = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, resultEmptyMatchers.length);
        
        // Edge case: Empty arguments list
        List<Matcher> matchersEmpty = new ArrayList<>();
        matchersEmpty.add(new ContainsExtraTypeInformation());
        
        Object[] argumentsEmpty = new Object[] {};
        
        Integer[] resultEmptyArgs = tool.getSuspiciouslyNotMatchingArgsIndexes(matchersEmpty, argumentsEmpty);
        
        assertEquals(0, resultEmptyArgs.length);
        
        // Error scenario: Matchers list size is not equal to arguments list size
        List<Matcher> matchersError = new ArrayList<>();
        matchersError.add(new ContainsExtraTypeInformation());
        
        Object[] argumentsError = new Object[] { "test", 123 };
        
        Integer[] resultError = tool.getSuspiciouslyNotMatchingArgsIndexes(matchersError, argumentsError);
        
        assertEquals(0, resultError.length);
    }
}