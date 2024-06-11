import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_typicalCase() {
        // Arrange
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] expected = {1, 2};
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = {"abc", 123, 123.45};
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertArrayEquals(expected, result);
    }
  
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyMatchers() {
        // Arrange
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] expected = new Integer[0];
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = {};
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertArrayEquals(expected, result);
    }
  
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_differentMatcherAndArgumentTypes() {
        // Arrange
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] expected = {0};
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = {123};
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertArrayEquals(expected, result);
    }
  
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_differentNoOfMatchersAndArguments() {
        // Arrange
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] expected = new Integer[0];
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = {123, 456};
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertArrayEquals(expected, result);
    }
  
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsNullMatcher() {
        // Arrange
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] expected = new Integer[0];
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(null);
        Object[] arguments = {123};
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertArrayEquals(expected, result);
    }
  
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsNullArgument() {
        // Arrange
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] expected = new Integer[0];
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = {null};
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertArrayEquals(expected, result);
    }
  
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_exceptionInMatcherMatches() {
        // Arrange
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] expected = new Integer[0];
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = {"abc"};
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertArrayEquals(expected, result);
    }
  
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_exceptionInMatcherToString() {
        // Arrange
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] expected = new Integer[0];
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new CustomMatcherWithException());
        Object[] arguments = {123};
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertArrayEquals(expected, result);
    }
  
    // Custom matcher with exception for testing purposes
    private static class CustomMatcherWithException implements Matcher {
      
        @Override
        public boolean matches(Object arg) {
            throw new RuntimeException("Exception in matches() method");
        }
      
        @Override
        public void describeTo(Description description) {
            throw new RuntimeException("Exception in describeTo() method");
        }
    }

}