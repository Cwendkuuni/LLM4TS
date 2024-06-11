import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue_LessThan() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setContextNode(new Node());
        
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_GreaterThan() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setContextNode(new Node());
        
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        String symbol = expression.getSymbol();
        assertEquals("<=", symbol);
    }
}