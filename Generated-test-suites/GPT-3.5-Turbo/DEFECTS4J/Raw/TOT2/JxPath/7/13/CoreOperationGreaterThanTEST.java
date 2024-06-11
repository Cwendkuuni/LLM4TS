import static org.junit.Assert.*;

import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(null, null);
        
        // Typical use case
        double arg1 = 5.0;
        double arg2 = 3.0;
        assertEquals(Boolean.TRUE, operation.computeValue(createEvalContext(arg1, arg2)));
        
        // Edge case
        arg1 = Double.MIN_VALUE;
        arg2 = Double.MAX_VALUE;
        assertEquals(Boolean.FALSE, operation.computeValue(createEvalContext(arg1, arg2)));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(null, null);
        
        // Typical use case
        assertEquals(">", operation.getSymbol());
    }
    
    private EvalContext createEvalContext(double arg1, double arg2) {
        EvalContext context = new EvalContext(null);
        context.setNodeSet(new Double[] {arg1, arg2});
        return context;
    }
}