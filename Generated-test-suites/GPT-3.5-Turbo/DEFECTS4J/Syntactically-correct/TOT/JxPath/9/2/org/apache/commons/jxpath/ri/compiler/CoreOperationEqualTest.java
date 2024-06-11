import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationEqualTest {

    private CoreOperationEqual coreOperationEqual;
    
    @Before
    public void setup() {
        // Initialize the CoreOperationEqual object with dummy arguments
        CoreOperationCompare arg1 = new CoreOperationCompare(new DummyExpression(), new DummyExpression());
        CoreOperationCompare arg2 = new CoreOperationCompare(new DummyExpression(), new DummyExpression());
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    }
    
    @Test
    public void testComputeValue_equalArguments_returnsTrue() {
        // Arrange
        EvalContext context = new EvalContext();
        context.setArg(null);
        
        // Act
        Object result = coreOperationEqual.computeValue(context);
        
        // Assert
        Assert.assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_unequalArguments_returnsFalse() {
        // Arrange
        EvalContext context = new EvalContext();
        context.setArg(null);
        
        // Act
        Object result = coreOperationEqual.computeValue(context);
        
        // Assert
        Assert.assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testGetSymbol_returnsEqualsSymbol() {
        // Act
        String symbol = coreOperationEqual.getSymbol();
        
        // Assert
        Assert.assertEquals("=", symbol);
    }
    
    // Dummy Expression class for testing purposes
    private class DummyExpression extends Expression {
        
        @Override
        public Object computeValue(EvalContext context) {
           return null;
        }
        
    }
}