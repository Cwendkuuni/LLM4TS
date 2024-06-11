import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // test values
        double l = 10.5;
        double r = 5.5;
        
        // set test values
        context.set(arg[0], l);
        context.set(arg[1], r);
        
        // compute value
        Object result = operation.computeValue(context);
        
        // check if l > r then result should be true
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        
        // check if symbol is >
        assertTrue(operation.getSymbol().equals(">"));
    }
    
}