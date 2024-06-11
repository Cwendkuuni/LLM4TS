import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    
    private CoreOperationRelationalExpression expression;
    
    @Before
    public void setUp() {
        expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
    }
    
    @Test
    public void testComputeValue_LeftEqualsRight_ReturnsFalse() {
        // Arrange
        String left = "abc";
        String right = "abc";
        
        // Act
        Object result = expression.computeValue(left, right);
        
        // Assert
        Assert.assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftLessThanRight_ReturnsTrue() {
        // Arrange
        int left = 5;
        int right = 10;
        
        // Act
        Object result = expression.computeValue(left, right);
        
        // Assert
        Assert.assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftGreaterThanRight_ReturnsFalse() {
        // Arrange
        int left = 10;
        int right = 5;
        
        // Act
        Object result = expression.computeValue(left, right);
        
        // Assert
        Assert.assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftNaN_ReturnsFalse() {
        // Arrange
        double left = Double.NaN;
        double right = 5;
        
        // Act
        Object result = expression.computeValue(left, right);
        
        // Assert
        Assert.assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_RightNaN_ReturnsFalse() {
        // Arrange
        double left = 5;
        double right = Double.NaN;
        
        // Act
        Object result = expression.computeValue(left, right);
        
        // Assert
        Assert.assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftIteratorRightIterator_NoMatch_ReturnsFalse() {
        // Arrange
        Iterator<String> left = Arrays.asList("one", "two", "three").iterator();
        Iterator<String> right = Arrays.asList("four", "five").iterator();
        
        // Act
        Object result = expression.computeValue(left, right);
        
        // Assert
        Assert.assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftIteratorRightIterator_Match_ReturnsTrue() {
        // Arrange
        Iterator<String> left = Arrays.asList("one", "two", "three").iterator();
        Iterator<String> right = Arrays.asList("two", "five").iterator();
        
        // Act
        Object result = expression.computeValue(left, right);
        
        // Assert
        Assert.assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftIteratorRightValue_NoMatch_ReturnsFalse() {
        // Arrange
        Iterator<String> left = Arrays.asList("one", "two", "three").iterator();
        String right = "four";
        
        // Act
        Object result = expression.computeValue(left, right);
        
        // Assert
        Assert.assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftValueRightIterator_NoMatch_ReturnsFalse() {
        // Arrange
        String left = "four";
        Iterator<String> right = Arrays.asList("one", "two", "three").iterator();
        
        // Act
        Object result = expression.computeValue(left, right);
        
        // Assert
        Assert.assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftValueRightIterator_Match_ReturnsTrue() {
        // Arrange
        String left = "two";
        Iterator<String> right = Arrays.asList("one", "two", "three").iterator();
        
        // Act
        Object result = expression.computeValue(left, right);
        
        // Assert
        Assert.assertTrue((Boolean) result);
    }
}