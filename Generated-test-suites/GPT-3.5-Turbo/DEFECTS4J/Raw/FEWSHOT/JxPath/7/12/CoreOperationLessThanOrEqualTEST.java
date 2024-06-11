import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        
        EvalContext context = new EvalContext();
        context.setContextNode(new Object());
        
        // Test case 1
        context.setValue("arg1", 10);
        context.setValue("arg2", 10);
        assertEquals(Boolean.TRUE, op.computeValue(context));
        
        // Test case 2
        context.setValue("arg1", 5);
        context.setValue("arg2", 10);
        assertEquals(Boolean.TRUE, op.computeValue(context));
        
        // Test case 3
        context.setValue("arg1", 20);
        context.setValue("arg2", 10);
        assertEquals(Boolean.FALSE, op.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        assertEquals("<=", op.getSymbol());
    }
}