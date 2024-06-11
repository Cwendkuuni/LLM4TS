import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        
        EvalContext context = new MockEvalContext();
        Object result = operation.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        
        String symbol = operation.getSymbol();
        
        assertEquals("!=", symbol);
    }
    
    // Additional test cases for edge cases and exception handling scenarios can be added here
    
}

class MockExpression extends Expression {
    public Object computeValue(EvalContext context) {
        return null;
    }
}

class MockEvalContext extends EvalContext {
    // Mock implementation for EvalContext
}