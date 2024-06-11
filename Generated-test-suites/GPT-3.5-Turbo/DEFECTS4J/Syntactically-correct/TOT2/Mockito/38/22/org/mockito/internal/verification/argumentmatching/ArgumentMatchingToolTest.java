import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.hamcrest.Matchers;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        // Typical use case
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Matchers.isA(String.class));
        matchers.add(Matchers.isA(Integer.class));
        Object[] arguments = {"test", 123};
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
        
        // Edge case: Empty input
        matchers.clear();
        arguments = new Object[]{};
        result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
        
        // Edge case: Different types but toString() representation is the same
        matchers.add(Matchers.isA(Double.class));
        arguments = new Object[]{1.0};
        result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
        
        // Edge case: Suspicious arguments with extra type information
        matchers.clear();
        matchers.add(new ContainsExtraTypeInformation(String.class));
        arguments = new Object[]{"test"};
        result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(1, result.length);
        assertEquals((Integer) 0, result[0]);
        
        // Error scenario: Exception in matcher comparison
        matchers.clear();
        matchers.add(Matchers.isA(String.class));
        arguments = new Object[]{new Object()};
        result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
    }

}