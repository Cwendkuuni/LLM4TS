import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {
    
    @Test
    public void testComputeValue_WhenArgsAreEqual_ExpectTrue() {
        // Arrange
        Expression arg1 = new Expression(); // dummy value
        Expression arg2 = new Expression(); // dummy value
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(); // dummy context

        // Act
        Object result = coreOperationEqual.computeValue(context);

        // Assert
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_WhenArgsAreNotEqual_ExpectFalse() {
        // Arrange
        Expression arg1 = new Expression(); // dummy value
        Expression arg2 = new Expression(); // dummy value
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(); // dummy context

        // Act
        Object result = coreOperationEqual.computeValue(context);

        // Assert
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testGetSymbol_ExpectEqualSymbol() {
        // Arrange
        Expression arg1 = new Expression(); // dummy value
        Expression arg2 = new Expression(); // dummy value
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        // Act
        String symbol = coreOperationEqual.getSymbol();

        // Assert
        assertEquals("=", symbol);
    }
}