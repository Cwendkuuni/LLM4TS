import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {
    
    @Test
    public void testComputeValue_typicalCase_returnTrue() {
        // Arrange
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
        context.setVariable("arg1", arg1);
        context.setVariable("arg2", arg2);
        
        // Act
        Object result = operationEqual.computeValue(context);
        
        // Assert
        assertEquals(true, result);
    }
    
    @Test
    public void testComputeValue_typicalCase_returnFalse() {
        // Arrange
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
        context.setVariable("arg1", arg1);
        context.setVariable("arg2", new Expression());
        
        // Act
        Object result = operationEqual.computeValue(context);
        
        // Assert
        assertEquals(false, result);
    }
    
    @Test
    public void testComputeValue_nullArguments_returnFalse() {
        // Arrange
        EvalContext context = new EvalContext();
        CoreOperationEqual operationEqual = new CoreOperationEqual(null, null);
        
        // Act
        Object result = operationEqual.computeValue(context);
        
        // Assert
        assertEquals(false, result);
    }
    
    @Test
    public void testGetSymbol_returnEqualSymbol() {
        // Arrange
        CoreOperationEqual operationEqual = new CoreOperationEqual(new Expression(), new Expression());
        
        // Act
        String symbol = operationEqual.getSymbol();
        
        // Assert
        assertEquals("=", symbol);
    }
}