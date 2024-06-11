import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationLessThanTest {
    
    private CoreOperationLessThan operation;

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        
        // Test when arg1 is less than arg2
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression(arg1), new Expression(arg2));
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
        
        // Test when arg1 is greater than or equal to arg2
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression(arg1), new Expression(arg2));
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression(arg1), new Expression(arg2));
        
        String symbol = operation.getSymbol();
        assertEquals("<", symbol);
    }
}