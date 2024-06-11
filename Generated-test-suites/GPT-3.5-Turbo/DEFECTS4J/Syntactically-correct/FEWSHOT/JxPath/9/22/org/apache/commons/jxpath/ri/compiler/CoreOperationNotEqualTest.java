import static org.junit.Assert.*;

import org.junit.Test;

public class CoreOperationNotEqualTest {
    
    @Test
    public void testComputeValue() {
        // Test case using equal arguments
        CoreOperationNotEqual op = new CoreOperationNotEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        assertEquals(Boolean.FALSE, op.computeValue(context));
        
        // Test case using unequal arguments
        CoreOperationNotEqual op2 = new CoreOperationNotEqual(new Expression(), new Expression());
        Expression arg1 = op2.getArguments()[0];
        Expression arg2 = op2.getArguments()[1];
        // Set up different values for arg1 and arg2
        arg1.setValue("value1");
        arg2.setValue("value2");
        assertEquals(Boolean.TRUE, op2.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual op = new CoreOperationNotEqual(new Expression(), new Expression());
        assertEquals("!=", op.getSymbol());
    }
}