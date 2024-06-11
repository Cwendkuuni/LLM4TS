import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        
        // Test when l <= r, should return true
        EvalContext context1 = new EvalContext();
        context1.setVariableValue("l", 5);
        context1.setVariableValue("r", 10);
        assertTrue((boolean) expression.computeValue(context1));
        
        // Test when l > r, should return false
        EvalContext context2 = new EvalContext();
        context2.setVariableValue("l", 15);
        context2.setVariableValue("r", 10);
        assertFalse((boolean) expression.computeValue(context2));
        
        // Test when l == r, should return true
        EvalContext context3 = new EvalContext();
        context3.setVariableValue("l", 10);
        context3.setVariableValue("r", 10);
        assertTrue((boolean) expression.computeValue(context3));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        
        assertEquals("<=", expression.getSymbol());
    }
}