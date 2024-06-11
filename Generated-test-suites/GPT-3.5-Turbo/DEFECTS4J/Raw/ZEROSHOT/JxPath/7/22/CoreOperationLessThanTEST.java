import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression(arg1), new Expression(arg2));
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 10);
        
        Object result = operation.computeValue(context);
        
        assertFalse(result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression(arg1), new Expression(arg2));
        
        String symbol = operation.getSymbol();
        
        assertEquals("<", symbol);
    }
}