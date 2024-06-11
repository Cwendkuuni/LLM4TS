import org.junit.Assert;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_BothArgumentsNull_ShouldReturnFalse() {
        // Arrange
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, null);
        EvalContext context = new EvalContext();
        
        // Act
        Object result = operation.computeValue(context);
        
        // Assert
        Assert.assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_FirstArgumentNullSecondArgumentNotNull_ShouldReturnTrue() {
        // Arrange
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, "value");
        EvalContext context = new EvalContext();
        
        // Act
        Object result = operation.computeValue(context);
        
        // Assert
        Assert.assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_FirstArgumentNotNullSecondArgumentNull_ShouldReturnTrue() {
        // Arrange
        CoreOperationNotEqual operation = new CoreOperationNotEqual("value", null);
        EvalContext context = new EvalContext();
        
        // Act
        Object result = operation.computeValue(context);
        
        // Assert
        Assert.assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_BothArgumentsEqual_ShouldReturnFalse() {
        // Arrange
        CoreOperationNotEqual operation = new CoreOperationNotEqual("value", "value");
        EvalContext context = new EvalContext();
        
        // Act
        Object result = operation.computeValue(context);
        
        // Assert
        Assert.assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_BothArgumentsNotEqual_ShouldReturnTrue() {
        // Arrange
        CoreOperationNotEqual operation = new CoreOperationNotEqual("value1", "value2");
        EvalContext context = new EvalContext();
        
        // Act
        Object result = operation.computeValue(context);
        
        // Assert
        Assert.assertTrue((Boolean) result);
    }
    
    @Test
    public void testGetSymbol_ShouldReturnNotEqualSymbol() {
        // Arrange
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, null);
        
        // Act
        String symbol = operation.getSymbol();
        
        // Assert
        Assert.assertEquals("!=", symbol);
    }
}