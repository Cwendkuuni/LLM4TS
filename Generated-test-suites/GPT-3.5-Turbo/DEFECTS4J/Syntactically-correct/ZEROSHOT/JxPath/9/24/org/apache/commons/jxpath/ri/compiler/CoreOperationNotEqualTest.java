import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test when the arg1 and arg2 are equal
        arg1.setValue(5);
        arg2.setValue(5);
        assertEquals(Boolean.FALSE, operationNotEqual.computeValue(context));

        // Test when the arg1 and arg2 are not equal
        arg1.setValue(5);
        arg2.setValue(10);
        assertEquals(Boolean.TRUE, operationNotEqual.computeValue(context));
        
        // Add more test cases...
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertEquals("!=", operationNotEqual.getSymbol());
    }
}