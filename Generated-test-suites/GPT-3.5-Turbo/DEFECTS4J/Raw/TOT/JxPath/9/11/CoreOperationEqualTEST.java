import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationEqualTest {
    
    private CoreOperationEqual coreOperationEqual;

    @Before
    public void setUp() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue_NormalValues_Equal() {
        // Arrange
        EvalContext context = new EvalContext();

        // Act
        Object result = coreOperationEqual.computeValue(context);

        // Assert
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_NormalValues_NotEqual() {
        // Arrange
        EvalContext context = new EvalContext();
        // Assume arg1 and arg2 are not equal
        
        // Act
        Object result = coreOperationEqual.computeValue(context);

        // Assert
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_NullArgs() {
        // Arrange
        EvalContext context = new EvalContext();
        coreOperationEqual = new CoreOperationEqual(null, null);
        
        // Act
        Object result = coreOperationEqual.computeValue(context);

        // Assert
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_OneNullArg() {
        // Arrange
        EvalContext context = new EvalContext();
        coreOperationEqual = new CoreOperationEqual(new Expression(), null);
        
        // Act
        Object result = coreOperationEqual.computeValue(context);

        // Assert
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        // Act
        String symbol = coreOperationEqual.getSymbol();

        // Assert
        assertEquals("=", symbol);
    }
}