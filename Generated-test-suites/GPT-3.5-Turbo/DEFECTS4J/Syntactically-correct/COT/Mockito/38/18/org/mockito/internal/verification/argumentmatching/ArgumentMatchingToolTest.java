import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Basic test case - matchers and arguments have the same size
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[1];
        arguments[0] = new ContainsExtraTypeInformation();
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesDifferentSizes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Test case - matchers and arguments have different sizes
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[1];
        arguments[0] = new ContainsExtraTypeInformation();
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        Assert.assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesTypeMismatch() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Test case - type mismatch
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[1];
        arguments[0] = "String";
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        Assert.assertEquals(1, result.length);
        Assert.assertEquals(0, result[0].intValue());
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesTypeMatch() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Test case - type matches
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[1];
        arguments[0] = new ContainsExtraTypeInformation();
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        Assert.assertEquals(0, result.length);
    }
    
    @Test
    public void testSafelyMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Test case - matcher throws an exception
        Matcher matcher = new ContainsExtraTypeInformation();
        Object arg = new ContainsExtraTypeInformation();
        
        boolean result = tool.safelyMatches(matcher, arg);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testToStringEquals() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Test case - toString() of matcher equals toString() of argument
        Matcher matcher = new ContainsExtraTypeInformation();
        Object arg = new ContainsExtraTypeInformation();
        
        boolean result = tool.toStringEquals(matcher, arg);
        
        Assert.assertTrue(result);
    }
}