import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_shouldReturnEmptyArrayWhenMatchersAndArgumentsSizeDifferent() {
        // Arrange
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        
        // Act
        Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(Arrays.asList(CoreMatchers.any()), new Object[]{});
        
        // Assert
        Assert.assertEquals(0, indexes.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_shouldNotAddIndexToSuspiciousListWhenMatcherMatches() {
        // Arrange
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        Matcher matcher = CoreMatchers.equalTo(5);
        List<Matcher> matchers = Arrays.asList(matcher);
        Object[] arguments = new Object[]{5};
        
        // Act
        Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        Assert.assertEquals(0, indexes.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_shouldAddIndexToSuspiciousListWhenMatcherDoesNotMatch() {
        // Arrange
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        Matcher matcher = CoreMatchers.equalTo(5);
        List<Matcher> matchers = Arrays.asList(matcher);
        Object[] arguments = new Object[]{"5"};
        
        // Act
        Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        Assert.assertEquals(1, indexes.length);
        Assert.assertEquals(Integer.valueOf(0), indexes[0]);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_shouldAddIndexToSuspiciousListWhenMatcherTypeDoesNotMatch() {
        // Arrange
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        Matcher matcher = CoreMatchers.instanceOf(Integer.class);
        List<Matcher> matchers = Arrays.asList(matcher);
        Object[] arguments = new Object[]{5.5};
        
        // Act
        Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        Assert.assertEquals(1, indexes.length);
        Assert.assertEquals(Integer.valueOf(0), indexes[0]);
    }
    
    @Test
    public void testSafelyMatches_shouldReturnTrueWhenMatcherMatches() {
        // Arrange
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        Matcher matcher = CoreMatchers.equalTo(5);
        
        // Act
        boolean result = argumentMatchingTool.safelyMatches(matcher, 5);
        
        // Assert
        Assert.assertTrue(result);
    }
    
    @Test
    public void testSafelyMatches_shouldReturnFalseWhenMatcherThrowsException() {
        // Arrange
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        Matcher matcher = (arg) -> { throw new RuntimeException(); };
        
        // Act
        boolean result = argumentMatchingTool.safelyMatches(matcher, 5);
        
        // Assert
        Assert.assertFalse(result);
    }
    
    @Test
    public void testToStringEquals_shouldReturnTrueWhenMatcherToStringEqualsArgumentToString() {
        // Arrange
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        Matcher matcher = CoreMatchers.equalTo(5);
        
        // Act
        boolean result = argumentMatchingTool.toStringEquals(matcher, "5");
        
        // Assert
        Assert.assertTrue(result);
    }
    
    @Test
    public void testToStringEquals_shouldReturnFalseWhenMatcherToStringDiffersFromArgumentToString() {
        // Arrange
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        Matcher matcher = CoreMatchers.equalTo(5);
        
        // Act
        boolean result = argumentMatchingTool.toStringEquals(matcher, "6");
        
        // Assert
        Assert.assertFalse(result);
    }
}