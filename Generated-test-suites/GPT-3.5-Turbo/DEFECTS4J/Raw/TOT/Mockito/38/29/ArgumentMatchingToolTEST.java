import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatchersSizeDifferentFromArgumentsLength_ReturnsEmptyArray() {
        // Arrange
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(new LinkedList<>(), new Object[]{});
        
        // Assert
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatchersContainsExtraTypeInformationAndMatches_ReturnsEmptyArray() {
        // Arrange
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        LinkedList<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = { new Object() };
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatchersContainsExtraTypeInformationAndDoesNotMatch_ReturnsSuspiciousIndex() {
        // Arrange
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        LinkedList<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = { "test" };
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatchersDoesNotContainExtraTypeInformationAndMatches_ReturnsEmptyArray() {
        // Arrange
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        LinkedList<Matcher> matchers = new LinkedList<>();
        matchers.add((Matcher)15);
        Object[] arguments = { 15 };
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatchersDoesNotContainExtraTypeInformationAndDoesNotMatch_ReturnsSuspiciousIndex() {
        // Arrange
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        LinkedList<Matcher> matchers = new LinkedList<>();
        matchers.add((Matcher)15);
        Object[] arguments = { "15" };
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatchersContainsExtraTypeInformationAndMatcherThrowsException_ReturnsSuspiciousIndex() {
        // Arrange
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        LinkedList<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation() {
            @Override
            public boolean matches(Object item) {
                throw new RuntimeException("Test exception");
            }
        });
        Object[] arguments = { "test" };
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatchersContainsExtraTypeInformationAndToStringDoesNotEqualArgumentToString_ReturnsSuspiciousIndex() {
        // Arrange
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        LinkedList<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = { "different" };
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatchersContainsExtraTypeInformationAndMatcherDoesNotTypeMatch_ReturnsSuspiciousIndex() {
        // Arrange
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        LinkedList<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = { (short) 15 };
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
    }
}