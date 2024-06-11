import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoreOperationGreaterThanOrEqualTest {
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        
        // Test case 1
        EvalContext context1 = new EvalContext();
        context1.setVariable("arg1", 1);
        context1.setVariable("arg2", 2);
        assertEquals(Boolean.FALSE, op.computeValue(context1));
        
        // Test case 2
        EvalContext context2 = new EvalContext();
        context2.setVariable("arg1", 2);
        context2.setVariable("arg2", 1);
        assertEquals(Boolean.TRUE, op.computeValue(context2));
        
        // Test case 3
        EvalContext context3 = new EvalContext();
        context3.setVariable("arg1", 3.5);
        context3.setVariable("arg2", 3.5);
        assertEquals(Boolean.TRUE, op.computeValue(context3));
        
        // Test case 4
        EvalContext context4 = new EvalContext();
        context4.setVariable("arg1", -1);
        context4.setVariable("arg2", -2);
        assertEquals(Boolean.TRUE, op.computeValue(context4));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        assertEquals(">=", op.getSymbol());
    }
}