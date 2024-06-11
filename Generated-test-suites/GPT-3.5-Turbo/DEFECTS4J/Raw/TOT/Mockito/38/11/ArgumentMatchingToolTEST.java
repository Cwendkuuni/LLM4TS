import static org.junit.Assert.*;
import org.junit.Test;
import org.hamcrest.CoreMatchers;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

public class ArgumentMatchingToolTest {
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        Matcher matcher1 = CoreMatchers.isA(String.class);
        Matcher matcher2 = CoreMatchers.instanceOf(Integer.class);
        Matcher matcher3 = new ContainsExtraTypeInformation(Integer.class);
        Matcher matcher4 = CoreMatchers.equalTo("test");
        Matcher matcher5 = CoreMatchers.isA(Double.class);
        Matcher matcher6 = CoreMatchers.nullValue();
        
        Object[] arguments1 = {"test", 10, 10.5, null};
        Object[] arguments2 = {"test", 20, 15.0, null};
        Object[] arguments3 = {"test", 10, 10.5, "test"};
        Object[] arguments4 = {"test", 10, 10.5, null};
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Test when matchers and arguments have different lengths
        Integer[] result1 = tool.getSuspiciouslyNotMatchingArgsIndexes(
            List.of(matcher1, matcher2, matcher3), arguments1);
        assertArrayEquals(new Integer[0], result1);
        
        // Test when matchers and arguments have matching types and values
        Integer[] result2 = tool.getSuspiciouslyNotMatchingArgsIndexes(
            List.of(matcher1, matcher2, matcher3, matcher4), arguments1);
        assertArrayEquals(new Integer[0], result2);
        
        // Test when matchers and arguments have matching types but different values
        Integer[] result3 = tool.getSuspiciouslyNotMatchingArgsIndexes(
            List.of(matcher1, matcher2, matcher3, matcher4), arguments2);
        assertArrayEquals(new Integer[0], result3);
        
        // Test when matchers and arguments have matching types and matching toString() representation but different values
        Integer[] result4 = tool.getSuspiciouslyNotMatchingArgsIndexes(
            List.of(matcher1, matcher2, matcher3), arguments3);
        assertArrayEquals(new Integer[0], result4);
        
        // Test when matchers and arguments have matching types and matching toString() representation but different values, and extra type information does not match
        Integer[] result5 = tool.getSuspiciouslyNotMatchingArgsIndexes(
            List.of(matcher1, matcher2, matcher3), arguments4);
        assertArrayEquals(new Integer[]{2}, result5);
    }

    @Test
    public void testSafelyMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        // Test when Matcher matches the argument
        assertTrue(tool.safelyMatches(CoreMatchers.isA(String.class), "test"));
        
        // Test when Matcher does not match the argument and throws an exception
        assertFalse(tool.safelyMatches(CoreMatchers.isA(Integer.class), "test"));
    }
    
    @Test
    public void testToStringEquals() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        // Test when Matcher's toString() representation is equal to the argument's toString() representation
        assertTrue(tool.toStringEquals(CoreMatchers.isA(String.class), "test"));
        
        // Test when Matcher's toString() representation is not equal to the argument's toString() representation
        assertFalse(tool.toStringEquals(CoreMatchers.isA(String.class), "another"));
    }
}