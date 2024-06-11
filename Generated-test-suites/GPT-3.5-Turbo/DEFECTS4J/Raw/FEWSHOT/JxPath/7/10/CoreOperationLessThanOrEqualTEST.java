import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setContextNode(new Node());
        context.setContextPosition(1);
        context.setContextSize(10);
        
        context.setVariable("var1", "value1");
        context.setVariable("var2", "value2");
        
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        
        // Add more test cases here
        
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        assertEquals("<=", operation.getSymbol());
    }

}